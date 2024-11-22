package com.inventory.LogiStack.repositories;

import com.inventory.LogiStack.entity.Order;
import com.inventory.LogiStack.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select o from Order o where o.orderTrackingNumber= ?1")
    Optional<Order> getOrderDetailsFromTrackingNumber(String trackingNumber);
    @Query("select o from Order o")
    List<Order> getAllProducts();
}
