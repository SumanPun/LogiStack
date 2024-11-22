package com.inventory.LogiStack.services.impl;

import com.inventory.LogiStack.dtos.OrderItemDto;
import com.inventory.LogiStack.dtos.order.AddProductToOrderDto;
import com.inventory.LogiStack.dtos.order.ConformOrderDto;
import com.inventory.LogiStack.dtos.order.CreateOrder;
import com.inventory.LogiStack.dtos.order.OrderDto;
import com.inventory.LogiStack.entity.Order;
import com.inventory.LogiStack.entity.OrderItem;
import com.inventory.LogiStack.entity.Product;
import com.inventory.LogiStack.entity.User;
import com.inventory.LogiStack.enums.OrderStatusEnum;
import com.inventory.LogiStack.exceptions.OutOfStockException;
import com.inventory.LogiStack.exceptions.ResourceNotFoundException;
import com.inventory.LogiStack.exceptions.UserRequiredException;
import com.inventory.LogiStack.repositories.OrderItemRepository;
import com.inventory.LogiStack.repositories.OrderRepository;
import com.inventory.LogiStack.repositories.ProductRepository;
import com.inventory.LogiStack.repositories.UserRepository;
import com.inventory.LogiStack.services.IUtility;
import com.inventory.LogiStack.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private IUtility utility;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public OrderDto createOrder() {
        String loggedInUserName = utility.getLoggedInUserName();
        if(loggedInUserName == null){
            throw new UserRequiredException("User Should be loggedIn");
        }
        User loggedUser = this.userRepository.findByEmail(loggedInUserName)
                .orElseThrow(()-> new ResourceNotFoundException("User","UserEmail : "+loggedInUserName,0));
        String trackingNumber = utility.generateUniqueString();
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setUserId(loggedUser.getId());
        order.setOrderTrackingNumber(trackingNumber);
        order.setTotalPrice(BigDecimal.valueOf(0));
        order.setOrderConfirm(false);
        this.orderRepository.save(order);
        return this.modelMapper.map(order,OrderDto.class);
    }

    @Override
    public OrderItemDto addProductToOrder(Long orderId, AddProductToOrderDto model) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order","OrderId",orderId));
        OrderItem addProductItem = new OrderItem();
        Product product = this.productRepository.findById(model.getProductId())
                        .orElseThrow(()-> new ResourceNotFoundException("Product","ProductId", model.getProductId()));
        int remainingProductQuantity = product.getQuantity();
        if(remainingProductQuantity < model.getQuantity()){
            throw new OutOfStockException("You order over quantity, Please choose less than "+remainingProductQuantity);
        }
        BigDecimal totalAmount;
        if(model.getDiscount().intValue() > 0){
            totalAmount = BigDecimal.valueOf(model.getQuantity()).multiply((product.getUnit_price().subtract(BigDecimal.valueOf((model.getDiscount())/100).multiply(product.getUnit_price()))));
        }else {
            totalAmount = BigDecimal.valueOf(model.getQuantity()).multiply(product.getUnit_price());
        }
        order.getOrderItems().add(addProductItem);
        order.setUpdatedAt(LocalDateTime.now());
        BigDecimal totalOrderAmount = order.getTotalPrice() == null ? BigDecimal.valueOf(0) : order.getTotalPrice();
        order.setTotalPrice(totalOrderAmount.add(totalAmount));
        this.orderRepository.save(order);
        addProductItem.setUnitPrice(product.getUnit_price());
        addProductItem.setTotalPrice(totalAmount);
        addProductItem.setQuantity(model.getQuantity());
        addProductItem.setDiscount(model.getDiscount());
        addProductItem.setProduct(product);
        addProductItem.setCreatedAt(LocalDateTime.now());
        addProductItem.setOrder(order);
        this.orderItemRepository.save(addProductItem);
        return this.modelMapper.map(addProductItem,OrderItemDto.class);
    }

    @Override
    public OrderDto getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order","OrderId",orderId));
        return this.modelMapper.map(order, OrderDto.class);
    }

    @Override
    public Boolean deleteProductFromOrder(Long orderId, Long productItemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order","OrderId",orderId));
        OrderItem orderItem = orderItemRepository.findById(productItemId)
                .orElseThrow(()-> new ResourceNotFoundException("OrderItem","OrderItemId",productItemId));
        order.getOrderItems().remove(orderItem);
        orderRepository.save(order);
        return true;
    }

    @Override
    public OrderDto getOrderFromTrackingNumber(String trackingNumber) {
        Order order = orderRepository.getOrderDetailsFromTrackingNumber(trackingNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Order","orderTrackingNumber : "+trackingNumber,0));
        return this.modelMapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto createInvoiceOrder(Long orderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order","OrderId",orderId));
        List<OrderItem> orderItems = this.orderItemRepository.getOrdersItemsFromOrder(orderId);
        if(!orderItems.isEmpty()){
            order.setOrderItems(orderItems);
        }
        return this.modelMapper.map(order, OrderDto.class);
    }

    @Override
    public Boolean conformOrder(Long orderId, ConformOrderDto model) {
        String loggedInUserName = utility.getLoggedInUserName();
        if(loggedInUserName == null){
            throw new UserRequiredException("User Should be loggedIn");
        }
        User loggedUser = this.userRepository.findByEmail(loggedInUserName)
                .orElseThrow(()-> new ResourceNotFoundException("User","UserEmail : "+loggedInUserName,0));
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order","OrderId",orderId));
        if (Boolean.TRUE.equals(order.getOrderConfirm()) || order.getOrderConfirm() == null) {
            return false;
        }
        if(loggedUser.getId() == order.getUserId()){
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem item : orderItems){
                Product product = this.productRepository.findById(item.getProduct().getId())
                        .orElseThrow(()-> new ResourceNotFoundException("Product","ProductId", item.getId()));
                if(product.getQuantity() < item.getQuantity()){
                    throw new OutOfStockException("OverQuantity is ordered");
                }
                product.setQuantity(product.getQuantity() - item.getQuantity());
                this.productRepository.save(product);
            }
            order.setOrderConfirm(true);
            order.setStatus(OrderStatusEnum.PENDING.name());
            LocalDateTime orderDate = utility.convertToDate(model.getDate());
            order.setOrderDate(orderDate);
            this.orderRepository.save(order);
            return true;
        }
        return false;
    }
}
