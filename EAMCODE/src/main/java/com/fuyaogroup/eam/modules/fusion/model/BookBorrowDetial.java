package com.fuyaogroup.eam.modules.fusion.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("book_borrowDetial")
public class BookBorrowDetial extends BaseEntity{

	private String bid;
    private String manID;
    private String name;
    private String bookName;
    private String bookID;
    private String telphone;
    private String btime;
    private String rtime;
    private int state;
}
