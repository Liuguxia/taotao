package com.itheima.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
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

    //HttpClient的get方式请求，带参数
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

    //HttpClient的post方式请求
    @Test
    public void testPost() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url="http://admin.taotao.com/item";

        HttpPost httpPost=new HttpPost(url);
        //给post请求携带参数
        List<NameValuePair> paramters=new ArrayList<>();
        paramters.add(new BasicNameValuePair("title","iphoneXR"));
        paramters.add(new BasicNameValuePair("desc","有钱可以考虑"));
        paramters.add(new BasicNameValuePair("sellPoint","你自己看看"));
        paramters.add(new BasicNameValuePair("price","19999"));
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
}
