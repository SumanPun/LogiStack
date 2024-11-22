package com.inventory.LogiStack.repositories;

import com.inventory.LogiStack.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select o from OrderItem o where o.order.id=?1")
    List<OrderItem> getOrdersItemsFromOrder(Long orderId);
}
