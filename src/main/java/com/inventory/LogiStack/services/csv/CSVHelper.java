package com.inventory.LogiStack.services.csv;

import com.inventory.LogiStack.dtos.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVHelper {

    public static String TYPE = "text/csv";
    static String[] HEADERs = {"id","name","description","unit_price","quantity","reorder_level","category_name","supplier_name"
    };

    public static boolean hasCSVFormat(MultipartFile file) {
        if(!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<ProductDto> csvToProduct(InputStream inputStream) {

        log.info("initialize import csv to database");

        //final CSVFormat csvFormat = CSVFormat.Builder.create().setHeader(HEADERs).setAllowMissingColumnNames(true).build();

        try (
                BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        ) {

            List<ProductDto> listOfProduct = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                ProductDto product = new ProductDto(
                        Long.parseLong(csvRecord.get("Id")),
                        csvRecord.get("name"),
                        csvRecord.get("description"),
                        BigDecimal.valueOf(Long.parseLong(csvRecord.get("unit_price"))),
                        Integer.parseInt(csvRecord.get("quantity")),
                        Integer.parseInt(csvRecord.get("reorder_level")),
                        Long.parseLong(csvRecord.get("category_id")),
                        Long.parseLong(csvRecord.get("supplier_id"))
                );
                listOfProduct.add(product);
            }
            log.info("import csv to database completed");
            return listOfProduct;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file" + e.getMessage());
        }
    }
}
