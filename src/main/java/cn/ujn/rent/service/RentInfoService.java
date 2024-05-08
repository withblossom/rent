package cn.ujn.rent.service;

import cn.ujn.rent.bean.RentInfo;
import cn.ujn.rent.bean.dto.RentInfoDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface RentInfoService {
    boolean rent(RentInfo rentInfo);

    RentInfo getRentById(Integer rentId);

    List<RentInfoDto> getRentsPage(int pageNum, int pageSize, String usernameLike, String phoneNumberLike, String houseNameLike);

    boolean deleteRentById(Integer rentId);

    List<RentInfoDto> getRentsByUserId(Integer userId);

    List<RentInfoDto> getRentsByHouseId(Integer houseId);

    boolean updateRentById(RentInfo rentInfo,Integer rentId);
}
