package com.imooc.sell.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.sell.dataobject.ProductCategory;
import com.imooc.sell.repository.ProductCategoryRepository;
import com.imooc.sell.service.CategoryService;
@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private ProductCategoryRepository ProductCategoryRepository;
	
	@Override
	public ProductCategory findOne(Integer categoryId) {
		return ProductCategoryRepository.findOne(categoryId);
	}

	@Override
	public List<ProductCategory> findAll() {
		return ProductCategoryRepository.findAll();
	}

	@Override
	public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
		return ProductCategoryRepository.findByCategoryTypeIn(categoryTypeList);
	}

	@Override
	public ProductCategory save(ProductCategory productCategory) {
		return ProductCategoryRepository.save(productCategory);
	}
	
}
