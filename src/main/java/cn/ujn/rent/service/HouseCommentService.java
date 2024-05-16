package cn.ujn.rent.service;

import cn.ujn.rent.bean.HouseComment;
import cn.ujn.rent.bean.dto.HouseCommentDto;
import java.util.List;

public interface HouseCommentService {
    boolean postComment(HouseComment houseComment);

    List<HouseCommentDto> getCommentsByHouseId(Integer houseId);

    List<HouseCommentDto> getCommentsByUserId(Integer userId);

    HouseComment getComment(Integer userId, Integer houseId);

    HouseComment getCommentById(Integer commentId);

    boolean deleteCommentById(Integer commentId);

    boolean updateCommentById(Integer commentId,String comment);
}
