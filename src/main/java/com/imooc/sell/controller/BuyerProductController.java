package com.imooc.sell.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.sell.VO.ProductInfoVO;
import com.imooc.sell.VO.ProductVO;
import com.imooc.sell.VO.ResultVO;
import com.imooc.sell.dataobject.ProductCategory;
import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.repository.ProductInfoRepository;
import com.imooc.sell.service.CategoryService;
import com.imooc.sell.service.ProductService;
import com.imooc.sell.utils.ResultVOUtil;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/list")
	public ResultVO list() {
		// 1.查询所有的上架商品
		List<ProductInfo> productInfos = productService.findUpAll();
		// 2.查询类目（一次性查询）
		List<Integer> categoryTypeList = new ArrayList<Integer>();
		// 传统方法
		for (ProductInfo productInfo : productInfos) {
			categoryTypeList.add(productInfo.getCategoryType());
		}
		// 精简方法（java8,lambda）
//		categoryTypeList = productInfos.stream()
//				.map(e -> e.getCategoryType())
//				.collect(Collectors.toList());
		
		List<ProductCategory> productCategories=categoryService.findByCategoryTypeIn(categoryTypeList);
		// 3.数据拼装
		List<ProductVO> productVOs=new ArrayList<>();
		for(ProductCategory productCategory:productCategories)
		{
			ProductVO productVO=new ProductVO();
			productVO.setCategoryType(productCategory.getCategoryType());
			productVO.setCategoryName(productCategory.getCategoryName());
			
			
			List<ProductInfoVO> productInfoVOs=new ArrayList<>();
			for(ProductInfo productInfo:productInfos)
			{
				if(productInfo.getCategoryType().equals(productCategory.getCategoryType())) 
				{
					ProductInfoVO productInfoVO=new ProductInfoVO();
					//拷贝数据
					BeanUtils.copyProperties(productInfo, productInfoVO);
					productInfoVOs.add(productInfoVO);
				}
			}
			productVO.setProductInfoVOs(productInfoVOs);
			productVOs.add(productVO);
		}
		
		return ResultVOUtil.success(productVOs);
	}
}
