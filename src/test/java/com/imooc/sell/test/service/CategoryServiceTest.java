package com.imooc.sell.test.service;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.spi.ValidationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.sell.Application;
import com.imooc.sell.dataobject.ProductCategory;
import com.imooc.sell.service.CategoryService;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class CategoryServiceTest {

	@Autowired
	private CategoryService CategoryService;
	@Test
	public void findOne() {
		ProductCategory productCategory=CategoryService.findOne(1);
		Assert.assertEquals(new Integer(1), productCategory.getCategoryId());
	}
	
	@Test
	public void findAll()
	{
		List<ProductCategory> productCategories=CategoryService.findAll();
		Assert.assertNotSame(new Integer(0), productCategories.size());
	}
	
	@Test
	public void findByCategoryTypeIn()
	{
		List<ProductCategory> productCategories=CategoryService.findByCategoryTypeIn(Arrays.asList(2,3,4));
		Assert.assertNotSame(new Integer(0), productCategories.size());
	}
	
	@Test
	@Transactional
	public void save()
	{
		ProductCategory productCategory=new ProductCategory("男生专享",12);
		ProductCategory result=CategoryService.save(productCategory);
		Assert.assertNotNull(result);
	}

}
