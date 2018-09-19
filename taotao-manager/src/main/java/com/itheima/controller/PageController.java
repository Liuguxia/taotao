package com.itheima.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller     //注意不能是RestController，因为我们要跳页面
public class PageController {

    @RequestMapping("index")
    public String index(){
        System.out.println("要显示首页了");

        /*
            如果使用SpringMVC，需要指定资源路径所在

            如果使用springboot，那么默认跳转的位置在
                                /resources/static或public文件夹或xxx文件或xxx文件或templates

            现在资源在WEB-INF/view/  ，故要在application.properties下指定资源路径

                spring.mvc.view.prefix=/WEB-INF/view/

                spring.mvc.view.suffix=.jsp

            最终的跳转：/WEB-INF/view/index.jsp
         */
        return "index";
    }


    @RequestMapping("/rest/page/item-add")
    public String ItemAdd() {
        System.out.println("item-add.jsp  显示了");
        return "item-add";
    }

    @RequestMapping("/rest/page/item-list")
    public String ItemList() {
        System.out.println("item-list.jsp  显示了");
        return "item-list";
    }

    @RequestMapping("/rest/page/content-category")
    public String ItemCategory() {
        System.out.println("content-category.jsp  显示了");
        return "content-category";
    }

    @RequestMapping("/rest/page/content")
    public String ItemContent() {
        System.out.println("content.jsp  显示了");
        return "content";
    }

}
