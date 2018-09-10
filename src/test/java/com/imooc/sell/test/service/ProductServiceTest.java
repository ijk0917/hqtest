package com.imooc.sell.test.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.sell.Application;
import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.enums.ProductStatusEnum;
import com.imooc.sell.service.ProductService;

import junit.framework.Assert;
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class ProductServiceTest {

	@Autowired
	private ProductService productService;
	
	@Test
	public void testFindOne() {
		ProductInfo productInfo=productService.findOne("123456");
		Assert.assertEquals("123456", productInfo.getProductId());
	}

	@Test
	public void testFindUpAll() {
		List<ProductInfo> productInfos=productService.findUpAll();
		Assert.assertNotSame(0, productInfos.size());
	}

	@Test
	public void testFindAll() {
		PageRequest request=new PageRequest(0, 2);
		Page<ProductInfo> productInfoPage= productService.findAll(request);
		System.out.println(productInfoPage.getTotalElements());
	}

	@Test
	public void testSave() {
		ProductInfo productInfo=new ProductInfo();
		
		productInfo.setProductId("123457");
		productInfo.setProductName("皮皮虾");
		productInfo.setProductPrice(new BigDecimal(3.2));
		productInfo.setProductStock(100);
		productInfo.setProductDescription("很好喝的粥");
		productInfo.setProductIcon("http://xxxx.jpg");
		productInfo.setProductStatus(0);
		productInfo.setCategoryType(ProductStatusEnum.DOWN.getCode());
		
		ProductInfo result=productService.save(productInfo);
		Assert.assertNotNull(result);
	}
	

}
