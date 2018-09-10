package com.imooc.sell.converter;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.imooc.sell.dataobject.OrderMaster;
import com.imooc.sell.dto.OrderDTO;

public class OrderMaster2OrderDTOConverter {
	public static OrderDTO  convert(OrderMaster orderMaster)
	{
		
		OrderDTO orderDTO = new OrderDTO();
		
		BeanUtils.copyProperties(orderMaster, orderDTO);
		return orderDTO;
	}
	
	public static List<OrderDTO> convert(List<OrderMaster> orderMasters)
	{
		return orderMasters.stream().map(e ->
			convert(e)
		).collect(Collectors.toList());
	}
}
