package photostation.service;
import photostation.model.UserFile;

import java.util.List;

/**
 * @program: Dragonsking
 * @description: 服务层
 * @author: hezijian6338
 * @create: 2019-02-15 13:29
 **/

public interface UserFileService {

    /**
     * 查所有
     *
     * @return
     */
    List<UserFile> findAll();

    /**
     * 根据id查
     *
     * @param id
     * @return
     */
    UserFile findById(String id);


    /**
     * 添加
     *
     * @param userFile
     * @return
     */
    UserFile add(UserFile userFile);

    /**
     * 删除
     *
     * @param id
     */
    void delete(String id);

    List<UserFile> findAllByUser_id(String User_id);

}
