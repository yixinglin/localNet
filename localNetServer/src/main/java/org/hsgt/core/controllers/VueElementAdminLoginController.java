package org.hsgt.core.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.hsgt.core.domain.ResponseCode;
import org.hsgt.core.domain.ResponseResult;
import org.hsgt.core.domain.User;
import org.hsgt.core.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.utils.JwtsUtils;
import org.utils.Logger;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/vue-element-admin/user")
public class VueElementAdminLoginController {

    @Autowired
    UserMapper userMapper;
    Logger logger = Logger.loggerBuilder(VueElementAdminLoginController.class);
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        ResponseResult resp;
        User selectedUser = userMapper.selectByName(user.getUsername());
        if (selectedUser != null && selectedUser.getPassword().equals(user.getPassword())) {
            resp = ResponseResult.success();
            String token = JwtsUtils.token(selectedUser);
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            resp.setData(data);
            userMapper.updateTokenById(selectedUser.getId(), token);
        } else {
            resp = new ResponseResult(ResponseCode.PASSWD_ERROR,
                    "Account or password is incorrect.") ;
        }
        return resp;
    }



    @PostMapping("logout")
    public ResponseResult logout() {
        return ResponseResult.success();
    }

    @GetMapping("/info")
    public ResponseResult userInfo(String token) {
        ResponseResult resp = ResponseResult.error(ResponseCode.PASSWD_ERROR.getCode(),
                "Login failed, unable to get user details.", token);
        try {
            if (token != null) {
                User userinfo = null;
                String username = JwtsUtils.verify(token).getSubject();
                // User userinfo = userMapper.selectByToken(token);
                userinfo = userMapper.selectByName(username);
                if (userinfo != null) {
                    resp = ResponseResult.success();
                    resp.setData(userinfo);
                }
            }
        } catch (MalformedJwtException | IllegalArgumentException | ExpiredJwtException e) {
            logger.error(e.toString());
        }
        return resp;
    }

}
