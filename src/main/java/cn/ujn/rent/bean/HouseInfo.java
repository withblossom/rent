package cn.ujn.rent.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class HouseInfo {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer houseId;
    String profile;
    String labels;
}
