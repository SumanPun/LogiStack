package com.inventory.LogiStack.controllers.csv;

import com.inventory.LogiStack.dtos.ApiResponse;
import com.inventory.LogiStack.services.csv.CSVHelper;
import com.inventory.LogiStack.services.csv.CsvGenerator;
import com.inventory.LogiStack.services.csv.CsvService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/logistack/csv")
public class CsvController {

    @Autowired
    private CsvGenerator csvGenerator;
    @Autowired
    private CsvService csvService;

    @GetMapping("/products/export-to-csv")
    public void getAllProductsInCsv(HttpServletResponse response) throws IOException{
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition","attachment; filename=\"products.csv\"");
        csvGenerator.writeProductToCsv(response.getWriter());
    }

    @GetMapping("/orders/export-to-csv")
    public void getAllOrdersInCsv(HttpServletResponse response) throws IOException{
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition","attachment; filename=\"orders.csv\"");
        csvGenerator.writeOrderToCsv(response.getWriter());
    }
    @PostMapping("/upload-product-csv")
    public ResponseEntity<ApiResponse> uploadProductCSV(@RequestParam("file") MultipartFile file) {

        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                csvService.saveProduct(file);
                message = "uploaded file successfully" + file.getOriginalFilename();
                return  new ResponseEntity<>(new ApiResponse(message,true), HttpStatus.OK);
            } catch (Exception e) {
                message = "failed to upload file "+file.getOriginalFilename();
                return new ResponseEntity<>(new ApiResponse(message, false), HttpStatus.OK);
            }
        }
        message = "please upload CSV file";
        return  new ResponseEntity<>(new ApiResponse(message, false), HttpStatus.OK);
    }

}
