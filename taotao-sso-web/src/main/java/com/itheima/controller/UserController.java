package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    @Reference
    private UserService userService;

    @GetMapping("/user/check/{param}/{type}")
    public ResponseEntity<String> check(@PathVariable String param,@PathVariable int type,String callback){
        try {
            System.out.println("要检测的用户名是否存在" + param + ":" + type);
            Boolean exist = userService.check(param, type);

            String result="";

            if (!StringUtils.isEmpty(callback)){
                result=callback+"("+exist+")";
            }else {
                result=exist+"";
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

//    @GetMapping("/user/check/{param}/{type}")
//    public ResponseEntity<Boolean> check(@PathVariable String param,@PathVariable int type){
//        try {
//            System.out.println("要检测的用户名是否存在" + param + ":" + type);
//            Boolean exist = userService.check(param, type);
//            return ResponseEntity.ok(exist);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(true);
//    }

    /*
       实现根据ticket查询用户http://sso.taotao.com/user/{ticket}
       示例：http://sso.taotao.com/user/fe5cb546aeb3ce1bf37abcb08a40493e
       其中，ticket：fe5cb546aeb3ce1bf37abcb08a40493e
     */
    @GetMapping("/user/{ticket}")
    public ResponseEntity<String> selectUser(@PathVariable String ticket){
        try {
            String result = userService.selectUser(ticket);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户未登录");
    }

}
