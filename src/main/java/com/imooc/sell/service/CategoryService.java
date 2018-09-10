package com.imooc.sell.service;

import java.util.List;

import com.imooc.sell.dataobject.ProductCategory;

public interface CategoryService {
	ProductCategory findOne(Integer categoryId);
	
	List<ProductCategory> findAll();
	
	List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
	
	ProductCategory save(ProductCategory productCategory);
}
