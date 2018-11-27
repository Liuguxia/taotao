package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.pojo.Item;
import com.itheima.pojo.Page;
import com.itheima.service.SearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrClient solrClient;

    //这个方法的数据来自索引库
    @Override
    public Page<Item> searchItem(String q, int page) {

        try {
            SolrQuery query=new SolrQuery();

            //设置查询的条件
            query.setQuery("item_title:"+q);

            //设置分页
            query.setStart((page-1)*16);//忽略前面的多少条
            query.setRows(16);//每页返回多少条

            //查询到当前页的数据
            QueryResponse response = solrClient.query(query);

            //页面需要：查询的关键字，itemlist,page,totalpage
            SolrDocumentList results = response.getResults();

            //总记录数
            long total=results.getNumFound();
            System.out.println("aaa");
            System.out.println(" total  =  " + total);
            System.out.println("bbb");


        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
