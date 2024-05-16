package cn.ujn.rent.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ujn.rent.bean.House;
import cn.ujn.rent.bean.HouseComment;
import cn.ujn.rent.bean.User;
import cn.ujn.rent.bean.dto.HouseCommentDto;
import cn.ujn.rent.cache.CacheService;
import cn.ujn.rent.mapper.HouseCommentMapper;
import cn.ujn.rent.service.HouseCommentService;
import cn.ujn.rent.service.HouseService;
import cn.ujn.rent.service.UserService;
import cn.ujn.rent.utils.Checker;
import cn.ujn.rent.utils.SystemConstants;
import cn.ujn.rent.utils.UserHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseCommentServiceImpl implements HouseCommentService {

    @Resource
    HouseCommentMapper houseCommentMapper;
    @Resource
    HouseService houseService;
    @Resource
    UserService userService;
    @Resource
    CacheService cacheManager;

    @Override
    public boolean postComment(HouseComment houseComment) {
        return houseCommentMapper.insert(houseComment) != 0;
    }

    @Override
    public List<HouseCommentDto> getCommentsByHouseId(Integer houseId) {
        LambdaQueryWrapper<HouseComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HouseComment::getHouseId, houseId);
        List<HouseComment> houseComments = houseCommentMapper.selectList(queryWrapper);
        return houseComments
                .stream()
                .map(houseComment -> houseComment2Dto(houseComment, null, null, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<HouseCommentDto> getCommentsByUserId(Integer userId) {
        LambdaQueryWrapper<HouseComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HouseComment::getUserId, userId);
        List<HouseComment> houseComments = houseCommentMapper.selectList(queryWrapper);
        return houseComments
                .stream()
                .map(houseComment -> houseComment2Dto(houseComment, null, null, null))
                .collect(Collectors.toList());
    }

    @Override
    public HouseComment getComment(Integer userId, Integer houseId) {
        LambdaQueryWrapper<HouseComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HouseComment::getUserId, userId);
        queryWrapper.eq(HouseComment::getHouseId, houseId);
        return houseCommentMapper.selectOne(queryWrapper);
    }

    @Override
    public HouseComment getCommentById(Integer commentId) {
        String cacheKey = SystemConstants.COMMENT_CACHE_SUFFIX + commentId;
        HouseComment houseComment = (HouseComment) cacheManager.get(cacheKey, HouseComment.class);
        if (houseComment == null) {
            houseComment = houseCommentMapper.selectById(commentId);
            cacheManager.put(cacheKey, houseComment);
        }
        return houseComment;

    }

    @Override
    public boolean deleteCommentById(Integer commentId) {
        HouseComment houseComment = getCommentById(commentId);
        if (!houseComment.getUserId().equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }

        cacheManager.remove(SystemConstants.COMMENT_CACHE_SUFFIX + commentId);

        return houseCommentMapper.deleteById(commentId) != 0;
    }

    @Override
    public boolean updateCommentById(Integer commentId,String comment) {
        HouseComment houseComment = getCommentById(commentId);
        if (!houseComment.getUserId().equals(UserHolder.getUser().getId())){
            Checker.assertAdmin();
        }

        cacheManager.remove(SystemConstants.COMMENT_CACHE_SUFFIX + commentId);

        LambdaUpdateWrapper<HouseComment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(HouseComment::getId,commentId);
        updateWrapper.set(HouseComment::getComment,comment);
        return houseCommentMapper.update(null, updateWrapper) != 0;
    }

    public HouseCommentDto houseComment2Dto(HouseComment houseComment, House house, User user,HouseCommentDto houseCommentDto){
        if (houseCommentDto == null){
            houseCommentDto = BeanUtil.copyProperties(houseComment, HouseCommentDto.class);
        }else {
            BeanUtil.copyProperties(houseComment, houseCommentDto);
        }

        if (house == null){
            house = houseService.getHouseById(houseComment.getHouseId());
        }
        if (house != null){
            houseCommentDto.setHouseName(house.getName());
        }
        if (user == null){
            user = userService.getUserById(houseComment.getUserId());
        }
        if (user != null){
            houseCommentDto.setUsername(user.getUsername());
        }
        return houseCommentDto;
    }
}
