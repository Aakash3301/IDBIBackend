package com.hackathon.healthcard.service.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.AADto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class AAMockDataReader implements MockDataReader<AADto> {
    @Override
    public AADto readData(String msmeId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", msmeId, "AA.json").toFile();
            if (!jsonFile.exists()) {
                jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", "AA.json").toFile();
            }
            if (!jsonFile.exists()) return null;

            JsonNode rootNode = mapper.readTree(jsonFile);
            String fileMsmeId = rootNode.path("msmeId").asText();
            if (!msmeId.equals(fileMsmeId)) return null;

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
