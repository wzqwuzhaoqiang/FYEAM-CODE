package com.fuyaogroup.eam.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fuyaogroup.eam.modules.fusion.model.vo.BookInfoVo;

public class EasyExcelUtil extends AnalysisEventListener<BookInfoVo>{

	private static List<BookInfoVo> bList = new ArrayList<BookInfoVo>();
	
	@Override
	public void invoke(BookInfoVo data, AnalysisContext context) {
		data.setState("0");
		bList.add(data);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		// TODO Auto-generated method stub
		
	}

	public static List getBList() {
		return bList;
	}
	
}
