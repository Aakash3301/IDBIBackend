package com.hackathon.healthcard.service.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.UPIDto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class UPIMockDataReader implements MockDataReader<UPIDto> {
    @Override
    public UPIDto readData(String msmeId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", msmeId, "UPI.json").toFile();
            if (!jsonFile.exists()) {
                jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", "UPI.json").toFile();
            }
            if (!jsonFile.exists()) return null;

            JsonNode rootNode = mapper.readTree(jsonFile);
            String fileMsmeId = rootNode.path("businessProfile").path("msmeId").asText();
            if (!msmeId.equals(fileMsmeId)) return null;

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
