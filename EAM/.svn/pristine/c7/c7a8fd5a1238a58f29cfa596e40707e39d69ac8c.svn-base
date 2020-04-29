package eam.fusion;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fuyaogroup.eam.EamApplication;
import com.fuyaogroup.eam.util.ImageUtil;

@SpringBootTest(classes = EamApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ImageTest {
	@Test
	public void createImage() throws Exception {
		  ImageUtil cg = new ImageUtil();
          try {
              String tableData1[][] = {{"8月31日","累计用户数","目标值","完成进度","时间进度", "进度差异"}, {"掌厅客户端（户）","469281","1500000","31.2%","33.6%", "-2.4%"}};
              String[][] tableData2 = {{"设备","设备编码","工厂","设备科","检修日期","检修内容"},
              {"合肥和巢湖","469281","1500000","31.2%","33.6%"},
              {"芜湖","469281","1500000","31.2%","33.6%"},
              {"蚌埠","469281","1500000","31.2%","33.6%"},
              {"淮南","469281","1500000","31.2%","33.6%"},
              {"马鞍山","469281","1500000","31.2%","33.6%"},
              {"淮北","469281","1500000","31.2%","33.6%"}};
//              cg.myGraphicsGeneration(tableData2, "D:\\123\\123\\myPic.jpg","明日预防性维护列表",);
          } catch (Exception e) {
              e.printStackTrace();
          }
	}
}
