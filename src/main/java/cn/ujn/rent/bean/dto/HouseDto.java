package cn.ujn.rent.bean.dto;

import lombok.Data;

@Data
public class HouseDto {
    Integer id;
    String name;
    Double price;
    Byte unit;
    String location;
    Byte state;
    Integer owner;
    String ownerName;
    String avatar;
    Byte tradingMode;
}
