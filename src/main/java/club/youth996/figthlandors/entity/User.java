package club.youth996.figthlandors.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
*  user
* @author Zhan Xinjian 2020-04-17
*/
@SuppressWarnings("ALL")
@TableName("USER")
@KeySequence(value = "S_USER", clazz = Integer.class)
@Data
public class User {

    /**
    * id，主键自增
    */
    @TableId(value = "id",type = IdType.INPUT)
    private Integer id;
    /**
    * 用户名
    */
    private String username;
    /**
    * 密码
    */
    private String password;
    /**
    * 创建时间
    */
    private Date createTime;

    /**
     * 用户具有的牌局信息
     */
    private Integer cardGameID;

}