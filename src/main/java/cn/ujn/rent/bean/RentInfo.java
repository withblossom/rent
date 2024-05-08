package cn.ujn.rent.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentInfo {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer houseId;
    Integer userId;
    LocalDateTime startTime;
    Integer time;
    Double totalPrice;
    Byte state;
    String reason;
}
