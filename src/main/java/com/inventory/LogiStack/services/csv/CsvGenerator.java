package com.inventory.LogiStack.services.csv;

import com.inventory.LogiStack.entity.Order;
import com.inventory.LogiStack.entity.Product;
import com.inventory.LogiStack.repositories.OrderRepository;
import com.inventory.LogiStack.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.csv.*;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service
@Slf4j
public class CsvGenerator {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    public void writeProductToCsv(Writer writer){

        List<Product> products = productRepository.getAllProducts();
        try{
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            csvPrinter.printRecord("id","name","description","unit_price","quantity","reorder_level","category_name","supplier_name");
            for (Product product : products){
                csvPrinter.printRecord(product.getId(),product.getName(),product.getDescription(),
                        product.getUnit_price(),product.getQuantity(),product.getReorder_level(),
                        product.getCategory().getName(),product.getSupplier().getName());
            }
            csvPrinter.flush();
            csvPrinter.close();
        }catch (IOException ex){
            log.error("Error while writing product csv "+ex);
        }
    }
    public void writeOrderToCsv(Writer writer){

        List<Order> orders = orderRepository.getAllProducts();
        try{
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            csvPrinter.printRecord("id","order_date","order_confirm","tracking_number","total_price");
            for (Order order : orders){
                csvPrinter.printRecord(order.getId(),order.getOrderDate(), order.getOrderConfirm(),
                        order.getOrderTrackingNumber(),order.getTotalPrice());
            }
            csvPrinter.flush();
            csvPrinter.close();
        }catch (IOException ex){
            log.error("Error while writing order csv "+ex);
        }
    }
}
