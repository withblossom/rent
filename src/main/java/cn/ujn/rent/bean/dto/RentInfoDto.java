package cn.ujn.rent.bean.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RentInfoDto {
    Integer id;
    Integer houseId;
    Integer userId;
    LocalDateTime startTime;
    Integer time;
    Byte unit;
    Double totalPrice;
    String houseName;
    String username;
    String phoneNumber;
    Byte state;
    String reason;
    Byte tradingMode;
}
