package com.itheima;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

public class Get {
    @Test
    public void testGet(){
        try {
            CloseableHttpClient httpClient=HttpClients.createDefault();
            String uri="http://www.baidu.com";
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse response=httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode()==200){
                String content = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println("content内容" + content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGet1(){
        //创建一个客户端
        CloseableHttpClient httpClient=HttpClients.createDefault();
        //uri
        String uri="http://www.taobao.com";
        //选择get方式
        HttpGet httpGet=new HttpGet(uri);

        try {
            //执行动作，相当于回车
            HttpResponse response = httpClient.execute(httpGet);
            //判断状态码
            if (response.getStatusLine().getStatusCode()==200){
                //对响应拿内容
                String s = EntityUtils.toString(response.getEntity(), "uf-8");
                System.out.println("get的方式响应的内容" + s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
