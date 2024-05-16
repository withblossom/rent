package cn.ujn.rent.controller;

import cn.ujn.rent.bean.User;
import cn.ujn.rent.service.UserInfoService;
import cn.ujn.rent.service.UserService;
import cn.ujn.rent.utils.Result;
import cn.ujn.rent.utils.UserHolder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;
    @Resource
    UserInfoService userInfoService;

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.ok();
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpServletResponse response) {
        String token = userService.login(user, response);
        return Result.ok(token);
    }

    @PostMapping("/profile/{userId}")
    public Result admitProfile(@PathVariable Integer userId, String profile) {
        userInfoService.admitProfile(userId, profile);
        return Result.ok();
    }

    @PostMapping("/labels/{userId}")
    public Result admitLabels(@PathVariable Integer userId, List<String> labels) {
        userInfoService.admitLabels(userId, labels);
        return Result.ok();
    }

    @PostMapping("/label/{userId}")
    public Result addLabel(@PathVariable Integer userId, String label) {
        userInfoService.addLabel(userId, label);
        return Result.ok();
    }

    @DeleteMapping("/label/{userId}")
    public Result deleteLabel(@PathVariable Integer userId, String label) {
        userInfoService.deleteLabel(userId, label);
        return Result.ok();
    }

    @PostMapping("/logout")
    public Result logout() {
        userService.logout();
        return Result.ok();
    }

    @GetMapping("/self")
    public Result self() {
        User user = UserHolder.getUser();
        return Result.ok(user);
    }

    @PutMapping("/{userId}")
    public Result updateUserById(@RequestBody User user, @PathVariable Integer userId) {
        userService.updateUserById(user, userId);
        return Result.ok();
    }

    @GetMapping("/{userId}")
    public Result getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return Result.ok(user);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUserById(@PathVariable Integer userId) {
        userService.deleteUserById(userId);
        return Result.ok();
    }

    @GetMapping("/{pageNum}/{pageSize}")
    public Result getUsersPage(@PathVariable int pageNum, @PathVariable int pageSize,
                               @RequestParam(required = false) String usernameLike,
                               @RequestParam(required = false) String phoneNumberLike) {
        Page<User> userPage = userService.getUsersPage(pageNum, pageSize, usernameLike, phoneNumberLike);
        return Result.ok(userPage.getRecords(), userPage.getTotal());
    }

    @GetMapping("/info/{userId}")
    public Result getUserInfo(@PathVariable Integer userId) {
        return userService.getUserInfo(userId);
    }

    @GetMapping("/select")
    public Result getAllUsersForSelect() {
        List<User> users = userService.getAllUsersForSelect();
        return Result.ok(users, (long) users.size());
    }
}
