package photostation.service;

import org.springframework.web.multipart.MultipartFile;
import photostation.model.MongoUser;

import java.io.IOException;
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


public interface MongoUserService {

    /**
     * 查所有
     *
     * @return
     */
    List<MongoUser> findAll();

    /**
     * 根据id查
     *
     * @param id
     * @return
     */
    MongoUser findById(Long id);

    /**
     * 根据名字查
     *
     * @param userName
     * @return
     */
    MongoUser findByName(String userName);

    /**
     * 添加
     *
     * @param mongoUser
     * @return
     */
    MongoUser add(MongoUser mongoUser);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 更新
     *
     * @param mongoUser
     * @return
     */
    MongoUser update(MongoUser mongoUser);

    String upload(MultipartFile file, String fileName, String user_id);

    byte[] fileImage(String fileName, int w) throws IOException;

    List<byte[]> fileImages() throws IOException;

    List<String> userImages(String user_id) throws IOException;

}
