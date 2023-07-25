package org.lin.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/say")
    String sayHello() {
        return "Hello World!";
    }

    @GetMapping("/say-cn")
    String sayHelloCN() {
        return "你好世界!";
    }
}
