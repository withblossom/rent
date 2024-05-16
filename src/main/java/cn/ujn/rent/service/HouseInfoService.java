package cn.ujn.rent.service;

import cn.ujn.rent.bean.HouseInfo;
import java.util.List;

public interface HouseInfoService {
    HouseInfo getInfoByHouseId(Integer houseId);

    void admitProfile(Integer houseId, String profile);

    void admitLabels(Integer houseId, List<String> labels);

    void addLabel(Integer houseId, String label);

    void deleteLabel(Integer houseId, String label);
}
