package com.example.mailserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mailserver.controller.entity.UserRequestBody;
import com.example.mailserver.dao.entity.User;
import com.example.mailserver.exception.UserExistedException;
import com.example.mailserver.service.UserService;
import com.example.mailserver.service.entity.QueryListResponseBody;
import com.example.mailserver.service.entity.UserItem;
import com.example.mailserver.utils.CurrentUser;
import com.example.mailserver.utils.JudgeUserUtil;
import com.example.mailserver.utils.PasswordMD5;
import com.example.mailserver.utils.Result;
import com.example.mailserver.utils.ResultEnum;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/user")
public class UserController {

  @Resource
  UserService userService;


  /**
   * 查询用户列表
   *
   * @param id       用户记录id
   * @param username 用户名
   * @param nickname 用户昵称
   * @param phone    手机号码
   * @param pageSize 页大小
   * @param current  当前页
   * @return QueryListResponseBody<UserItem>
   */
  @GetMapping
  public Result<QueryListResponseBody<UserItem>> queryUserList(
      @RequestParam(value = "id", required = false) String id,
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "nickname", required = false) String nickname,
      @RequestParam(value = "phone", required = false) String phone,
      @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
      @RequestParam(defaultValue = "1", value = "current") int current
  ) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(id != null && !"".equals(id), "id", id)
        .like(username != null && !"".equals(username), "username", username)
        .like(nickname != null && !"".equals(nickname), "nickname", nickname)
        .like(phone != null && !"".equals(phone), "phone", phone);
    try {
      QueryListResponseBody<UserItem> res = userService.queryUserList(current, pageSize,
          queryWrapper);
      return Result.success(res);
    } catch (Exception e) {
      log.error("Exception", e);
      return Result.error(ResultEnum.DEFAULT_ERROR);
    }
  }

  /**
   * 添加用户
   *
   * @param req UserRequestBody.AddUser
   */
  @PostMapping
  public Result<?> addUser(@CurrentUser User user,
      @Valid @RequestBody UserRequestBody.AddUser req) {
    // 非管理员禁止添加新用户
    if (!JudgeUserUtil.isRoot(user.getRole().getValue())) {
      return Result.error(ResultEnum.AUTH_ERROR);
    }
    User newUser = new User();
    BeanUtils.copyProperties(req, newUser);
    newUser.setPassword(PasswordMD5.getPasswordMD5(newUser.getPassword()));
    newUser.setDisabled(0);
    try {
      userService.save(newUser);
      log.info("添加用户 " + user.getUsername() + " 成功！");
      return Result.success();
    } catch (Exception e) {
      log.error("Exception", e);
      return Result.error(ResultEnum.DEFAULT_ERROR);
    }
  }

  @PostMapping("/register")
  public Result<?> register(@RequestBody UserRequestBody.RegisterUser req) {
    try {
      userService.register(req.getUsername(), req.getPassword());
      log.info("用户" + req.getUsername() + " 注册成功！");
      return Result.success();
    } catch (UserExistedException e) {
      log.error("UserExistedException", e);
      return Result.error(ResultEnum.USER_EXISTED_ERROR);
    } catch (Exception e) {
      log.error("Exception", e);
      return Result.error(ResultEnum.DEFAULT_ERROR);
    }
  }

  /**
   * 修改用户信息
   */
  @PutMapping
  public Result<?> editUser(@CurrentUser User user,
      @Valid @RequestBody UserRequestBody.EditUser req) {
    User editUser = new User();
    BeanUtils.copyProperties(req, editUser);
    try {
      // 非管理员禁止修改别人的信息
      if (!user.getId().equals(editUser.getId()) && JudgeUserUtil.isNormal(
          user.getRole().getValue())) {
        return Result.error(ResultEnum.AUTH_ERROR);
      }
      userService.updateById(editUser);
      log.info("用户 " + user.getUsername() + " 修改资料成功！");
      return Result.success();
    } catch (Exception e) {
      log.error("Exception", e);
      return Result.error(ResultEnum.DEFAULT_ERROR);
    }
  }

  @DeleteMapping
  public Result<?> deleteUserById(@CurrentUser User user,
      @RequestParam("id") @NotEmpty Integer id) {
    if (!JudgeUserUtil.isRoot(user.getRole().getValue())) {
      return Result.error(ResultEnum.AUTH_ERROR);
    }
    try {
      userService.removeById(id);
      log.info("用户 " + user.getUsername() + " 删除成功！");
      return Result.success();
    } catch (Exception e) {
      log.error("Exception", e);
      return Result.error(ResultEnum.DEFAULT_ERROR);
    }
  }

  /**
   * 修改用户密码
   *
   * @param user 当前用户
   * @param req  serRequestBody.ChangePassword
   * @return null
   */
  @PutMapping("/change_password")
  public Result<?> changePassword(@CurrentUser User user,
      @Valid @RequestBody UserRequestBody.ChangePassword req) {
    if (!PasswordMD5.getPasswordMD5(req.getOldPassword()).equals(user.getPassword())) {
      return Result.error(ResultEnum.PASSWORD_ERROR);
    }
    try {
      user.setPassword(PasswordMD5.getPasswordMD5(req.getNewPassword()));
      boolean success = userService.updateById(user);
      if (success) {
        log.error("用户 " + user.getUsername() + " 修改密码成功！");
        return Result.success(null);
      } else {
        log.error("用户 " + user.getUsername() + " 修改密码失败！");
        return Result.error(ResultEnum.DEFAULT_ERROR);
      }
    } catch (Exception e) {
      log.error("Exception", e);
      return Result.error(ResultEnum.DEFAULT_ERROR);
    }
  }
}
