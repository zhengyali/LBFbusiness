package cn.lili.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.lili.common.web.Constants;
import cn.lili.core.service.product.UploadService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ABEL on 2018/5/21.
 */
@Controller
public class UploadController {

    @Autowired
    private UploadService uploadService;
    /**上传图片*/
    @RequestMapping(value = "/upload/uploadPic.do")
    public void uploadPic(@RequestParam(required = false) MultipartFile pic
            ,HttpServletResponse response) throws IOException{
        //pic这个名称与 input中name属性一样,要求pic必须有值，否则报错   required=false表示此处不是必须有值
        System.out.println(pic.getOriginalFilename()+"上传开始");
        String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
        System.out.println(pic.getOriginalFilename()+"上传结束");
        System.out.println("图片存储位置"+path);
        String url = Constants.IMG_URL + path;//访问的全路径
        //String url = "http://192.168.200.128/group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg";
        System.out.println("访问路径"+url);
        JSONObject  jo = new JSONObject();
        jo.put("url", url);
     /* 多条情况  jo.put("a",a);
        jo.put("b",b);*/
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jo.toString());
    }
    /**批量上传图片   responseBody是springmvc用于将返回对象转为json串传回去*/
    @RequestMapping(value = "/upload/uploadPics.do")
    public @ResponseBody
    List<String> uploadPics(@RequestParam(required = false) MultipartFile[] pics
            ,HttpServletResponse response) throws IOException{
        //pic这个名称与 input中name属性一样,要求pic必须有值，否则报错   required=false表示此处不是必须有值
        List<String> urls=new ArrayList<String>();
        for (MultipartFile pic:pics){
            System.out.println(pic.getOriginalFilename()+"上传开始");
            String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
            System.out.println(pic.getOriginalFilename()+"上传结束");
            System.out.println("图片存储位置"+path);
            String url = Constants.IMG_URL + path;//访问的全路径
            System.out.println("访问路径"+url);
            urls.add(url);
        }
        return urls;
    }

    /** 富文本图片上传 */
    @RequestMapping(value = "/upload/uploadFck.do")
    public void uploadFck(HttpServletRequest request,HttpServletResponse response) throws IOException{
        //无敌版接收  不知道图片名称的情况，用request接收
        //强转为spring提供的 MultipartRequest ,转后request中只有图片
        MultipartRequest mr=(MultipartRequest)request;
        Map<String, MultipartFile> fileMap = mr.getFileMap();
        Set<Map.Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
        for(Map.Entry<String, MultipartFile> entry : entrySet ){
            MultipartFile pic = entry.getValue();
            System.out.println(pic.getOriginalFilename()+"上传开始");
            String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
            System.out.println(pic.getOriginalFilename()+"上传结束");
            System.out.println("图片存储位置"+path);
            String url = Constants.IMG_URL + path;//访问的全路径
            System.out.println("访问路径"+url);
            JSONObject  jo = new JSONObject();
            jo.put("error",0);//上传状态，0表示上传无错即成功
            jo.put("url", url);//只接收url  源文档中规定的
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jo.toString());
        }

    }
}
