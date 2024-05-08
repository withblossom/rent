package cn.ujn.rent.controller;

import cn.ujn.rent.bean.HouseComment;
import cn.ujn.rent.bean.dto.HouseCommentDto;
import cn.ujn.rent.service.HouseCommentService;
import cn.ujn.rent.utils.Result;
import cn.ujn.rent.utils.UserHolder;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    HouseCommentService houseCommentService;

    @PostMapping("")
    public Result postComment(@RequestBody HouseComment houseComment) {
        houseCommentService.postComment(houseComment);
        return Result.ok();
    }

    @GetMapping("/house/{houseId}")
    public Result getCommentsByHouseId(@PathVariable Integer houseId) {
        List<HouseCommentDto> houseCommentDtos = houseCommentService.getCommentsByHouseId(houseId);
        return Result.ok(houseCommentDtos, (long) houseCommentDtos.size());
    }

    @GetMapping("/user/{userId}")
    public Result getCommentsByUserId(@PathVariable Integer userId) {
        List<HouseCommentDto> houseCommentDtos = houseCommentService.getCommentsByUserId(userId);
        return Result.ok(houseCommentDtos, (long) houseCommentDtos.size());
    }

    @GetMapping("/{userId}/{houseId}")
    public Result getComment(@PathVariable Integer userId, @PathVariable Integer houseId) {
        HouseComment houseComment = houseCommentService.getComment(userId, houseId);
        return Result.ok(houseComment);
    }

    @GetMapping("/{commentId}")
    public Result getCommentById(@PathVariable Integer commentId) {
        HouseComment houseComment = houseCommentService.getCommentById(commentId);
        return Result.ok(houseComment);
    }

    @DeleteMapping("/{commentId}")
    public Result deleteCommentById(@PathVariable Integer commentId) {
        houseCommentService.deleteCommentById(commentId);
        return Result.ok();
    }

    @PutMapping("/{commentId}")
    public Result updateCommentById(@PathVariable Integer commentId,String comment) {
        houseCommentService.updateCommentById(commentId,comment);
        return Result.ok();
    }
}
