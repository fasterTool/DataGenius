package cn.ly.archive.study.test.controller;

import cn.ly.archive.study.test.domain.User;
import cn.ly.archive.study.test.domain.req.UserAddReq;
import cn.ly.archive.study.test.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private IUserService userService;

    @Test
    public void add() {
        UserAddReq req = new UserAddReq();
        req.setName("admin");
        req.setPassword("123456");
        req.setAccount("admin");


        User user = new User();
        BeanUtils.copyProperties(req, user);
        user.setId(System.currentTimeMillis());
        user.setAddUser(12315531321L);
        user.setLastModifyUser(12315531321L);
        user.setAddTime(new Timestamp(System.currentTimeMillis()));
        user.setLastModifyTime(user.getAddTime());
        userService.add(user);
    }


}