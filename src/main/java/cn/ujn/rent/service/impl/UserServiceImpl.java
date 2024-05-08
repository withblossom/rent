package cn.ujn.rent.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ujn.rent.bean.User;
import cn.ujn.rent.bean.DetailedInfo;
import cn.ujn.rent.bean.UserInfo;
import cn.ujn.rent.cache.CacheManager;
import cn.ujn.rent.cache.CacheService;
import cn.ujn.rent.cache.caffeine.CaffeineCacheService;
import cn.ujn.rent.cache.redis.RedisCacheService;
import cn.ujn.rent.error.RentException;
import cn.ujn.rent.mapper.UserMapper;
import cn.ujn.rent.service.UserInfoService;
import cn.ujn.rent.service.UserService;
import cn.ujn.rent.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;
    @Resource
    UserInfoService userInfoService;
    @Resource
    CacheService cacheManager;
    @Resource
    RedisCacheService redisCacheService;

    @Override
    public boolean register(User user) {
        Checker.assertAdmin();
        //check phonenum format
        if (Checker.isPhoneInvalid(user.getPhoneNumber())) {
            RentException.cast("手机号格式不正确！！！");
        }
        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(User::getUsername, user.getUsername());
        Long count = userMapper.selectCount(queryWrapper1);
        if (count != 0) {
            RentException.cast("用户名已存在！！！");
        }
        LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(User::getPhoneNumber, user.getPhoneNumber());
        queryWrapper2.eq(User::getPassword, user.getPassword());
        Long count1 = userMapper.selectCount(queryWrapper2);
        if (count1 != 0) {
            RentException.cast("账户已存在！！！");
        }

        return userMapper.insert(user) != 0;
    }

    @Override
    public String login(User user, HttpServletResponse response) {
        if (Checker.isPhoneInvalid(user.getPhoneNumber())) {
            RentException.cast("手机号格式不正确！！！");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhoneNumber, user.getPhoneNumber());
        queryWrapper.eq(User::getPassword, user.getPassword());
        User userSelected = userMapper.selectOne(queryWrapper);
        if (userSelected == null) {
            RentException.cast("手机号或者密码错误！！！");
        }
        String token = TokenUtil.encrypt2Token(userSelected);
        redisCacheService.putTtl(SystemConstants.TOKEN_USER_SUFFIX + token,userSelected,30, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public boolean logout() {
        return true;
    }

    @Override
    public Result getUserInfo(Integer userId) {
        DetailedInfo<User> detailedInfo = new DetailedInfo<>();

        User user = getUserById(userId);
        detailedInfo.setObj(user);
        UserInfo info = userInfoService.getInfoByUserId(userId);

        if (info != null) {
            BeanUtil.copyProperties(info, detailedInfo, "id", "labels");
            Set<String> labels = StringUtils.commaDelimitedListToSet(info.getLabels());
            detailedInfo.setLabels(labels);
        }
        return Result.ok(detailedInfo);
    }

    @Override
    public Page<User> getUsersPage(int pageNum, int pageSize, String usernameLike, String phoneNumberLike) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(StringUtils.hasLength(usernameLike), User::getUsername, usernameLike);
        queryWrapper.likeRight(StringUtils.hasLength(phoneNumberLike), User::getPhoneNumber, phoneNumberLike);

        Page<User> userPage = new Page<>(pageNum, pageSize);
        userMapper.selectPage(userPage, queryWrapper);
        return userPage;
    }

    @Override
    public List<User> getAllUsersForSelect() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getId, User::getUsername);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateUserById(User user, Integer userId) {
        if (!userId.equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }

        if (Checker.isPhoneInvalid(user.getPhoneNumber())) {
            RentException.cast("手机号格式不正确！！！");
        }

        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.ne(User::getId, userId);
        queryWrapper1.eq(User::getUsername, user.getUsername());
        Long count = userMapper.selectCount(queryWrapper1);
        if (count != 0) {
            RentException.cast("用户名已存在！！！");
        }
        LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.ne(User::getId, userId);
        queryWrapper2.eq(User::getPhoneNumber, user.getPhoneNumber());
        queryWrapper2.eq(User::getPassword, user.getPassword());

        Long count1 = userMapper.selectCount(queryWrapper2);
        if (count1 != 0) {
            RentException.cast("账户已存在！！！");
        }

        cacheManager.remove(SystemConstants.USER_CACHE_SUFFIX + userId);
        user.setId(userId);
        return userMapper.updateById(user) != 0;
    }

    @Override
    public boolean deleteUserById(Integer userId) {
        if (!userId.equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }
        if (UserHolder.getUser().getId().equals(userId)) {
            RentException.cast("不能删除自己！！！");
        }
        cacheManager.remove(SystemConstants.USER_CACHE_SUFFIX + userId);
        return userMapper.deleteById(userId) != 0;
    }

    @Override
    public User getUserById(Integer userId) {
        String cacheKey = SystemConstants.USER_CACHE_SUFFIX + userId;
        User user = (User) cacheManager.get(cacheKey, User.class);
        if (user == null) {
            user = userMapper.selectById(userId);
            cacheManager.put(cacheKey, user);
        }
        return user;
    }

    @Override
    public List<User> getUsers(LambdaQueryWrapper<User> queryWrapper) {
        return userMapper.selectList(queryWrapper);
    }

}
