package com.hackathon.healthcard.service.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.UPIDto;
import org.springframework.stereotype.Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class UPIMockDataReader implements MockDataReader<UPIDto> {
    @Override
    public UPIDto readData(String msmeId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new ClassPathResource("mockdata/" + msmeId + "/UPI.json");
            if (!resource.exists()) {
                resource = new ClassPathResource("mockdata/UPI.json");
            }
            if (!resource.exists()) return null;

            JsonNode rootNode;
            try (InputStream inputStream = resource.getInputStream()) {
                rootNode = mapper.readTree(inputStream);
            }
            UPIDto dto = new UPIDto();
            dto.setMsmeId(msmeId);
            dto.setTransactionCount(rootNode.path("transactionCount").asInt(0));
            
            JsonNode transactionsNode = rootNode.path("transactions");
            List<UPIDto.Transaction> transactions = new ArrayList<>();
            if (transactionsNode.isArray()) {
                for (JsonNode txn : transactionsNode) {
                    UPIDto.Transaction t = new UPIDto.Transaction();
                    t.setType(txn.path("type").asText());
                    t.setAmount(txn.path("amount").asDouble(0));
                    transactions.add(t);
                }
            }
            dto.setTransactions(transactions);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
