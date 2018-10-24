package cn.lili.common.Conversion;


import org.springframework.core.convert.converter.Converter;

/**
 * 自定义转换器
 * 去掉前后空格
 * <String ,String>  --页面上的类型，T指的是转换后的类型
 * Created by ABEL on 2018/6/9.
 */
public class CustomConverter implements Converter<String,String> {
    //去掉前后的空格
    public String convert(String source){
        try{
             if(null != source){
                 source = source.trim();
                 if(!"".equals(source)){  //  ""表示空串
                     return source;
                 }
             }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
