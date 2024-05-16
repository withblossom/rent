package cn.ujn.rent.controller;

import cn.ujn.rent.bean.DetailedInfo;
import cn.ujn.rent.bean.House;
import cn.ujn.rent.bean.dto.HouseDto;
import cn.ujn.rent.service.HouseInfoService;
import cn.ujn.rent.service.HouseService;
import cn.ujn.rent.utils.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Resource
    HouseService houseService;
    @Resource
    HouseInfoService houseInfoService;

    @PostMapping("")
    public Result addHouse(@RequestBody House house) {
        houseService.addHouse(house);
        return Result.ok();
    }

    @PostMapping("/profile/{houseId}")
    public Result admitProfile(@PathVariable Integer houseId, String profile) {
        houseInfoService.admitProfile(houseId, profile);
        return Result.ok();
    }

    @PostMapping("/labels/{houseId}")
    public Result admitLabels(@PathVariable Integer houseId, List<String> labels) {
        houseInfoService.admitLabels(houseId, labels);
        return Result.ok();
    }

    @PostMapping("/label/{houseId}")
    public Result addLabel(@PathVariable Integer houseId, String label) {
        houseInfoService.addLabel(houseId, label);
        return Result.ok();
    }

    @DeleteMapping("/label/{houseId}")
    public Result deleteLabel(@PathVariable Integer houseId, String label) {
        houseInfoService.deleteLabel(houseId, label);
        return Result.ok();
    }

    @PutMapping("/{houseId}")
    public Result updateHouseById(@RequestBody House house, @PathVariable Integer houseId) {
        houseService.updateHouseById(house, houseId);
        return Result.ok();
    }

    @GetMapping("/{houseId}")
    public Result getHouseById(@PathVariable Integer houseId) {
        House house = houseService.getHouseById(houseId);
        return Result.ok(house);
    }

    @DeleteMapping("/{houseId}")
    public Result deleteHouseById(@PathVariable Integer houseId) {
        houseService.deleteHouseById(houseId);
        return Result.ok();
    }

    @GetMapping("/{pageNum}/{pageSize}")
    public Result getHousesPage(@PathVariable int pageNum, @PathVariable int pageSize,
                                @RequestParam(required = false) String housenameLike,
                                @RequestParam(required = false) Byte houseState) {
        List<HouseDto> houseDtos = houseService.getHousesPage(pageNum, pageSize, housenameLike, houseState);
        return Result.ok(houseDtos, (long) houseDtos.size());
    }

    @GetMapping("/info/{houseId}")
    public Result getHouseInfo(@PathVariable Integer houseId) {
        DetailedInfo<HouseDto> houseInfo = houseService.getHouseInfo(houseId);
        return Result.ok(houseInfo);
    }

    @GetMapping("/select")
    public Result getAllHousesForSelect() {
        List<House> houses = houseService.getAllHousesForSelect();
        return Result.ok(houses, (long) houses.size());
    }
}
