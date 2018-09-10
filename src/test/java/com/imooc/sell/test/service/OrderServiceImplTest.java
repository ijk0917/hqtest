package com.imooc.sell.test.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.sell.Application;
import com.imooc.sell.dataobject.OrderDetail;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.serviceImpl.OrderServiceImpl;

import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@Slf4j
public class OrderServiceImplTest {

	@Autowired
	private OrderServiceImpl orderService;
	
	private final String BUYER_OPENID="1101110";
	
	private final String ORDER_ID="1534053279280601307";
	@Test
	public void testCreate() {
		OrderDTO orderDTO=new OrderDTO();
		orderDTO.setBuyerName("廖师兄");
		orderDTO.setBuyerAddress("慕课网");
		orderDTO.setBuyerPhone("123456789012");
		orderDTO.setBuyerOpenid(BUYER_OPENID);
		
		//购物车
		List<OrderDetail> orderDetails=new ArrayList<>();
		
		OrderDetail o1 = new OrderDetail();
		o1.setProductId("1234568");
		o1.setProductQuantity(1);
		
		OrderDetail o2=new OrderDetail();
		o2.setProductId("123457");
		o2.setProductQuantity(2);
		
		orderDetails.add(o1);
		orderDetails.add(o2);
		
		orderDTO.setOrderDetails(orderDetails);
		
		OrderDTO result = orderService.create(orderDTO);
		log.info("【创建订单】 result={}",result);
		Assert.assertNotNull(result);
	}

	@Test
	public void testFindOne() {
		OrderDTO result = orderService.findOne(ORDER_ID);	
		log.info("【查询单个订单】result={}",result);
		Assert.assertEquals(ORDER_ID, result.getOrderId());
	}

	@Test
	public void testFindList() {
		PageRequest request = new PageRequest(0, 2);
		Page<OrderDTO> orderDTOPage = orderService.findList(BUYER_OPENID, request);
		Assert.assertNotSame(0, orderDTOPage.getTotalElements());
	}

	@Test
	public void testCancel() {
		OrderDTO orderDTO = orderService.findOne(ORDER_ID);	
		OrderDTO result = orderService.cancel(orderDTO);
		Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(), result.getOrderStatus());
		
	}

	@Test
	public void testFinish() {
		OrderDTO orderDTO = orderService.findOne(ORDER_ID);	
		OrderDTO result = orderService.finish(orderDTO);
		Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(), result.getOrderStatus());
		
	}

	@Test
	public void testPaid() {
		OrderDTO orderDTO = orderService.findOne(ORDER_ID);	
		OrderDTO result = orderService.paid(orderDTO);
		Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), result.getPayStatus());
		
	}

}
