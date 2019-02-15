package photostation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.stereotype.Service;
import photostation.dao.UserFileDao;
import photostation.model.UserFile;
import photostation.service.UserFileService;

import java.util.List;
import java.util.Optional;

/**
 * @program: Dragonsking
 * @description: 实现类
 * @author: hezijian6338
 * @create: 2019-02-15 13:33
 **/


@Service
public class UserFileServiceImpl implements UserFileService {

    private final UserFileDao dao;

    @Autowired
    public UserFileServiceImpl(UserFileDao dao) {
        this.dao = dao;
    }

    @Autowired
    public MongoTemplate mongoTemplate;

    @Override
    public List<UserFile> findAll() {
        return dao.findAll();
    }

    @Override
    public UserFile findById(String id) {
        Optional<UserFile> optionalUser = dao.findById(id);
        return optionalUser.orElse(null);
    }


    @Override
    public UserFile add(UserFile mongoUser) {
        return dao.save(mongoUser);
    }

    @Override
    public void delete(String id) {
        Optional<UserFile> optional = dao.findById(id);
        if (!optional.isPresent()) {
            return;
        }
        dao.delete(optional.get());
    }

    @Override
    public List<UserFile> findAllByUser_id(String user_id) {
        return mongoTemplate.find(Query.query(GridFsCriteria.where("user_id").is(user_id)),UserFile.class);
    }

}
