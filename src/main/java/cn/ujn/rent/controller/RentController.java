package cn.ujn.rent.controller;

import cn.ujn.rent.bean.RentInfo;
import cn.ujn.rent.bean.dto.RentInfoDto;
import cn.ujn.rent.service.RentInfoService;
import cn.ujn.rent.utils.Checker;
import cn.ujn.rent.utils.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/rent")
public class RentController {

    @Resource
    RentInfoService rentInfoService;

    @PostMapping("")
    public Result rent(@RequestBody RentInfo rentInfo) {
        rentInfoService.rent(rentInfo);
        return Result.ok();
    }

    @GetMapping("/{rentId}")
    public Result getRentById(@PathVariable Integer rentId) {
        RentInfo rentInfo = rentInfoService.getRentById(rentId);
        return Result.ok(rentInfo);
    }

    @PutMapping("/{rentId}")
    public Result updateRentById(@RequestBody RentInfo rentInfo, @PathVariable Integer rentId) {
        Checker.assertAdmin();
        rentInfoService.updateRentById(rentInfo, rentId);
        return Result.ok(rentInfo);
    }

    @DeleteMapping("/{rentId}")
    public Result deleteRentById(@PathVariable Integer rentId) {
        Checker.assertAdmin();
        rentInfoService.deleteRentById(rentId);
        return Result.ok();
    }

    @GetMapping("/house/{houseId}")
    public Result getRentsByHouseId(@PathVariable Integer houseId) {
        List<RentInfoDto> rentInfoDtos = rentInfoService.getRentsByHouseId(houseId);
        return Result.ok(rentInfoDtos, (long) rentInfoDtos.size());
    }

    @GetMapping("/user/{userId}")
    public Result getRentsByUserId(@PathVariable Integer userId) {
        List<RentInfoDto> rentInfoDtos = rentInfoService.getRentsByUserId(userId);
        return Result.ok(rentInfoDtos, (long) rentInfoDtos.size());
    }

    @GetMapping("/{pageNum}/{pageSize}")
    public Result getUsersPage(@PathVariable int pageNum, @PathVariable int pageSize,
                               @RequestParam(required = false) String usernameLike,
                               @RequestParam(required = false) String phoneNumberLike,
                               @RequestParam(required = false) String houseNameLike) {
        List<RentInfoDto> rentInfoDtos = rentInfoService.getRentsPage(pageNum, pageSize, usernameLike, phoneNumberLike, houseNameLike);
        return Result.ok(rentInfoDtos, (long) rentInfoDtos.size());
    }
}
