package cn.ujn.rent.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class House {
    @TableId(type = IdType.AUTO)
    Integer id;
    String name;
    Double price;
    Byte unit;
    String location;
    Byte state;
    Integer owner;
    String avatar;
    Byte tradingMode;
}
