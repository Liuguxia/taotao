package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.util.UploadUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/*
    负责文件上传的控制器
 */
@Controller
public class UploadController {

    /***
     * 用于处理JSON转换问题
     */
    @Autowired
    private ObjectMapper objectMapper;
//    //TrackerServer文件存放地址   Spring注入
//    @Value("${TrackerServer}")
//    private String TrackerServer;
//
//
//    //FastDFS图片访问地址
//    @Value("${FastDFSWebLink}")
//    private String FastDFSWebLink;

    /***
     * 基于SpringMVC文件上传
     * @param file
     * @param session
     * @return
     * @throws Exception
     *
     * @ResponseBody
     * Map<String, Object>
     * 这样子响应的数据类型为Json数据类型，而富文本编辑器解析对Json不兼容，兼容文本类型，所以需要将响应数据转成字符串
     *
     */
    @ResponseBody
    @RequestMapping(value="/rest/pic/upload",method=RequestMethod.POST)
    public String upload(@RequestParam(value="uploadFile")MultipartFile file, HttpSession session) throws Exception{
        //获取文件后缀
        String subfix = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");

        //String TrackerServer="tracker_server=192.168.227.131:22122";
        //获取resources的地址路径
        String path = System.getProperty("user.dir")+"/src/main/resources/";

        //执行文件上传,参数一写的是：配置文件的地址路径，配置文件的内容是：tracker_server=192.168.227.131:22122
        String[] uploadinfos = UploadUtil.upload(path+"tracker.conf", file.getBytes(), subfix);

        for (String string : uploadinfos) {
            System.out.println(string);
        }


        /****
         * error   	0标识成功，1失败
         * url		成功后文件访问地址
         * height	高度
         * width	宽度
         */
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("error", 0);//group1    /M00/00/00/wKjjg1u8wMiAAQArAAFNz-hpDa8360.jpg
        map.put("url", "http://192.168.227.131/"+uploadinfos[0]+"/"+uploadinfos[1]);//拼图片的地址路径
        //map.put("url", "http://image.taotao.com/"+uploadinfos[0]+"/"+uploadinfos[1]);//拼图片的地址路径
        map.put("height", 100);
        map.put("width", 100);

        //将Map对象转成JSON字符串
        String json = objectMapper.writeValueAsString(map);

        return json;
    }



}

