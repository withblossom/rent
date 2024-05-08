package cn.ujn.rent;

import cn.ujn.rent.mapper.HouseMapper;
import cn.ujn.rent.service.HouseInfoService;
import cn.ujn.rent.service.HouseService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MapperTest {

    @Resource
    HouseService houseService;

    @Test
    void test1(){
        System.out.println(houseService.getAllHousesForSelect());

    }
}
