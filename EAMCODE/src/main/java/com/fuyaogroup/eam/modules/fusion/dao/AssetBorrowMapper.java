package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.AssetBorrow;

@Mapper
public interface AssetBorrowMapper extends BaseMapper<AssetBorrow> {

	@Select("select * from  \"asset_borrow_trackrecord\"")
	List<AssetBorrow> queryList();

}
