package com.itheima.service.Impl;


import com.itheima.mapper.ItemMapper;
import com.itheima.pojo.Item;
import com.itheima.service.ItemService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service//采用普通的Service（也就是springframework）,才不会被上一层的controller调用
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrClient solrClient;

    //收到的消息其实就是商品的id值
    @JmsListener(destination = "item-save")
    @Override
    public void addItem(String messgae) {
        try {
            //其实传过来的message就是商品的主键id
            System.out.println("收到的message是 ：" + messgae);

            //根据id查询商品数据
            Long id=Long.parseLong(messgae);
            Item item = itemMapper.selectByPrimaryKey(id);

            System.out.println("查询到的商品是 ： " + item);

            //2.把商品数据添加到索引库

            SolrInputDocument doc=new SolrInputDocument();
            doc.addField("id",item.getId());
            doc.addField("item_title",item.getTitle());
            doc.addField("item_price",item.getPrice());
            doc.addField("item_cid",item.getCid());
            doc.addField("item_image",item.getImage());
            doc.addField("item_status",item.getStatus());

            solrClient.add(doc);
            solrClient.commit();

            System.out.println("更新索引库成功");
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
