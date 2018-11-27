package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Item;
import com.itheima.service.ItemService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
    把mysql的数据导入到solr索引库中
 */
@RestController
public class SolrInitController {

    @Autowired
    private SolrClient solrClient;

    @Reference
    private ItemService itemService;

    @RequestMapping("init")
    public String init() throws Exception {

        int page=1,pageSize=500;
        //什么时候不查了呢？
        do{
            PageInfo<Item> pageInfo = itemService.list(page, pageSize);
            //获取当前这一页的数据
            List<Item> list = pageInfo.getList();

            List<SolrInputDocument> documentList=new ArrayList<>();
            /*
                查询出来之后，把商品的数据添加到solr索引库中
             */
        for (Item item:list){
            //一件商品，就是一个document文件
            SolrInputDocument doc=new SolrInputDocument();

            doc.addField("id",item.getId());
            doc.addField("item_title",item.getTitle());
            doc.addField("item_image",item.getImage());
            doc.addField("item_status",item.getStatus());
            doc.addField("item_cid",item.getCid());
            doc.addField("item_price",item.getPrice());

            //先存储要添加到索引库的文档
            documentList.add(doc);

        }

        //当这一页商品遍历准备好文档集合之后，就要把文档集合给存储到solr里面去了
            solrClient.add(documentList);
            solrClient.commit();



            //查完一页之后，继续查询下一页
            page++;
            pageSize=list.size();//获取当前从数据库取出来一页的数值
        }while (pageSize==500);

        return "success";
    }
}
