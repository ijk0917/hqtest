package com.imooc.sell.test.repository;

import static org.junit.Assert.*;


import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.sell.Application;
import com.imooc.sell.dataobject.OrderMaster;
import com.imooc.sell.repository.OrderMasterRepository;

import junit.framework.Assert;



@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class OrderMasterRepositoryTest {

	@Autowired
	private OrderMasterRepository orderMasterRepository;
	
	private final String OPENID="110110";
	@Test
	public void saveTest()
	{
		OrderMaster orderMaster=new OrderMaster();
		
		orderMaster.setOrderId("123457");
		orderMaster.setBuyerName("师兄");
		orderMaster.setBuyerPhone("123456789123");
		orderMaster.setBuyerAddress("慕课网");
		orderMaster.setBuyerOpenid(OPENID);
		orderMaster.setOrderAmount(new BigDecimal(2.3));
		
		OrderMaster result = orderMasterRepository.save(orderMaster);
		
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testFindByBuyerOpengid() {
		PageRequest request=new PageRequest(1, 6);

		Page<OrderMaster> result=orderMasterRepository.findByBuyerOpenid(OPENID, request);
		System.out.println("aaaaa: "+result.getTotalElements());
	}

}
