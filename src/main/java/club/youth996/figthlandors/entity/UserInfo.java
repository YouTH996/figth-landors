package club.youth996.figthlandors.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
*  user_info
* @author Zhan Xinjian 2020-04-17
*/
@SuppressWarnings("ALL")
@TableName("USER_INFO")
@KeySequence(value = "S_USER_INFO", clazz = Integer.class)
@Data
public class UserInfo {

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
    * 昵称
    */
    private String nickname;
    /**
    * 性别，0为男，1为女
    */
    private Integer sex;
    /**
    * 积分
    */
    private Integer score;
}