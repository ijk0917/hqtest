package com.imooc.sell.serviceImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.dto.CartDTO;
import com.imooc.sell.enums.ProductStatusEnum;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.repository.ProductInfoRepository;
import com.imooc.sell.service.ProductService;
@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductInfoRepository productInfoRepository;
	
	@Override
	public ProductInfo findOne(String productId) {
		return productInfoRepository.findOne(productId);
	}

	@Override
	public List<ProductInfo> findUpAll() {
		return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
	}

	@Override
	public Page<ProductInfo>findAll(Pageable pageable) {
		return productInfoRepository.findAll(pageable);
	}

	@Override
	public ProductInfo save(ProductInfo productInfo) {
		return productInfoRepository.save(productInfo);
	}

	
	
	@Override
	@Transactional
	public void increaseStock(List<CartDTO> cartDTOs) {
		for(CartDTO cartDTO:cartDTOs)
		{
			ProductInfo productInfo = productInfoRepository.findOne(cartDTO.getProductId());
			if(null == productInfo)
			{
				throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
			}
			Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
			productInfo.setProductStock(result);
			productInfoRepository.save(productInfo);
		}
		
	}

	@Override
	@Transactional
	public void decreaseStock(List<CartDTO> cartDTOs) {
		for(CartDTO cartDTO:cartDTOs)
		{
			ProductInfo productInfo = productInfoRepository.findOne(cartDTO.getProductId());
			if(null == productInfo)
			{
				throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
			}
			
			Integer result = productInfo.getProductStock()-cartDTO.getProductQuantity();
			if(result<0)
			{
				throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
			}
			
			productInfo.setProductStock(result);
			
			productInfoRepository.save(productInfo);
		}
		
	}

}
