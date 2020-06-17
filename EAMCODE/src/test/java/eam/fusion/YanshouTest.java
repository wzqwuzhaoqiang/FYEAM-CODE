package eam.fusion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fuyaogroup.eam.EamApplication;
import com.fuyaogroup.eam.controller.AddAssetController;
import com.fuyaogroup.eam.modules.fusion.model.WindowServer;
import com.fuyaogroup.eam.modules.fusion.service.WindowServerService;
import com.soa.eis.adapter.framework.message.IMsgObject;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = EamApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class YanshouTest {
	
	@Autowired
	private WindowServerService wss;
	
	@Test
	public void ttest() throws Exception {
		String str = "insert into asset_borrow_trackrecord (OAID,company,formDate,borrower,department,contactInfor,borrowThing,number,purpose,assertNumber,assertName,model,serialNumber,configInfo,startDate,borrowOutDate,borrowUseDate,borrowOutman,returnIs,returntwoIs,renewDateNumber,thingSituation,reciver,returnDate)values(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)";
		System.out.println(str.toUpperCase());
	}
	@Test
	public void tet() {
		WindowServer ws = new WindowServer();
		ws.setTableID((UUID.randomUUID().toString()).substring(0,8));
		ws.setBorrowerId("userid");
		ws.setBorrowerName(("userName"));
		ws.setTools(("thingName"));
		ws.setBorrowTime(("currentTime"));
		ws.setCount(1);
		ws.setStatus("在借");
		//System.out.println(ws.toString());
		//wss.saveWindowServer(ws);
		List<WindowServer> wsl = new ArrayList<WindowServer>();
		wsl = wss.queryInBorrowThing("101798");
		for(WindowServer qser:wsl) {
			System.out.println("借用数据："+qser.toString());
		}
		//wss.saveOrUpdate(ws);
		//wss.save(ws);
	}
}
