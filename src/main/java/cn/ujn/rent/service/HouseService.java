package cn.ujn.rent.service;

import cn.ujn.rent.bean.DetailedInfo;
import cn.ujn.rent.bean.House;
import cn.ujn.rent.bean.User;
import cn.ujn.rent.bean.dto.HouseDto;
import cn.ujn.rent.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import java.util.List;

public interface HouseService {
    boolean addHouse(House house);

    List<House> getAllHousesForSelect();

    DetailedInfo<HouseDto> getHouseInfo(Integer houseId);

    boolean updateHouseById(House house, Integer houseId);

    boolean deleteHouseById(Integer houseId);

    House getHouseById(Integer houseId);

    List<HouseDto> getHousesPage(int pageNum, int pageSize, String housenameLike, Byte houseState);

    boolean updateHouse(LambdaUpdateWrapper<House> updateWrapper,Integer houseId);

    List<House> getHouses(LambdaQueryWrapper<House> queryWrapper);
}
