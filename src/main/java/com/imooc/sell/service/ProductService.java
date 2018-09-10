package com.imooc.sell.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.dto.CartDTO;

public interface ProductService {
	ProductInfo findOne(String productId);
	
	List<ProductInfo> findUpAll();
	
	Page<ProductInfo> findAll(Pageable pageable);
	
	ProductInfo save(ProductInfo productInfo);
	
	//加库存
	void increaseStock(List<CartDTO> cartDTOs);
	
	//减库存
	void decreaseStock(List<CartDTO> cartDTOs);
}
