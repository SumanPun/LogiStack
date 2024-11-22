package com.inventory.LogiStack.controllers;

import com.inventory.LogiStack.dtos.ApiResponse;
import com.inventory.LogiStack.dtos.OrderItemDto;
import com.inventory.LogiStack.dtos.order.AddProductToOrderDto;
import com.inventory.LogiStack.dtos.order.ConformOrderDto;
import com.inventory.LogiStack.dtos.order.CreateOrder;
import com.inventory.LogiStack.dtos.order.OrderDto;
import com.inventory.LogiStack.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/logistack/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public ResponseEntity<OrderDto> createOrder(){
        OrderDto createdOrder = orderService.createOrder();
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
    @PostMapping("/{orderId}")
    public ResponseEntity<OrderItemDto> addProductToOrder(@PathVariable(name = "orderId") Long orderId, @RequestBody AddProductToOrderDto model){
        OrderItemDto createdOrder = orderService.addProductToOrder(orderId,model);
        return new ResponseEntity<>(createdOrder, HttpStatus.OK);
    }
    @GetMapping("/details/{orderId}")
    public ResponseEntity<OrderDto> getOrderDetailsById(@PathVariable(name = "orderId") Long orderId)
    {
        OrderDto getOrder = orderService.getOrderDetails(orderId);
        return new ResponseEntity<>(getOrder, HttpStatus.OK);
    }
    @GetMapping("/{trackingNumber}")
    public ResponseEntity<OrderDto> getOrderDetailsByTrackingNumber(@PathVariable(name = "trackingNumber") String trackingNumber)
    {
        OrderDto getOrder = orderService.getOrderFromTrackingNumber(trackingNumber);
        return new ResponseEntity<>(getOrder, HttpStatus.OK);
    }
    @GetMapping("/invoice/{orderId}")
    public ResponseEntity<OrderDto> createOrderInvoice(@PathVariable(name = "orderId") Long orderId)
    {
        OrderDto getOrder = orderService.createInvoiceOrder(orderId);
        return new ResponseEntity<>(getOrder, HttpStatus.OK);
    }
    @GetMapping("/order-confirm/{orderId}")
    public ResponseEntity<ApiResponse> conformOrder(@Valid @PathVariable(name = "orderId") Long orderId, @RequestBody ConformOrderDto model)
    {
        Boolean orderConform = orderService.conformOrder(orderId,model);
        if(orderConform){
            return new ResponseEntity<>(new ApiResponse("Your order is conformed",true),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("Sorry your order is not conformed",false), HttpStatus.OK);
    }
    @GetMapping("/{orderId}/product/{productId}")
    public ResponseEntity<ApiResponse> removeProductFromOrder(@PathVariable(name = "orderId") Long orderId, @PathVariable(name = "productId") Long productId)
    {
        Boolean isRemoved = orderService.deleteProductFromOrder(orderId,productId);
        if(isRemoved){
            return new ResponseEntity<>(new ApiResponse("OrderItem Removed successfully",true), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ApiResponse("OrderItem failed to removed",false), HttpStatus.CREATED);

    }

}
