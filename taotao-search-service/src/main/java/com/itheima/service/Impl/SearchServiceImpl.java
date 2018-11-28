package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.pojo.Item;
import com.itheima.pojo.Page;
import com.itheima.service.SearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrClient solrClient;

    //这个方法的数据来自索引库
    @Override
    public Page<Item> searchItem(String q, int page) {

        try {

            int pageSize=16;

            SolrQuery query=new SolrQuery();

            //设置查询的条件
            query.setQuery("item_title:"+q);

            //设置分页
            query.setStart((page-1)*16);//忽略前面的多少条
            query.setRows(16);//每页返回多少条

            //查询到当前页的数据
            QueryResponse response = solrClient.query(query);

            //页面需要：查询的关键字，itemlist,page,totalpage
            //这是文档集合，需要遍历得到每一个文档，然后自己封装成item对象，再使用list集合来存储item对象
            SolrDocumentList results = response.getResults();

            //总记录数
            long total=results.getNumFound();

            //TODO:需要封装当前这一页的数据
            //当前这一页的集合数据
            List<Item> itemList = new ArrayList<>();

            for (SolrDocument document : results) {
                long id = Long.parseLong((String) document.getFieldValue("id")) ;
                String title = (String) document.getFieldValue("item_title");
                String image = (String) document.getFieldValue("item_image");
                long cid = (long) document.getFieldValue("item_cid");
                long price = (long) document.getFieldValue("item_price");

                Item item=new Item();
                item.setId(id);
                item.setTitle(title);
                item.setImage(image);
                item.setCid(cid);
                item.setPrice(price);

                //遍历一次item，就打印一次item
                System.out.println("item   ="+item);

                itemList.add(item);
            }


            //构建page对象：总记录数|当前页码|每页显示数
            Page<Item> pageItem = new Page<>(total,page,pageSize);

            //设置当前页的集合数据
            pageItem.setList(itemList);

            System.out.println("aaa");
            System.out.println(" total  =  " + total);
            System.out.println("bbb");

            //返回
            return pageItem;

        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
