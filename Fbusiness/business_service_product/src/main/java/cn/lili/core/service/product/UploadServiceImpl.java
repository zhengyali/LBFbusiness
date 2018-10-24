package cn.lili.core.service.product;
import cn.lili.common.fdfs.FastDFSUtils;
import org.springframework.stereotype.Service;

/**
 * Created by ABEL on 2018/5/22.
 */

@Service("uploadService")
public class UploadServiceImpl implements UploadService{

    //上传图片
    public String uploadPic(byte[] pic ,String name,long size){
        System.out.println("Service ................................");
        return FastDFSUtils.uploadPic(pic, name, size);
    }
}

