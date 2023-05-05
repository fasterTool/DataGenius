package cn.ly.archive.study.test.controller;

import cn.ly.archive.study.test.domain.User;
import cn.ly.archive.study.test.domain.req.UserAddReq;
import cn.ly.archive.study.test.domain.req.UserListReq;
import cn.ly.archive.study.test.domain.vo.UserVo;
import cn.ly.archive.study.test.service.IUserService;
import cn.ly.common.web.domain.AjaxResult;
import cn.ly.common.web.domain.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-04-30
 */

@Api("用户管理")
@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=utf-8")
public class UserController {

    @Autowired
    private IUserService userService;

    @Operation(summary = "新增")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public AjaxResult add(@RequestBody @Valid UserAddReq req) {
        User user = new User();
        BeanUtils.copyProperties(req, user);
        user.setAddUser(12315531321L);
        user.setLastModifyUser(12315531321L);
        user.setAddTime(new Timestamp(System.currentTimeMillis()));
        user.setLastModifyTime(user.getAddTime());
        userService.addWithUid(user);
        return AjaxResult.success();
    }

    @Operation(summary = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo<UserVo> list(@ModelAttribute UserListReq req) {
        return userService.searchPageInfo(req);
    }


}
