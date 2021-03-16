package com.fuyaogroup.eam.modules.fusion.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.modules.fusion.model.BookEntry;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
public class BookInfoVo {

	private String id;
	
	@ExcelProperty(index = 0)
    private String bookName;
    @ExcelProperty(index = 1)
    private String clazzName;
    @ExcelProperty(index = 2)
    private String bookCode;
    @ExcelProperty(index = 3)
    private String department;
    @ExcelProperty(index = 4)
    private String state;
	
}
