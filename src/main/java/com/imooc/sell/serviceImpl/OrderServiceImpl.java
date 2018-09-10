package com.imooc.sell.serviceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.imooc.sell.converter.OrderMaster2OrderDTOConverter;
import com.imooc.sell.dataobject.OrderDetail;
import com.imooc.sell.dataobject.OrderMaster;
import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.dto.CartDTO;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.repository.OrderDetailRepository;
import com.imooc.sell.repository.OrderMasterRepository;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.service.ProductService;
import com.imooc.sell.utils.KeyUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository ;
	
	@Autowired
	private OrderMasterRepository orderMasterRepository;
	
	@Override
	@Transactional
	public OrderDTO create(OrderDTO orderDTO) {
		String orderId=KeyUtil.genUniqueKey();
		BigDecimal orderAmount=new BigDecimal(BigInteger.ZERO);
		List<CartDTO> cartDTOs=new ArrayList<>();
		
		//1.查询商品（数量，价格）
		for(OrderDetail orderDetail:orderDTO.getOrderDetails())
		{
			ProductInfo productInfo=productService.findOne(orderDetail.getProductId());
			if(productInfo == null) {
				throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
			}
			//2.计算订单总价
			orderAmount = productInfo.getProductPrice()
					.multiply(new BigDecimal(orderDetail.getProductQuantity()))//乘法
					.add(orderAmount);//加法
			//订单详情入库
			orderDetail.setDetailId(KeyUtil.genUniqueKey());
			orderDetail.setOrderId(orderId);
			BeanUtils.copyProperties(productInfo, orderDetail);//spring自带的方法，进行对象属性拷贝
			orderDetailRepository.save(orderDetail);
		
			CartDTO cartDTO=new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
			cartDTOs.add(cartDTO);
		}
		
		
		//3.写入订单数据库（orderMaster和orderDetail）
		OrderMaster orderMaster=new OrderMaster();
		orderDTO.setOrderId(orderId);
		BeanUtils.copyProperties(orderDTO, orderMaster);
		orderMaster.setOrderAmount(orderAmount);
		orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
		orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
		orderMasterRepository.save(orderMaster);
		
		//4.扣库存
//		List<CartDTO>cartDTOs2=orderDTO.getOrderDetails()
//				.stream().map(e ->new CartDTO(e.getProductId(), e.getProductQuantity()))
//				.collect(Collectors.toList());
		
		productService.decreaseStock(cartDTOs);
		
		return orderDTO;
	}

	@Override
	public OrderDTO findOne(String orderId) {
		OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
		if(null == orderMaster)
		{
			throw new SellException(ResultEnum.ORDER_NOT_EXIST);
		}
		List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
		if(CollectionUtils.isEmpty(orderDetails))
		{
			throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
		}
		OrderDTO orderDTO=new OrderDTO();
		BeanUtils.copyProperties(orderMaster, orderDTO);
		orderDTO.setOrderDetails(orderDetails);
		return orderDTO;
	}

	@Override
	public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
		Page<OrderMaster> orderMasterPage =  orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
		
		List<OrderDTO> orderDTOs = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
		
		Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOs, pageable, orderMasterPage.getTotalElements());
		return orderDTOPage;
	}

	@Override
	@Transactional
	public OrderDTO cancel(OrderDTO orderDTO) {
		OrderMaster orderMaster = new OrderMaster();
		
		
		//判断订单状态
		if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
		{
			log.error("【取消订单】订单状态不正确，orderId={}, orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
			throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
		}
		//修改订单状态
		orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
		BeanUtils.copyProperties(orderDTO, orderMaster);
		OrderMaster updateResult = orderMasterRepository.save(orderMaster);
		if(null == updateResult)
		{
			log.error("【取消订单】更新失败，orderMaster={}",orderMaster);
			throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
		}
		
		//返回库存
		if(CollectionUtils.isEmpty(orderDTO.getOrderDetails()))
		{
			log.error("【取消订单】订单中无商品详情，orderDTO={}",orderDTO);
			throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
		}
		List<CartDTO> cartDTOs = orderDTO.getOrderDetails().stream()
				.map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
				.collect(Collectors.toList());
		
		productService.increaseStock(cartDTOs);
		
		//如果已支付，需要退款
		if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getClass()))
		{
			//TODO
		}
		return orderDTO;
	}

	@Override
	@Transactional
	public OrderDTO finish(OrderDTO orderDTO) {
		//判断订单状态
		if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
		{
			log.error("【完结订单】订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
			throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
		}
		
		//修改订单状态
		orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
		OrderMaster orderMaster = new OrderMaster();
		BeanUtils.copyProperties(orderDTO, orderMaster);
		OrderMaster updateResult = orderMasterRepository.save(orderMaster);
		if(null == updateResult)
		{
			log.error("【完结订单】更新失败，orderMaster={}",orderMaster);
			throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
		}
		return orderDTO;
	}

	@Override
	@Transactional
	public OrderDTO paid(OrderDTO orderDTO) {
		//判断订单状态
		if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
		{
			log.error("【订单支付完成】订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
			throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
			
		}
		//判断支付状态
		if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode()))
		{
			log.error("【订单支付完成】订单状态不正确，orderId={}",orderDTO);
			throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
		}
		//修改支付状态
		orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
		OrderMaster orderMaster = new OrderMaster();
		BeanUtils.copyProperties(orderDTO, orderMaster);
		OrderMaster updateResult = orderMasterRepository.save(orderMaster);
		if(null == updateResult)
		{
			log.error("【订单支付完成】订单状态不正确，orderId={}",orderMaster);
			throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
		}
		
		return orderDTO;
	}

}
