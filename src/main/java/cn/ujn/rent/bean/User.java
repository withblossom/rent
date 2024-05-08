package cn.ujn.rent.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {
    @TableId(type = IdType.AUTO)
    Integer id;
    String username;
    Integer age;
    Boolean sex;
    String phoneNumber;
    String password;
    Boolean vipStatus;
    String avatar;
}
