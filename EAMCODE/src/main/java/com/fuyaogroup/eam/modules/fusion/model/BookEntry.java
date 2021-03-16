package com.fuyaogroup.eam.modules.fusion.model;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("book_detial")
public class BookEntry extends BaseEntity{
	private String bid;
    private String bookName;
    private String clazzName;
    private String bookCode;
    private String department;
    private String state;
}
