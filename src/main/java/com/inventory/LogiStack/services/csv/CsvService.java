package com.inventory.LogiStack.services.csv;

import com.inventory.LogiStack.dtos.ProductDto;
import com.inventory.LogiStack.dtos.order.OrderDto;
import com.inventory.LogiStack.entity.Product;
import com.inventory.LogiStack.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CsvService {


    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepository productRepository;

    public void saveProduct(MultipartFile file) {

        log.info("logging to save");

        try {
            List<ProductDto> productDtos = CSVHelper.csvToProduct(file.getInputStream());
            List<Product> products = productDtos.stream().map((p)-> this.modelMapper.map(p, Product.class)).collect(Collectors.toList());
            log.info("before save");
            productRepository.saveAll(products);
            log.info("After save");
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

}
