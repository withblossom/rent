package cn.ujn.rent.service;

import cn.ujn.rent.bean.User;
import cn.ujn.rent.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    boolean register(User user);

    String login(User user, HttpServletResponse response);

    boolean logout();

    Result getUserInfo(Integer userId);

    Page<User> getUsersPage(int pageNum, int pageSize,String usernameLike, String phoneNumberLike);

    List<User> getAllUsersForSelect();

    boolean updateUserById(User user, Integer userId);

    boolean deleteUserById(Integer userId);

    User getUserById(Integer userId);

    List<User> getUsers(LambdaQueryWrapper<User> queryWrapper);
}
