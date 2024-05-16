package cn.ujn.rent.service.impl;

import cn.ujn.rent.bean.User;
import cn.ujn.rent.bean.UserInfo;
import cn.ujn.rent.error.RentException;
import cn.ujn.rent.mapper.UserInfoMapper;
import cn.ujn.rent.service.UserInfoService;
import cn.ujn.rent.service.UserService;
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
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    UserInfoMapper userInfoMapper;
    @Resource
    UserService userService;

    @Override
    public UserInfo getInfoByUserId(Integer userId) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userId);
        return userInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean admitProfile(Integer userId, String profile) {
        User user = userService.getUserById(userId);
        if (!user.getId().equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userId);
        Long c = userInfoMapper.selectCount(queryWrapper);
        if (c == 0) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setProfile(profile);
            return userInfoMapper.insert(userInfo) != 0;
        } else {
            LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserInfo::getUserId, userId);
            updateWrapper.set(UserInfo::getProfile, profile);
            return userInfoMapper.update(null, updateWrapper) != 0;
        }
    }

    @Override
    public boolean admitLabels(Integer userId, List<String> labels) {
        User user = userService.getUserById(userId);
        if (!user.getId().equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }

        if (labels == null || labels.size() == 0) {
            RentException.cast("无效标签！！！");
        }
        String ls = StringUtils.collectionToDelimitedString(labels, ",");

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userId);
        Long c = userInfoMapper.selectCount(queryWrapper);
        if (c == 0) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setLabels(ls);
            return userInfoMapper.insert(userInfo) != 0;
        } else {
            LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserInfo::getUserId, userId);
            updateWrapper.set(UserInfo::getLabels, ls);
            return userInfoMapper.update(null, updateWrapper) != 0;
        }
    }

    @Override
    public boolean addLabel(Integer userId, String label) {
        User user = userService.getUserById(userId);
        if (!user.getId().equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }

        if (!StringUtils.hasLength(label)) {
            RentException.cast("label没有内容！！！");
        }
        label = label.trim();
        if (label.length() > 5) {
            RentException.cast("标签名太长！！！");
        }
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userId);
        Long c = userInfoMapper.selectCount(queryWrapper);
        if (c == 0) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setLabels(label);
            return userInfoMapper.insert(userInfo) != 0;
        } else {
            queryWrapper.select(UserInfo::getLabels);
            UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
            String labels = userInfo.getLabels();
            Set<String> set = StringUtils.commaDelimitedListToSet(labels);
            if (set.size() >= 10) {
                RentException.cast("此标签数已达到上限！！！");
            }
            if (!set.add(label)) {
                RentException.cast("此标签已存在！！！");
            }

            LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserInfo::getUserId, userId);

            String newLabels = StringUtils.collectionToDelimitedString(set, ",");
            updateWrapper.set(UserInfo::getLabels, newLabels);
            return userInfoMapper.update(null, updateWrapper) != 0;
        }
    }

    @Override
    public boolean deleteLabel(Integer userId, String label) {
        User user = userService.getUserById(userId);
        if (!user.getId().equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }

        if (!StringUtils.hasLength(label)) {
            RentException.cast("label没有内容！！！");
        }
        label = label.trim();
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userId);
        queryWrapper.select(UserInfo::getLabels);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);

        String labels = userInfo.getLabels();
        Set<String> set = StringUtils.commaDelimitedListToSet(labels);
        if (!set.remove(label)) {
            RentException.cast("此标签不存在！！！");
        }

        LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInfo::getUserId, userId);

        String newLabels = StringUtils.collectionToDelimitedString(set, ",");
        updateWrapper.set(UserInfo::getLabels, newLabels);
        return userInfoMapper.update(null, updateWrapper) != 0;
    }
}
