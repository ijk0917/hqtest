package com.imooc.sell.test.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.sell.Application;
import com.imooc.sell.dataobject.ProductCategory;
import com.imooc.sell.repository.ProductCategoryRepository;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class ProductCategoryRepositoryTest {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	
	@Test
	public void test() {
		ProductCategory productCategory=productCategoryRepository.findOne(1);
		System.out.println(productCategory);
	}
	
	@Test
	public void saveTest()
	{
		ProductCategory productCategory=new ProductCategory();
		productCategory.setCategoryName("男生最爱");
		productCategory.setCategoryId(2);
		productCategory.setCategoryType(3);
		productCategoryRepository.save(productCategory);
	}

	@Test
	public void findByCategoryTypeInTest()
	{
		List<Integer> list=Arrays.asList(2,3,4);
		List<ProductCategory> productCategoryList=productCategoryRepository.findByCategoryTypeIn(list);
		Assert.assertEquals(2, productCategoryList.size());
	}
}
