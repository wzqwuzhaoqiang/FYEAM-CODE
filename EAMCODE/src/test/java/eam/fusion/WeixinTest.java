package eam.fusion;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuyaogroup.eam.EamApplication;
import com.fuyaogroup.eam.common.service.WeixinMessageService;
import com.fuyaogroup.eam.common.service.WeixinService;
import com.fuyaogroup.eam.common.service.qtfwWeixinService;
import com.fuyaogroup.eam.modules.fusion.model.BookBorrowDetial;
import com.fuyaogroup.eam.modules.fusion.model.WindowServer;
import com.fuyaogroup.eam.modules.fusion.model.vo.BookInfoVo;
import com.fuyaogroup.eam.modules.fusion.model.vo.QtfwObject;
import com.fuyaogroup.eam.modules.fusion.service.BookServerService;
import com.fuyaogroup.eam.modules.fusion.service.WindowServerService;
import com.fuyaogroup.eam.util.ChineseToEnglish;
import com.fuyaogroup.eam.util.EasyExcelUtil;

@SpringBootTest(classes = EamApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class WeixinTest {
	
	@Autowired
    private qtfwWeixinService ws; 
	
	@Autowired
	private BookServerService bss;
	
	@Autowired
    private WindowServerService wss;
	@Test
	public void sendWeixin() throws Exception {
		
		
//		BookBorrowDetial ws = new BookBorrowDetial();
//		ws.setBid((UUID.randomUUID().toString()).substring(0,8));
//		ws.setManID("101798");
//		ws.setName("巫赵强");
//		ws.setBookName("十二届全国人大四次会议文件");
//		ws.setBtime("2020-07-31 13:33:32");
//		ws.setBookID("FYZFZL-2A-23");
//		ws.setTelphone("12345678953");
//		ws.setState(0);
//		log.info("++++++bookApprove2222222++++++    ");
//		bss.saveBook(ws);
		
		
		
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String currentTime = sdf.format(new Date());
//		BookBorrowDetial ws = bss.selectGetById("sdaesasda");
//		ws.setRtime(currentTime);
//		ws.setState(2);
//		bss.updateBookById(ws);
//		int count = bss.selectCount("FYZFZL-2A-19",2);
//		System.out.println("数据++"+count);
		String filename = "C:\\Users\\gnb781\\Desktop\\宣传部书籍二维码打印数据库.xlsx";
		
        //Excel读入对象中
        //实现excel读操作
        EasyExcel.read(filename, BookInfoVo.class, new EasyExcelUtil()).sheet().doRead();
        List<BookInfoVo> bList = EasyExcelUtil.getBList();
        bList.forEach(item->{
        	item.setId(UUID.randomUUID().toString().substring(0, 12));
        	bss.insertBookDetial(item);
        	System.out.println(item.toString());
        });
        
        //System.out.println(result);
		
		//System.out.println("测试");
//		String filename = "D:\\前台借用记录.xls";
//    	List<WindowServer> wsList = new ArrayList<WindowServer>();
//    	List<QtfwObject> QOList = new ArrayList<QtfwObject>();
//    	wsList = wss.queryAll();
//    	if(wsList == null || wsList.size()<1) {
//    		//return null;
//    		System.out.println("结果为空");
//    	}
//    	wsList.forEach(ws->{
//    		QtfwObject qObject = new QtfwObject();
//    		BeanUtils.copyProperties(ws, qObject);
//    		QOList.add(qObject);
//    	});
//    	
//    	EasyExcel.write(filename, QtfwObject.class).sheet("借用记录").doWrite(QOList);
//    	Map<String, Object> map = new HashMap<String, Object>();
//    	map.put("res", "成功导出到 D:\\前台借用记录.xls 文件中");
//    	//return map;
	}
}
