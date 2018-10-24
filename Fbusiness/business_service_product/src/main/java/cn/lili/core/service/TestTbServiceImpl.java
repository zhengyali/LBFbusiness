package cn.lili.core.service;

import cn.lili.core.bean.TestTb;
import cn.lili.core.dao.TestTbDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**测试*/
@Service("testTbService")
public class TestTbServiceImpl implements TestTbService {

	public void insertTestTb(TestTb testTb){
		System.out.println("testing !!!!!!!!!!!!!!!!!!!!!!!!");
	}
    public static void main(String args[]){
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			System.out.println(inetAddress.getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}


	}


}
