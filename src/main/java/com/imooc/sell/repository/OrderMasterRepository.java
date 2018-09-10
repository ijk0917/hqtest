package com.imooc.sell.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.imooc.sell.dataobject.OrderMaster;

public interface OrderMasterRepository extends JpaRepository<OrderMaster, String>{
	
	Page<OrderMaster> findByBuyerOpenid(String buyerOpenid,Pageable pageable);

	
}