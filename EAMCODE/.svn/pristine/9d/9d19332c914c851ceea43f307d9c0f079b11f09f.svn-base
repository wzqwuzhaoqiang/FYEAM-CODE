package eam.fusion;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fuyaogroup.eam.EamApplication;
import com.fuyaogroup.eam.common.service.WeixinMessageService;
import com.fuyaogroup.eam.common.service.WeixinService;

@SpringBootTest(classes = EamApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class WeixinTest {
	
	@Test
	public void sendWeixin() throws Exception {
		WeixinMessageService wms = new WeixinMessageService();
		WeixinService wx = new WeixinService();
		 wx.getAccessToken();
		boolean flag = wms.send("112520", "", "...");
		System.out.println("是否成功发送："+flag);
	}
}
