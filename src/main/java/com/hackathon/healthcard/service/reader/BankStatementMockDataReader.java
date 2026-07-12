package com.hackathon.healthcard.service.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.BankStatementDto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

@Service
public class BankStatementMockDataReader implements MockDataReader<BankStatementDto> {
    @Override
    public BankStatementDto readData(String msmeId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", msmeId, "BANKSTATEMENT.json").toFile();
            if (!jsonFile.exists()) {
                jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", "BANKSTATEMENT.json").toFile();
            }
            if (!jsonFile.exists()) return null;

            JsonNode rootNode = mapper.readTree(jsonFile);
            String fileMsmeId = rootNode.path("msmeId").asText();
            if (!msmeId.equals(fileMsmeId)) return null;

            BankStatementDto dto = new BankStatementDto();
            dto.setMsmeId(msmeId);
            dto.setClosingBalance(rootNode.path("closingBalance").asDouble(0));
            dto.setTotalDeposits(rootNode.path("totalDeposits").asDouble(0));
            dto.setTotalWithdrawals(rootNode.path("totalWithdrawals").asDouble(0));
            dto.setTransactionCount(rootNode.path("transactionCount").asInt(0));
            
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
