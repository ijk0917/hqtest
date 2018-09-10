package com.imooc.sell.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.sell.dataobject.OrderDetail;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import com.imooc.sell.utils.serializer.Date2LongSerializer;

import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
	/*订单id*/
	private String orderId;
	
	/*买家名字*/
	private String buyerName;
	
	/*买家手机号*/
	private String buyerPhone;
	
	/*买家地址*/
	private String buyerAddress;
	
	/*买家微信openid*/
	private String buyerOpenid;
	
	/*订单总金额*/
	private BigDecimal orderAmount;
	
	/*订单状态，默认为新下单*/
	private Integer orderStatus=OrderStatusEnum.NEW.getCode();
	
	/*支付状态，默认为0未支付*/
	private Integer payStatus=PayStatusEnum.WAIT.getCode();

	/*创建时间*/
	@JsonSerialize(using = Date2LongSerializer.class)
	private Date createTime;
	
	/*更新时间*/
	@JsonSerialize(using = Date2LongSerializer.class)
	private Date updateTime;
	
	
	private List<OrderDetail> orderDetails;
}
