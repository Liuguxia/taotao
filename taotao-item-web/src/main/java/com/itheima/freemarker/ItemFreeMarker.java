package com.itheima.freemarker;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Item;
import com.itheima.pojo.ItemDesc;
import com.itheima.service.ItemDescService;
import com.itheima.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Component
public class ItemFreeMarker {

    @Reference//远程引用，也就是调用其他模块
    private ItemService itemService;
    @Reference
    private ItemDescService itemDescService;

    @JmsListener(destination = "item-save")
    public void addItem(String message) throws Exception {
        System.out.println("商品详情模块，收到的message=" + message);


        //1.根据id（也就是message）查询商品
        Item item = itemService.getItemById(Long.parseLong(message));
        //查询商品详情信息
        ItemDesc itemDesc = itemDescService.getDescById(Long.parseLong(message));

        /*
            2.生成对应的html，封装数据
         */
        //一：创建核心对象
        Configuration configuration=new Configuration(Configuration.VERSION_2_3_25);
        //二：设置模板目录在哪
        String path=System.getProperty("user.dir")+"/src/main/webapp/ftl";
        System.out.println("path     ："+path);
        configuration.setDirectoryForTemplateLoading(new File(path));

        //三：获取模板，参数名为模板名
        Template template = configuration.getTemplate("item.ftl");

        //四：构建动态数据
        Map<String,Object> root=new HashMap<>();

        root.put("item",item);
        root.put("itemDesc",itemDesc);



        //五：指定html网页输出到哪个位置去
        Writer out=new FileWriter("E:\\taotao\\"+message+".html");

        //六：生成网页，参数一：动态数据，参数二，输出对象
        template.process(root,out);  //process,加工，处理

        out.close();


    }

    //测试路径的代码
    public static void main(String[] args) {
        //E:\IntelliJ IDEA 2018.1.5\Workspace\taotao-parent\taotao-item-web\src\main\webapp\ftl
        //E:\IntelliJ IDEA 2018.1.5\Workspace\taotao-parent
        //相对：ftl
        String path=System.getProperty("user.dir");
        System.out.println(path);
    }
}
