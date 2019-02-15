package photostation.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;
import photostation.core.untils.FileUtil;
import photostation.dao.MongoUserDao;
import photostation.model.MongoUser;
import photostation.model.UserFile;
import photostation.service.MongoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import photostation.service.UserFileService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 *
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email: xiaomo@xiaomo.info
 * <p>
 * Date: 2016/11/15 15:45
 * Copyright(©) 2015 by xiaomo.
 **/

@Service
public class MongoUserServiceImpl implements MongoUserService {
    private final MongoUserDao dao;

    @Autowired
    public MongoUserServiceImpl(MongoUserDao dao) {
        this.dao = dao;
    }

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private UserFileService userFileService;

    @Value("${server.port}")
    private String port;

    @Value("${server.hostname}")
    private String hostname;

    @Override
    public List<MongoUser> findAll() {
        return dao.findAll();
    }

    @Override
    public MongoUser findById(Long id) {
        Optional<MongoUser> optionalUser = dao.findById(id);
        return optionalUser.orElse(null);
    }

    @Override
    public MongoUser findByName(String userName) {
        return dao.findByUserName(userName);
    }

    @Override
    public MongoUser add(MongoUser mongoUser) {
        return dao.save(mongoUser);
    }

    @Override
    public void delete(Long id) {
        Optional<MongoUser> optional = dao.findById(id);
        if (!optional.isPresent()) {
            return;
        }
        dao.delete(optional.get());
    }

    @Override
    public MongoUser update(MongoUser mongoUser) {
        return dao.save(mongoUser);
    }

    @Override
    public String upload(MultipartFile file, String fileName, String user_id) {
        // GridFS gridFS = new GridFS(mongodbfactory.getLegacyDb(), "photo");
        // LOGGER.info("Saving file..");
        // DBObject metaData = new BasicDBObject();
        BasicDBObject metaData = new BasicDBObject();
        Date date = new Date();
        metaData.put("createdDate", date);

        if (fileName.isEmpty()) {
            fileName = file.getOriginalFilename();
        }

        // String oldName = FileUtil.getNamePart(fileName);
        String oldName = StringUtils.substringBefore(fileName, ".");

        //Date ------> Date对象
        //创建日期格式化对象   因为DateFormat类为抽象类 所以不能new
        DateFormat bf = new SimpleDateFormat("yyyyMMddHHmmss");//多态
        //2017-04-19 星期三 下午 20:17:38

        // Date date = new Date();//创建时间
        String format = bf.format(date);//格式化 bf.format(date);

        String newName = oldName + "_" + format + "." + FileUtil.getFileType(file.getOriginalFilename());

        // LOGGER.info("File Name: " + fileName);

        InputStream inputStream = null;

        ObjectId objectId;
        try {
            inputStream = file.getInputStream();
            objectId = gridFsTemplate.store(inputStream, newName, file.getContentType(), metaData);
            ObjectId _id = (ObjectId) metaData.get("_id");

            // LOGGER.info("File saved: " + fileName);
            ObjectId oid = new ObjectId(objectId.getTimestamp(), objectId.getMachineIdentifier(), objectId.getProcessIdentifier(), objectId.getCounter());
            // System.out.println("ObjectId: " + oid.equals(objectId) + "oid: " + oid.toHexString() + ";objectid: " + objectId.toHexString());
        } catch (IOException e) {
            // LOGGER.error("IOException: " + e);
            throw new RuntimeException("System Exception while handling request");
        }
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(GridFsCriteria.where("_id").is(objectId.toHexString())));
        System.out.println(gridFSFile.getFilename());

        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        UserFile userFile = new UserFile();
        userFile.setFilename(newName);
        userFile.setUser_id(user_id);
        userFile.setCreateDate(date.toString());
        userFileService.add(userFile);

        // String fileUrl = "http://" + address.getHostAddress() + ":" + port + "/mongodb/img/" + newName;
        String fileUrl = "http://" + hostname + ":" + port + "/mongodb/img/" + newName;
        // LOGGER.info("File return: " + fileName);

        return fileUrl;
    }

    @Override
    public byte[] fileImage(String fileName, int w) throws IOException {
        // LOGGER.info("Getting file.." + fileName);
        GridFSFindIterable result = gridFsTemplate
                .find(new Query().addCriteria(Criteria.where("filename").is(fileName)));
        if (result == null) {
            // LOGGER.info("File not found" + fileName);
            throw new RuntimeException("No file with name: " + fileName);
        }
        GridFsResource gridFsResource = gridFsTemplate.getResource(fileName);
        // LOGGER.info("File found " + fileName);
        byte[] data = IOUtils.toByteArray(gridFsResource.getInputStream());
        byte[] finalImage = null;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BufferedImage img = ImageIO.read(in);
        int width = img.getWidth();
        int height = img.getHeight();
        float scal = w / width;
        int h = (int) (height * scal);
        System.out.println("photo name: " + fileName + ", image width: " + width + "; image height: " + height);
        BufferedImage dimg = new BufferedImage(w, w, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, w, h, 0, 0, width, height, null);
        g.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(dimg, "jpg", byteArrayOutputStream);
        finalImage = byteArrayOutputStream.toByteArray();

        // File imageFile = new File("D:\\" + gridFsResource.getFilename());
        // FileImageOutputStream imageOutput = new FileImageOutputStream((imageFile));
        // imageOutput.write(data, 0, data.length);
        // imageOutput.close();

        String encoded = Base64.getEncoder().encodeToString(data);

        // System.out.println(encoded);
        return data;
    }

    @Override
    public List<byte[]> fileImages() throws IOException {
        GridFsResource[] gridFsResources = gridFsTemplate.getResources("*");
        List<byte[]> bytes = new ArrayList<>();
        for (GridFsResource gridFsResource : gridFsResources) {
            byte[] data = IOUtils.toByteArray(gridFsResource.getInputStream());
            bytes.add(data);
        }
        return bytes;
    }

    @Override
    public List<byte[]> userImages(String user_id) throws IOException {
        List<UserFile> userFiles = userFileService.findAllByUser_id(user_id);
        List<byte[]> bytes = new ArrayList<>();
        for (UserFile userFile : userFiles) {
            bytes.add(this.fileImage(userFile.getFilename(),375));
        }
        return bytes;
    }

}
