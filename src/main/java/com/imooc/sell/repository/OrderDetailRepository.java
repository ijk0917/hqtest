package com.imooc.sell.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imooc.sell.dataobject.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String>{
	List<OrderDetail> findByOrderId(String orderId);
}
