package cn.ujn.rent.service;

import cn.ujn.rent.bean.UserInfo;
import java.util.List;

public interface UserInfoService {

    UserInfo getInfoByUserId(Integer userId);

    boolean admitProfile(Integer userId, String profile);

    boolean admitLabels(Integer userId, List<String> labels);

    boolean addLabel(Integer userId, String label);

    boolean deleteLabel(Integer userId, String label);
}
