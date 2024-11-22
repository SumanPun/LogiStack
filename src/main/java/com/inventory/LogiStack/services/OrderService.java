package com.inventory.LogiStack.services;

import com.inventory.LogiStack.dtos.OrderItemDto;
import com.inventory.LogiStack.dtos.order.AddProductToOrderDto;
import com.inventory.LogiStack.dtos.order.ConformOrderDto;
import com.inventory.LogiStack.dtos.order.CreateOrder;
import com.inventory.LogiStack.dtos.order.OrderDto;

public interface OrderService {

    OrderDto createOrder();
    OrderItemDto addProductToOrder(Long orderId, AddProductToOrderDto model);
    OrderDto getOrderDetails(Long orderId);
    Boolean deleteProductFromOrder(Long orderId, Long productItemId);
    OrderDto getOrderFromTrackingNumber(String trackingNumber);
    OrderDto createInvoiceOrder(Long orderId);
    Boolean conformOrder(Long orderId, ConformOrderDto model);
}
