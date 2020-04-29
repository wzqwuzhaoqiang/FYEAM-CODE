package eam.fusion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONArray;
import com.fuyaogroup.eam.EamApplication;
import com.fuyaogroup.eam.common.Json.JsonBean;
import com.fuyaogroup.eam.util.FusionRestUtil;

@SpringBootTest(classes = EamApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class HCMuserTest {
//	@Value("${interface.fusionRest}")
	private  String fusionRest="https://ekfa-test.fa.us2.oraclecloud.com";
	
//	@Value("${interface.fusionSoap.deaultUserName}")
	private  String deaultUserName="112520";
	
//	@Value("${interface.fusionSoap.deaultPassword}")
	private  String deaultPassword="fy123456";
	
//	@Value("${invention.organization}")
	private  String organizationId="300000003746043";
	public  <T> List<T> getFusionListFromOjbect(String reponseStr,Class<T> object){
		List<T> list = new ArrayList<T>();  
		String result=reponseStr;
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(result);
		if(otherjb.get("items")!=null){
		list =JSONArray.parseArray(otherjb.get("items").toString(),object);
		}else{
			otherjb = com.alibaba.fastjson.JSONObject.parseObject(result);
			list =JSONArray.parseArray('['+otherjb.toString()+']',object);
		}
		return list;
	}
	@Test
	public void getOneUser() throws Exception {
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/hcmRestApi/resources/11.13.18.05/workers?q=PersonNumber=083380";
		String	mainworkresult = frUtil.get(url,deaultUserName,deaultPassword);
		List<Map> map = this.getFusionListFromOjbect(mainworkresult, Map.class);
		String personId=map.get(0).get("PersonId").toString();
		this.getWorkRelation(personId);
		System.out.print(map.get(0).get("PersonId"));
		}
	@Test
	public void getUsers(){
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/hcmRestApi/resources/11.13.18.05/workers?totalResults=true";
		String mainworkresult = null;
		try {
			mainworkresult = frUtil.get(url,deaultUserName,deaultPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Map> map = this.getFusionListFromOjbect(mainworkresult, Map.class);
		System.out.print(map.toString());
	}
	
	public String getWorkRelation(String PersonId){
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		
		String url =fusionRest+ "/hcmRestApi/resources/11.13.18.05/workers/"+PersonId+"/child/workRelationships";
		String mainworkresult = null;
		try {
				mainworkresult = frUtil.get(url,deaultUserName,deaultPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mainworkresult;
		
	}
	
	@Test
	public void createOneWork(){
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		//String url = fusionRest+"/hcmRestApi/resources/11.13.18.05/workers";
		String url="https://ekfa-test.fa.us2.oraclecloud.com:443/hcmRestApi/resources/11.13.18.05/userAccounts";
		HashMap<String, Object> testObj = new HashMap<String, Object>();
		testObj.put("PersonId", "3001000034480020");
//		testObj.put("Salutation" , "MISS.");
//		testObj.put(  "LegalEntityId" , "300100003448002");
//		testObj.put(  "FirstName", "苏");
		testObj.put(  "PersonNumber", "75");
//		testObj.put(  "LastName", "秀");
//		testObj.put(  "DisplayName" , "苏秀");
//		testObj.put(  "WorkPhoneCountryCode" , "1");
//		testObj.put(  "WorkPhoneAreaCode" , "1");
//		testObj.put(  "phones" , "13635275950");
//		testObj.put(  "CreatedBy" , "112520");
//		testObj.put(  "WorkMobilePhoneAreaCode" , "1");
//		testObj.put(  "WorkMobilePhoneNumber" , "18060480054");
//		testObj.put(  "HomeFaxCountryCode","1");
//		testObj.put(  "HomeFaxAreaCode","1");
//		testObj.put(  "HomeFaxNumber","1111663");
//		testObj.put(  "HomePhoneCountryCode","1");
//		testObj.put(  "HomePhoneAreaCode","1");
//		testObj.put(  "HomePhoneNumber","122993333");
//		testObj.put(  "NameSuffix","Jr.");
//		testObj.put(  "NationalIdExpirationDate" , "4712-12-31");
        List<JsonBean> lbs = new ArrayList<JsonBean>();  

		JsonBean j1 = new JsonBean("苏", "秀", "苏秀");  
		lbs.add(j1);
        net.sf.json.JSONArray ja_beans =net.sf.json.JSONArray.fromObject(lbs);  
        Map<String,String> map = new HashMap<String,String>();
//        map.put( "FirstName", "苏");
//        map.put( "LastName", "秀");
//        map.put( "DisplayName", "苏秀");
		testObj.put( "names", "[{\"FirstName\":\"苏\",\"LastName\":\"秀\"}]");
//		map.put(key, value)
//		testObj.put(  "EmailAddress" ,  "xiu.su@fuyaogroup.com");
//		testObj.put(  "AddressLine1" , "福建省福清市福耀花园2#204");
//		testObj.put(  "AddressLine2" , "XX路XX号");
//		testObj.put(  "AddressLine3" , null);
//		testObj.put(  "City" , "福清");
//		testObj.put(  "Region" , "福建省");
//		testObj.put(  "Region2" , null);
//		testObj.put(  "Country" , "China");
//		testObj.put(  "PostalCode", "350100");
//		testObj.put(  "CitizenshipLegislationCode" , "China");
		
//		testObj.put(  "DateOfBirth", "1995-09-09");
//		testObj.put(  "Ethnicity", "8");
//		testObj.put(  "Gender" , "F");
//		testObj.put(  "MaritalStatus" , "S");
//		testObj.put(  "NationalIdCountry", "China");
		testObj.put(  "UserName","xiu.su@fuyaogroup.com");
		JSONObject jsonObj = new JSONObject(testObj);
		String mainworkresult = null;
		try {
			mainworkresult = frUtil.post(url, jsonObj.toString(),deaultUserName,deaultPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		com.alibaba.fastjson.JSONObject itemMap = com.alibaba.fastjson.JSONObject.parseObject(mainworkresult);
		if(Integer.parseInt(itemMap.get("count").toString())==0){
//			System.out.print(itemMap.toString());
		}
		System.out.print(itemMap.toString());
	}
		
	
}
