package com.fuyaogroup.eam.modules.fusion.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import lombok.Data;

@Data
public class QtfwObject extends BaseRowModel{

	@ExcelProperty(value = "借用人工号",index = 0)
		private String borrowerId;//
	@ExcelProperty(value = "借用人姓名",index = 1)
		private String borrowerName;//
	@ExcelProperty(value = "借用物品",index = 2)
		private String tools;//
	@ExcelProperty(value = "借用数量",index = 3)
		private int count;//
	@ExcelProperty(value = "借用时间",index = 4)
		private String borrowTime;//
	@ExcelProperty(value = "归还时间",index = 5)
		private String returnTime;//
	@ExcelProperty(value = "状态",index = 6)
		private String status;//
	@ExcelProperty(value = "借用人手机号",index = 7)
		private String mobile;
    
    
    
    
}
