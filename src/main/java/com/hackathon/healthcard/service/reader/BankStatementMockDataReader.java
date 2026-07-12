package com.hackathon.healthcard.service.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.BankStatementDto;
import org.springframework.stereotype.Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.InputStream;

@Service
public class BankStatementMockDataReader implements MockDataReader<BankStatementDto> {
    @Override
    public BankStatementDto readData(String msmeId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new ClassPathResource("mockdata/" + msmeId + "/BANKSTATEMENT.json");
            if (!resource.exists()) {
                resource = new ClassPathResource("mockdata/BANKSTATEMENT.json");
            }
            if (!resource.exists()) return null;

            JsonNode rootNode;
            try (InputStream inputStream = resource.getInputStream()) {
                rootNode = mapper.readTree(inputStream);
            }
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
