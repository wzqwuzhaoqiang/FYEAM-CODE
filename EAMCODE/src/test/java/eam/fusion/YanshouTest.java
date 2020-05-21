package eam.fusion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fuyaogroup.eam.EamApplication;
import com.fuyaogroup.eam.controller.AddAssetController;
import com.soa.eis.adapter.framework.message.IMsgObject;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = EamApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class YanshouTest {
	
	@Test
	public void ttest() throws Exception {
		String str = "insert into asset_borrow_trackrecord (OAID,company,formDate,borrower,department,contactInfor,borrowThing,number,purpose,assertNumber,assertName,model,serialNumber,configInfo,startDate,borrowOutDate,borrowUseDate,borrowOutman,returnIs,returntwoIs,renewDateNumber,thingSituation,reciver,returnDate)values(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)";
		System.out.println(str.toUpperCase());
	}
}
