package com.itheima.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TestHttpClient {
    @Test
    public void testDemo() throws IOException {
        //创建一个http的客户端(打开浏览器)，创建默认的客户端
        CloseableHttpClient httpClient=HttpClients.createDefault();

        //构建get请求（相当于在浏览器输入）
        HttpGet httpGet=new HttpGet("http://www.baidu.com");
        //发起请求
        HttpResponse response = httpClient.execute(httpGet);

        //拿到响应后的状态码
        int code = response.getStatusLine().getStatusCode();
        //判断状态码
        if (200== code){
            //获取响应实体
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity,"utf-8");
            System.out.println("content=  " + content);
        }

        //关闭客户端
        httpClient.close();
    }


    @Test
    public void testGet() throws IOException {
        //创建浏览器客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //带参数的get请求
        String url="http://admin.taotao.com/item/536563";
        //创建get请求
        HttpGet httpGet = new HttpGet(url);
        //在和客户端输入uri地址
        HttpResponse response = httpClient.execute(httpGet);
        //返回状态码
        int code = response.getStatusLine().getStatusCode();
        //判断状态码
        if (200==code){
            //获取响应实体
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            System.out.println("content=" + content);
        }
        httpClient.close();
    }

/*
    这个方法仅仅针对以前的那种api接口，如果是restful风格的接口，参数是直接在地址上面拼的
 */
    //HttpClient的get方式请求，带参数
    @Test
    public void testGetParam() throws Exception {
        //创建浏览器客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //带参数的get请求
        //String url="http://admin.taotao.com/item/536563";
        //get请求
        //String url="http://admin.taotao.com/item";
        String url="http://admin.taotao.com/rest/content/category";

        //对地址进行再一次封装,以便携带数据
        URIBuilder uriBuilder=new URIBuilder(url);
        uriBuilder.addParameter("id","0");

        URI uri = uriBuilder.build();

        System.out.println("uri====" + uri.toString());

        //创建get请求
        HttpGet httpGet = new HttpGet(uri);

        //在和客户端输入uri地址
        HttpResponse response = httpClient.execute(httpGet);
        //返回状态码
        int code = response.getStatusLine().getStatusCode();
        //判断状态码
        if (200==code){
            //获取响应实体
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            System.out.println("content=" + content);
        }
        httpClient.close();
    }

    //HttpClient的post方式请求
    @Test
    public void testPost() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url="http://admin.taotao.com/item";

        HttpPost httpPost=new HttpPost(url);
        //给post请求携带参数
        List<NameValuePair> paramters=new ArrayList<>();
        paramters.add(new BasicNameValuePair("title","联想k5pro"));
        paramters.add(new BasicNameValuePair("desc","电量大"));
        paramters.add(new BasicNameValuePair("sellPoint","不贵，实用"));
        paramters.add(new BasicNameValuePair("price","1298"));
        paramters.add(new BasicNameValuePair("num","133"));
        paramters.add(new BasicNameValuePair("cid","76"));



        HttpEntity entity=new UrlEncodedFormEntity(paramters,"utf-8");
        httpPost.setEntity(entity);




        HttpResponse response = httpClient.execute(httpPost);

        //返回状态码
        int code = response.getStatusLine().getStatusCode();

        if (200==code){
            //获取响应实体
            //HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println("!! !!! !!!   content=" + content);
        }
        httpClient.close();
    }


    //HttpClient的delete方式请求
    @Test
    public void testDelete() throws IOException{

        //创建客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String url="http://admin.taotao.com/item/1540917000666";

        //创建delete请求
        HttpDelete httpDelete = new HttpDelete(url);

        //在浏览器输入请求地址
        HttpResponse response = httpClient.execute(httpDelete);

        //响应response有一个状态码code和响应体entity

        int code = response.getStatusLine().getStatusCode();

        if (200==code){
            //获取响应实体
            HttpEntity entity=response.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            System.out.println("content==" + content);
        }

        httpClient.close();
    }

    //HttpClient的put方式请求
    @Test
    public void testPut() throws IOException{
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String url="http://admin.taotao.com/item";

        HttpPut httpPut = new HttpPut(url);

        /*
            put请求带数据
         */
        //封装传递的数据
        List<NameValuePair> list=new ArrayList<>();
        list.add(new BasicNameValuePair("id","1540917002811"));
        list.add(new BasicNameValuePair("title","苹果XR"));


        //对数据进url编码，使用utf-8字符集
        HttpEntity entity=new UrlEncodedFormEntity(list,"utf-8");
        //给这次请求设置携带的数据
        httpPut.setEntity(entity);

        //执行put请求并返回一个response响应
        HttpResponse response = httpClient.execute(httpPut);
        /*
            response：响应码和响应体
         */
        int code = response.getStatusLine().getStatusCode();

        if (200==code){
            entity = response.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            System.out.println("-------put更新成功");
            System.out.println("content==" + content);
        }
        httpClient.close();
    }
}
