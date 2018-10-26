package com.itheima;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.cglib.core.EmitUtils;

import java.io.File;
import java.io.IOException;

public class DoGet {

    public static void main(String[] args) throws IOException {

        HttpClient httpClient=HttpClients.createDefault();

        HttpGet httpGet=new HttpGet("http://www.baidu.com");

        HttpResponse response=null;

        response = httpClient.execute(httpGet);

        if(response.getStatusLine().getStatusCode()==200){
            String content = EntityUtils.toString(response.getEntity(), "utf-8");

            System.out.println("content" + content);

            FileUtils.writeStringToFile(new File("C:\\Users\\pc\\Desktop\\Out\\a.txt"),content);
        }
    }

}
