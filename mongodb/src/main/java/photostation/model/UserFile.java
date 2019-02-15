package photostation.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

/**
 * @program: Dragonsking
 * @description: 用来存放用户上传的图片文件名称
 * @author: hezijian6338
 * @create: 2019-02-15 13:15
 **/

@Data
@ToString(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UserFile {

    @Id
    private int id;

    @ApiModelProperty(value = "用户的id")
    private String user_id;

    @ApiModelProperty(value = "用户存放的图片的名字")
    private String filename;

    @ApiModelProperty(value = "用户存放图片的时间")
    private String createDate;

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getId() {
        return id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getFilename() {
        return filename;
    }
}
