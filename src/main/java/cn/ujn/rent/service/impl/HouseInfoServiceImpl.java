package cn.ujn.rent.service.impl;

import cn.ujn.rent.bean.House;
import cn.ujn.rent.bean.HouseInfo;
import cn.ujn.rent.error.RentException;
import cn.ujn.rent.mapper.HouseInfoMapper;
import cn.ujn.rent.service.HouseInfoService;
import cn.ujn.rent.service.HouseService;
import cn.ujn.rent.utils.Checker;
import cn.ujn.rent.utils.UserHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class HouseInfoServiceImpl implements HouseInfoService {

    @Resource
    HouseInfoMapper houseInfoMapper;
    @Resource
    HouseService houseService;

    @Override
    public HouseInfo getInfoByHouseId(Integer houseId) {
        LambdaQueryWrapper<HouseInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HouseInfo::getHouseId, houseId);
        return houseInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public void admitProfile(Integer houseId, String profile) {
        House house = houseService.getHouseById(houseId);
        if (!house.getOwner().equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }

        LambdaQueryWrapper<HouseInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HouseInfo::getHouseId, houseId);
        Long c = houseInfoMapper.selectCount(queryWrapper);
        if (c == 0) {
            HouseInfo houseInfo = new HouseInfo();
            houseInfo.setHouseId(houseId);
            houseInfo.setProfile(profile);
            houseInfoMapper.insert(houseInfo);
        } else {
            LambdaUpdateWrapper<HouseInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(HouseInfo::getHouseId, houseId);
            updateWrapper.set(HouseInfo::getProfile, profile);
            houseInfoMapper.update(null, updateWrapper);
        }
    }

    @Override
    public void admitLabels(Integer houseId, List<String> labels) {
        House house = houseService.getHouseById(houseId);
        if (!house.getOwner().equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }
        if (labels == null || labels.size() == 0) {
            return;
        }
        String ls = StringUtils.collectionToDelimitedString(labels, ",");

        LambdaQueryWrapper<HouseInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HouseInfo::getHouseId, houseId);
        Long c = houseInfoMapper.selectCount(queryWrapper);
        if (c == 0) {
            HouseInfo houseInfo = new HouseInfo();
            houseInfo.setHouseId(houseId);
            houseInfo.setLabels(ls);
            houseInfoMapper.insert(houseInfo);
        } else {
            LambdaUpdateWrapper<HouseInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(HouseInfo::getHouseId, houseId);
            updateWrapper.set(HouseInfo::getLabels, ls);
            houseInfoMapper.update(null, updateWrapper);
        }
    }

    @Override
    public void addLabel(Integer houseId, String label) {
        House house = houseService.getHouseById(houseId);
        if (!house.getOwner().equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }
        if (!StringUtils.hasLength(label)){
            RentException.cast("label没有内容！！！");
        }
        label = label.trim();
        if (label.length() > 5) {
            RentException.cast("标签名太长！！！");
        }
        LambdaQueryWrapper<HouseInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HouseInfo::getHouseId, houseId);
        Long c = houseInfoMapper.selectCount(queryWrapper);
        if (c == 0) {
            HouseInfo houseInfo = new HouseInfo();
            houseInfo.setHouseId(houseId);
            houseInfo.setLabels(label);
            houseInfoMapper.insert(houseInfo);
        } else {
            queryWrapper.select(HouseInfo::getLabels);
            HouseInfo houseInfo = houseInfoMapper.selectOne(queryWrapper);
            String labels = houseInfo.getLabels();
            Set<String> set = StringUtils.commaDelimitedListToSet(labels);
            if (set.size() >= 10) {
                RentException.cast("此标签数已达到上限！！！");
            }
            if (!set.add(label)) {
                RentException.cast("此标签已存在！！！");
            }
            LambdaUpdateWrapper<HouseInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(HouseInfo::getHouseId, houseId);

            String newLabels = StringUtils.collectionToDelimitedString(set, ",");
            updateWrapper.set(HouseInfo::getLabels, newLabels);
            houseInfoMapper.update(null, updateWrapper);
        }
    }

    @Override
    public void deleteLabel(Integer houseId, String label) {
        House house = houseService.getHouseById(houseId);
        if (!house.getOwner().equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }

        if (!StringUtils.hasLength(label)){
            RentException.cast("label没有内容！！！");
        }
        label = label.trim();
        LambdaQueryWrapper<HouseInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HouseInfo::getHouseId, houseId);
        queryWrapper.select(HouseInfo::getLabels);
        HouseInfo houseInfo = houseInfoMapper.selectOne(queryWrapper);

        String labels = houseInfo.getLabels();
        Set<String> set = StringUtils.commaDelimitedListToSet(labels);
        if (!set.remove(label)) {
            RentException.cast("此标签不存在！！！");
        }

        LambdaUpdateWrapper<HouseInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(HouseInfo::getHouseId, houseId);

        String newLabels = StringUtils.collectionToDelimitedString(set, ",");
        updateWrapper.set(HouseInfo::getLabels, newLabels);
        houseInfoMapper.update(null, updateWrapper);
    }
}
