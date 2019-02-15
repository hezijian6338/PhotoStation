package photostation.dao;

import org.springframework.stereotype.Repository;
import photostation.Base.BaseDao;
import photostation.model.UserFile;

import java.util.List;

/**
 * @program: Dragonsking
 * @description: 用户实体类
 * @author: hezijian6338
 * @create: 2019-02-15 13:24
 **/

@Repository
public interface UserFileDao extends BaseDao<UserFile, String> {
}
