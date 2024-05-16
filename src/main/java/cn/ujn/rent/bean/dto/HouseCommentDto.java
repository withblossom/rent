package cn.ujn.rent.bean.dto;

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
