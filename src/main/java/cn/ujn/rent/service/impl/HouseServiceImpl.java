package cn.ujn.rent.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ujn.rent.bean.DetailedInfo;
import cn.ujn.rent.bean.House;
import cn.ujn.rent.bean.HouseInfo;
import cn.ujn.rent.bean.User;
import cn.ujn.rent.bean.dto.HouseDto;
import cn.ujn.rent.cache.CacheService;
import cn.ujn.rent.error.RentException;
import cn.ujn.rent.mapper.HouseMapper;
import cn.ujn.rent.service.HouseInfoService;
import cn.ujn.rent.service.HouseService;
import cn.ujn.rent.service.UserService;
import cn.ujn.rent.utils.SystemConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {

    @Resource
    HouseMapper houseMapper;

    @Resource
    HouseInfoService houseInfoService;
    @Resource
    UserService userService;
    @Resource
    CacheService cacheManager;

    @Override
    public boolean addHouse(House house) {
        if (house.getUnit() == SystemConstants.TimeUnitEnum.FOREVERUNIT.ordinal()) {
            house.setTradingMode((byte) SystemConstants.TradingMode.BUYING.ordinal());
        }
        return houseMapper.insert(house) != 0;
    }

    @Override
    public List<House> getAllHousesForSelect() {
        LambdaQueryWrapper<House> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(House::getId, House::getName, House::getTradingMode);
        return houseMapper.selectList(queryWrapper);
    }

    @Override
    public DetailedInfo<HouseDto> getHouseInfo(Integer houseId) {
        DetailedInfo<HouseDto> detailedInfo = new DetailedInfo<>();

        House house = getHouseById(houseId);
        HouseDto houseDto = BeanUtil.copyProperties(house, HouseDto.class);
        User user = userService.getUserById(house.getOwner());
        if(user != null){
            houseDto.setOwnerName(user.getUsername());
        }

        detailedInfo.setObj(houseDto);
        HouseInfo info = houseInfoService.getInfoByHouseId(houseId);

        if (info != null) {
            BeanUtil.copyProperties(info, detailedInfo, "id", "labels");
            Set<String> labels = StringUtils.commaDelimitedListToSet(info.getLabels());
            detailedInfo.setLabels(labels);
        }
        return detailedInfo;
    }

    @Override
    public boolean updateHouseById(House house, Integer houseId) {
        cacheManager.remove(SystemConstants.HOUSE_CACHE_SUFFIX + houseId);
        house.setId(houseId);
        return houseMapper.updateById(house) != 0;
    }

    @Override
    public boolean deleteHouseById(Integer houseId) {
        House house = getHouseById(houseId);

        if (house.getState() != 0) {
            RentException.cast("该房产正在使用中，不可删除！！！");
        }

        cacheManager.remove(SystemConstants.HOUSE_CACHE_SUFFIX + houseId);
        return houseMapper.deleteById(houseId) != 0;
    }

    @Override
    public House getHouseById(Integer houseId) {
        String cacheKey = SystemConstants.HOUSE_CACHE_SUFFIX + houseId;
        House house = (House) cacheManager.get(cacheKey, House.class);
        if (house == null) {
            house = houseMapper.selectById(houseId);
            cacheManager.put(cacheKey, house);
        }
        return house;
    }

    @Override
    public List<HouseDto> getHousesPage(int pageNum, int pageSize, String housenameLike, Byte houseState) {
        LambdaQueryWrapper<House> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(StringUtils.hasLength(housenameLike),House::getName,housenameLike);
        queryWrapper.eq(houseState!=null,House::getState,houseState);

        Page<House> housePage = new Page<>(pageNum, pageSize);
        houseMapper.selectPage(housePage, queryWrapper);

        return housePage.getRecords().stream().map((house -> {
            User user = userService.getUserById(house.getOwner());
            HouseDto houseDto = BeanUtil.copyProperties(house, HouseDto.class);
            if (user != null){
                houseDto.setOwnerName(user.getUsername());
            }
            return houseDto;
        })).collect(Collectors.toList());
    }

    @Override
    public boolean updateHouse(LambdaUpdateWrapper<House> updateWrapper,Integer houseId) {
        cacheManager.remove(SystemConstants.HOUSE_CACHE_SUFFIX + houseId);
        return houseMapper.update(null, updateWrapper) != 0;
    }

    @Override
    public List<House> getHouses(LambdaQueryWrapper<House> queryWrapper) {
        return houseMapper.selectList(queryWrapper);
    }

    @Override
    public Long countHouses(LambdaQueryWrapper<House> queryWrapper) {
        return houseMapper.selectCount(queryWrapper);
    }

}
