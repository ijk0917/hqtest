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
import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.repository.ProductInfoRepository;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class ProductInfoRepositoryTest {

	@Autowired                    
	private ProductInfoRepository productInfoRepository;
	
	@Test
	public void saveTest()
	{
		ProductInfo productInfo=new ProductInfo();
		
		productInfo.setProductId("123456");
		productInfo.setProductName("皮蛋粥");
		productInfo.setProductPrice(new BigDecimal(3.2));
		productInfo.setProductStock(100);
		productInfo.setProductDescription("很好喝的粥");
		productInfo.setProductIcon("http://xxxx.jpg");
		productInfo.setProductStatus(0);
		productInfo.setCategoryType(2);
		
		System.out.println(productInfo);
		ProductInfo result=productInfoRepository.save(productInfo);
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testFindByProductStatus() {
		List<ProductInfo> productInfos=productInfoRepository.findByProductStatus(0);
		Assert.assertEquals(1, productInfos.size());
	}

}
