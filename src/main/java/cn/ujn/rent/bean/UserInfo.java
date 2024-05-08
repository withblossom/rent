package cn.ujn.rent.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserInfo {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer userId;
    String profile;
    String labels;
}
