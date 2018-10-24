import cn.lili.core.bean.user.Buyer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by ABEL on 2018/6/10.
 */
public class TestJJson {

    @Test
    public void testJSon() throws IOException {
        //Springmvc  @RequestBody   @ResponseBody   JSON与对象转换
        // OOM out of memery
        Buyer buyer = new Buyer();
        buyer.setUsername("lili超可爱");
        ObjectMapper om = new ObjectMapper();
        //null 就不转了
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);//null 不转
        StringWriter w = new StringWriter();
        om.writeValue(w,buyer);
        System.out.println(w.toString());
        //转回对象
        Buyer r = om.readValue(w.toString(), Buyer.class);
        System.out.println(r);
    }

    @Test
    public void test(){
        HashMap<String,String > hashMap = new HashMap<>();
        Hashtable<String,String > hashtable = new Hashtable<>();
    }
}
