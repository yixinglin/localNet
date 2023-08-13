package org.hsgt.controllers;

import org.hsgt.controllers.response.VueElementAdminResponse;
import org.hsgt.entities.common.User;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.utils.IoUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/vue-element-admin/user")
public class VueElementAdminLoginController {

    String token;

    @PostMapping("/login")
    public VueElementAdminResponse login(@RequestBody User user) {
        VueElementAdminResponse resp;
        if (user.getUsername().equals("admin1")) {
            resp = VueElementAdminResponse.ok();
            token = user.getUsername() + "-token";
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            resp.setData(data);
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
        if (this.token.equals(token)) {
            resp = VueElementAdminResponse.ok();
            String s = IoUtils.readFile("src/main/resources/hsgt/login/user_mock.json");
            JSONObject info = new JSONObject(s);
            JSONObject user = info.getJSONObject("users").getJSONObject(this.token);
            resp.setData(user.toMap());
        } else {
            resp = new VueElementAdminResponse(VueElementAdminResponse.PW_INCORRECT,
                    "Login failed, unable to get user details.", null) ;
        }
        return resp;
    }
}
