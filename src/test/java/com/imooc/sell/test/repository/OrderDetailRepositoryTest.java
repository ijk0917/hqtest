package com.imooc.sell.test.repository;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.sell.Application;
import com.imooc.sell.dataobject.OrderDetail;
import com.imooc.sell.repository.OrderDetailRepository;

import junit.framework.Assert;
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class OrderDetailRepositoryTest {
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Test
	public void testSave() {
		OrderDetail orderDetail=new OrderDetail();
		
		orderDetail.setDetailId("1234567810");
		orderDetail.setOrderId("111111111");
		orderDetail.setProductIcon("http://xxxx,jpg");
		orderDetail.setProductId("111111112");
		orderDetail.setProductName("皮蛋粥");
		orderDetail.setProductPrice(new BigDecimal(2.2));
		orderDetail.setProductQuantity(2);
		
		OrderDetail result=orderDetailRepository.save(orderDetail);
		Assert.assertNotNull(result);
	}

	@Test
	public void testFindByOrderId() {
		List<OrderDetail> orderDetails=orderDetailRepository.findByOrderId("111111111");
		Assert.assertNotSame(0, orderDetails.size());
	}

	
}
