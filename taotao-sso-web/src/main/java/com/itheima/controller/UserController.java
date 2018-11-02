package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    @Reference
    private UserService userService;

    @GetMapping("/user/check/{param}/{type}")
    public ResponseEntity<Boolean> check(@PathVariable String param,@PathVariable int type){
        try {
            Boolean exist = userService.check(param, type);
            return ResponseEntity.ok(exist);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(true);
    }
}
