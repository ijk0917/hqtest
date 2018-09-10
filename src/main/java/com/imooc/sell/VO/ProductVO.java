package com.imooc.sell.VO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
/*
 * 商品（包含类目）
 * 
 * */
@Data
public class ProductVO {
	@JsonProperty("name")
	private String categoryName;
	
	@JsonProperty("type")
	private Integer categoryType;
	
	@JsonProperty("foods")
	private List<ProductInfoVO> productInfoVOs;
}
