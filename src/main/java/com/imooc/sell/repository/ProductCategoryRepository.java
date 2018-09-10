package com.imooc.sell.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imooc.sell.dataobject.ProductCategory;
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer>{
	List<ProductCategory> findByCategoryTypeIn(List<Integer> list);
}
