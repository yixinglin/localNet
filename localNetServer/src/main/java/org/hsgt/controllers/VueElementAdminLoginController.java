package org.hsgt.controllers;

import org.hsgt.controllers.response.VueElementAdminResponse;
import org.hsgt.entities.common.User;
import org.hsgt.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.utils.JwtsUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/vue-element-admin/user")
public class VueElementAdminLoginController {

    @Autowired
    UserMapper userMapper;
    @PostMapping("/login")
    public VueElementAdminResponse login(@RequestBody User user) {
        VueElementAdminResponse resp;
        User selectedUser = userMapper.selectByName(user.getUsername());
        if (selectedUser != null && selectedUser.getPassword().equals(user.getPassword())) {
            resp = VueElementAdminResponse.ok();
            String token = JwtsUtils.token(selectedUser);
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            resp.setData(data);
            userMapper.updateTokenById(selectedUser.getId(), token);
        } else {
            resp = new VueElementAdminResponse(VueElementAdminResponse.PW_INCORRECT,
                    "Account or password is incorrect.", null) ;
        }
        return resp;
    }



    @PostMapping("logout")
    public VueElementAdminResponse logout() {
        return VueElementAdminResponse.ok();
    }

    @GetMapping("/info")
    public VueElementAdminResponse userInfo(String token) {
        VueElementAdminResponse resp;
        String username = JwtsUtils.verify(token).getSubject();
        // User userinfo = userMapper.selectByToken(token);
        User userinfo = userMapper.selectByName(username);
        if (userinfo != null) {
            resp = VueElementAdminResponse.ok();
            resp.setData(userinfo);
        } else {
            resp = new VueElementAdminResponse(VueElementAdminResponse.PW_INCORRECT,
                    "Login failed, unable to get user details.", null) ;
        }
        return resp;
    }
}
