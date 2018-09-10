package com.imooc.sell.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;


/*
 * 类目
 */
@Entity
@DynamicUpdate/*动态更新*/
@Table(name = "product_category")
@Data
public class ProductCategory {
	/* 类目id */
	@Id
	@GeneratedValue
	private Integer categoryId;
	
	/*类目名字*/
	private String categoryName;
	
	/*类目编号*/
	private Integer categoryType;
	
	public ProductCategory()
	{}
	
	public ProductCategory(String categoryName,Integer categoryType)
	{
		this.categoryName=categoryName;
		this.categoryType=categoryType;
	}
}
