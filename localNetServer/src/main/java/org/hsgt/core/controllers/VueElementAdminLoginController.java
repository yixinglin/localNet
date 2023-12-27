package org.hsgt.core.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.hsgt.core.controllers.response.ControllerResponse;
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
    public ControllerResponse login(@RequestBody User user) {
        ControllerResponse resp;
        User selectedUser = userMapper.selectByName(user.getUsername());
        if (selectedUser != null && selectedUser.getPassword().equals(user.getPassword())) {
            resp = ControllerResponse.ok();
            String token = JwtsUtils.token(selectedUser);
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            resp.setData(data);
            userMapper.updateTokenById(selectedUser.getId(), token);
        } else {
            resp = new ControllerResponse(ControllerResponse.PW_INCORRECT,
                    "Account or password is incorrect.", null) ;
        }
        return resp;
    }



    @PostMapping("logout")
    public ControllerResponse logout() {
        return ControllerResponse.ok();
    }

    @GetMapping("/info")
    public ControllerResponse userInfo(String token) {
        ControllerResponse resp = new ControllerResponse(ControllerResponse.PW_INCORRECT,
                "Login failed, unable to get user details.", token);
        try {
            if (token != null) {
                User userinfo = null;
                String username = JwtsUtils.verify(token).getSubject();
                // User userinfo = userMapper.selectByToken(token);
                userinfo = userMapper.selectByName(username);
                if (userinfo != null) {
                    resp = ControllerResponse.ok();
                    resp.setData(userinfo);
                }
            }
        } catch (MalformedJwtException | IllegalArgumentException | ExpiredJwtException e) {
            logger.error(e.toString());
        }
        return resp;
    }

}
