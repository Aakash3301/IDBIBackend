package com.hackathon.healthcard.service.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.AADto;
import org.springframework.stereotype.Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class AAMockDataReader implements MockDataReader<AADto> {
    @Override
    public AADto readData(String msmeId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new ClassPathResource("mockdata/" + msmeId + "/AA.json");
            if (!resource.exists()) {
                resource = new ClassPathResource("mockdata/AA.json");
            }
            if (!resource.exists()) return null;

            JsonNode rootNode;
            try (InputStream inputStream = resource.getInputStream()) {
                rootNode = mapper.readTree(inputStream);
            }
            AADto dto = new AADto();
            dto.setMsmeId(msmeId);
            
            JsonNode summary = rootNode.path("Account").path("Summary");
            dto.setCurrentBalance(summary.path("currentBalance").asDouble(0));
            
            JsonNode transactionsNode = rootNode.path("Account").path("Transactions").path("Transaction");
            List<AADto.Transaction> transactions = new ArrayList<>();
            if (transactionsNode.isArray()) {
                for (JsonNode txn : transactionsNode) {
                    AADto.Transaction t = new AADto.Transaction();
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
