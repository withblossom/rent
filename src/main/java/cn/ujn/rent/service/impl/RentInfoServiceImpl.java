package cn.ujn.rent.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ujn.rent.bean.House;
import cn.ujn.rent.bean.RentInfo;
import cn.ujn.rent.bean.User;
import cn.ujn.rent.bean.dto.RentInfoDto;
import cn.ujn.rent.cache.CacheService;
import cn.ujn.rent.error.RentException;
import cn.ujn.rent.mapper.RentInfoMapper;
import cn.ujn.rent.service.HouseService;
import cn.ujn.rent.service.RentInfoService;
import cn.ujn.rent.service.UserService;
import cn.ujn.rent.utils.SystemConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RentInfoServiceImpl implements RentInfoService {

    @Resource
    RentInfoMapper rentInfoMapper;
    @Resource
    HouseService houseService;
    @Resource
    UserService userService;
    @Resource
    CacheService cacheManager;

    @Override
    public boolean rent(RentInfo rentInfo) {
        House house = houseService.getHouseById(rentInfo.getHouseId());
        if (house.getState() != SystemConstants.HouseState.FREE.ordinal()) {
            RentException.cast("你看中的房子已经被人出手了！！！");
        }
        if (house.getOwner().equals(rentInfo.getUserId())) {
            RentException.cast("这是自己发布的房子！！！");
        }

        int i = rentInfoMapper.insert(rentInfo);
        if (house.getTradingMode() == SystemConstants.TradingMode.RENTING.ordinal()) {
            house.setState((byte) SystemConstants.HouseState.RENTED.ordinal());
        }
        if (house.getTradingMode() == SystemConstants.TradingMode.BUYING.ordinal()) {
            house.setState((byte) SystemConstants.HouseState.BOUGHT.ordinal());
        }
        if (i != 0) {
            return houseService.updateHouseById(house, house.getId());
        }
        return false;
    }

    @Override
    public RentInfo getRentById(Integer rentId) {
        String cacheKey = SystemConstants.RENTINFO_CACHE_SUFFIX + rentId;
        RentInfo rentInfo = (RentInfo) cacheManager.get(cacheKey, User.class);
        if (rentInfo == null) {
            rentInfo = rentInfoMapper.selectById(rentId);
            cacheManager.put(cacheKey, rentId);
        }
        return rentInfo;
    }

    @Override
    public List<RentInfoDto> getRentsPage(int pageNum, int pageSize, String usernameLike,
                                          String phoneNumberLike, String houseNameLike) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(StringUtils.hasLength(usernameLike), User::getUsername, usernameLike);
        queryWrapper.likeRight(StringUtils.hasLength(phoneNumberLike), User::getPhoneNumber, phoneNumberLike);
        queryWrapper.select(User::getId, User::getPhoneNumber, User::getUsername);
        List<User> users = userService.getUsers(queryWrapper);
        if (users == null || users.size() == 0) {
            return new ArrayList<>();
        }
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        Set<Integer> userIds = userMap.keySet();

        LambdaQueryWrapper<House> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.likeRight(StringUtils.hasLength(houseNameLike), House::getName, houseNameLike);
        queryWrapper1.select(House::getId, House::getName, House::getUnit, House::getTradingMode);
        List<House> houses = houseService.getHouses(queryWrapper1);
        if (houses == null || houses.size() == 0) {
            return new ArrayList<>();
        }
        Map<Integer, House> houseMap = houses.stream().collect(Collectors.toMap(House::getId, house -> house));
        Set<Integer> houseIds = houseMap.keySet();

        LambdaQueryWrapper<RentInfo> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(RentInfo::getHouseId, houseIds);
        queryWrapper2.in(RentInfo::getUserId, userIds);
        List<RentInfo> rentInfos = rentInfoMapper.selectList(queryWrapper2);
        return rentInfos.stream().map(rentInfo -> {
            Integer houseId = rentInfo.getHouseId();
            Integer userId = rentInfo.getUserId();
            User user = userMap.get(userId);

            House house = houseMap.get(houseId);
            return rentInfo2Dto(rentInfo, user, house, null);
        }).collect(Collectors.toList());
    }

    @Override
    public boolean deleteRentById(Integer rentId) {
        RentInfo rentInfo = getRentById(rentId);

        if (rentInfo.getState() == SystemConstants.RentState.RUNNING.ordinal()) {
            RentException.cast("该条租赁信息正在生效中，不可删除！！！");
        }
        cacheManager.remove(SystemConstants.RENTINFO_CACHE_SUFFIX + rentId);

        Integer houseId = rentInfo.getHouseId();
        LambdaUpdateWrapper<House> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(House::getState, SystemConstants.HouseState.FREE.ordinal());
        updateWrapper.eq(House::getId, houseId);
        houseService.updateHouse(updateWrapper, houseId);

        return rentInfoMapper.deleteById(rentId) != 0;
    }

    @Override
    public List<RentInfoDto> getRentsByUserId(Integer userId) {
        LambdaQueryWrapper<RentInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RentInfo::getUserId, userId);
        List<RentInfo> rentInfos = rentInfoMapper.selectList(queryWrapper);
        return rentInfos.stream()
                .map(rentInfo -> rentInfo2Dto(rentInfo, null, null, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<RentInfoDto> getRentsByHouseId(Integer houseId) {
        LambdaQueryWrapper<RentInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RentInfo::getHouseId, houseId);
        List<RentInfo> rentInfos = rentInfoMapper.selectList(queryWrapper);
        return rentInfos.stream()
                .map(rentInfo -> rentInfo2Dto(rentInfo, null, null, null))
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateRentById(RentInfo rentInfo, Integer rentId) {
        cacheManager.remove(SystemConstants.RENTINFO_CACHE_SUFFIX + rentId);
        rentInfo.setId(rentId);
        if (rentInfo.getState() == SystemConstants.RentState.COMPLETED.ordinal()){
            LambdaUpdateWrapper<House> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(House::getId,rentInfo.getHouseId());
            updateWrapper.set(House::getState,SystemConstants.HouseState.FREE.ordinal());
            houseService.updateHouse(updateWrapper,rentInfo.getHouseId());
        }
        return rentInfoMapper.updateById(rentInfo) != 0;
    }

    public RentInfoDto rentInfo2Dto(RentInfo rentInfo,User user,House house,RentInfoDto rentInfoDto) {
        if (rentInfoDto == null) {
            rentInfoDto = BeanUtil.copyProperties(rentInfo, RentInfoDto.class);
        }else {
            BeanUtil.copyProperties(rentInfo, rentInfoDto);
        }
        Integer houseId = rentInfo.getHouseId();
        Integer userId = rentInfo.getUserId();
        if (user == null) {
            user = userService.getUserById(userId);
        }
        if (house == null) {
            house = houseService.getHouseById(houseId);
        }
        if (user != null){
            rentInfoDto.setPhoneNumber(user.getPhoneNumber());
            rentInfoDto.setUsername(user.getUsername());
        }
        if (house != null) {
            rentInfoDto.setUnit(house.getUnit());
            rentInfoDto.setTradingMode(house.getTradingMode());
            rentInfoDto.setHouseName(house.getName());
        }
        return rentInfoDto;
    }
}
