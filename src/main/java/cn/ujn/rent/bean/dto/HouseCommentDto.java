package cn.ujn.rent.bean.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HouseCommentDto {
    Integer id;
    Integer houseId;
    String houseName;
    Integer userId;
    String username;
    String comment;
    LocalDateTime createTime;
}
