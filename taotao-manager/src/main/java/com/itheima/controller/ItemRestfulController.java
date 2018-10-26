package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Item;
import com.itheima.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    该controller主要演练restful风格的接口设计
 */
@Controller
public class ItemRestfulController {
    @Reference
    private ItemService itemService;

    //查询商品
//    @GetMapping("/item/{id}")
//    public ResponseEntity<Item> getById(@PathVariable(value = "id") long id){
//        try {
//            Item item=itemService.getOneById(id);
//
//            return ResponseEntity.status(HttpStatus.OK).body(item);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }



    //根据商品id查询商品(查一个)
    //localhost:8080/taotao/item?id=3
    /*
        变成
        localhost:8080/taotao/item/{id},默认get请求,只有查询实体类ResponseEntity才有返回
     */
//    @RequestMapping("/item/{id}",method = RequestMethod.GET)
    @GetMapping("/item/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable(value = "id") long id){
        try {
            Item item = itemService.getItemById(id);
            System.out.println("id=" + id);
            return ResponseEntity.status(HttpStatus.OK).body(item);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
    //查询商品所有
//    @GetMapping("/item")
//    public List<Item> getItem(){
//        List<Item> list=itemService.getItem();
//        System.out.println("list=" + list);
//        return list;
//    }
    //查询商品所有
    @GetMapping("/item")
    public ResponseEntity<List<Item>> getItem(){
        try {
            List<Item> list = itemService.getItem();
            System.out.println("list=" + list);
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //新增商品
    @PostMapping("/item")
    public ResponseEntity<Void> addItem(Item item,String desc){
        try {
            int result = itemService.addItem(item, desc);

            System.out.println("新增结果：" + result);

            return ResponseEntity.ok().body(null);

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /*
        url:        localhost:8081/item?id=3
        restful:    localhost:8081/item/{id}
     */
    //按照主键id来删除商品
    @DeleteMapping("/item/{id}")
    public ResponseEntity deleteItemById(@PathVariable long id){
        try {
            int result = itemService.deleteItem(id);
            System.out.println("result=" + result);
            return ResponseEntity.ok(null);
        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //更新商品
    @PutMapping("/item")
    public ResponseEntity<Void> updateItem(Item item){

        try {
            int result = itemService.updateItem(item);
            System.out.println("更新的result=" + result);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
