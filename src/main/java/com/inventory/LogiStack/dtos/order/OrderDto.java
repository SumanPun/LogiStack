package com.inventory.LogiStack.dtos.order;

import com.inventory.LogiStack.dtos.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private LocalDateTime orderDate;
    private String orderTrackingNumber;
    private String status;
    private BigDecimal totalPrice;
//    private List<AddProductToOrderDto> product;
    private List<OrderItemDto> orderItems;
    private Long userId;
}
