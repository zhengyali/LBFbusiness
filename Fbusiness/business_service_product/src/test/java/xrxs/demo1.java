package xrxs;

import org.junit.Test;

import java.net.InetAddress;

/**
 * Created by 鸭梨 on 2018/8/1.
 * InetAddress类代表了一个网络目标地址，包括主机名和数字类型的地址信息。
 * 该类有两个子类，Inet4Address和Inet6Address，分别对应了目前IP地址的两个版本。
 *  byte[] getAddress()
 返回此 InetAddress 对象的原始 IP 地址。
 String getHostAddress()
 返回 IP 地址字符串（以文本表现形式）。
 String getHostName()
 获取此 IP 地址的主机名。
此处无法运行
 */
public class demo1 {
    @Test
    public void getHostName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println(inetAddress.getHostName());
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    @Test
    public  String getHostIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (Exception e) {
            return "unknown";
        }
    }

}
