package com.my.ssm.mapper;

import java.util.List;

import com.my.ssm.po.ItemsCustom;
import com.my.ssm.po.ItemsQueryVo;

public interface ItemsMapperCustom {

	// 商品查询列表
	public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception;

}