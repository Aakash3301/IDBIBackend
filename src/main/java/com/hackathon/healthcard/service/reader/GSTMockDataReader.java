package com.hackathon.healthcard.service.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.GSTComplianceInputResDto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

@Service
public class GSTMockDataReader implements MockDataReader<GSTComplianceInputResDto> {
    @Override
    public GSTComplianceInputResDto readData(String msmeId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", msmeId, "GST.json").toFile();
            if (!jsonFile.exists()) {
                jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", "GST.json").toFile();
            }
            if (!jsonFile.exists()) return null;

            JsonNode rootNode = mapper.readTree(jsonFile);
            String fileMsmeId = rootNode.path("msmeId").asText();
            if (!msmeId.equals(fileMsmeId)) return null;

            GSTComplianceInputResDto dto = new GSTComplianceInputResDto();
            
            JsonNode history = rootNode.path("returnFilingHistory");
            int returnsExpected = history.size();
            int returnsFiled = 0;
            int delayed = 0;
            double totalDelay = 0;
            double taxExpected = 0;
            double taxPaid = 0;
            int latePayments = 0;
            
            if (history.isArray()) {
                for (JsonNode period : history) {
                    if (period.path("filingStatus").asText().startsWith("Filed")) {
                        returnsFiled++;
                    }
                    int delay = period.path("delayDays").asInt(0);
                    if (delay > 0) {
                        delayed++;
                        totalDelay += delay;
                    }
                    taxExpected += period.path("totalTaxLiability").asDouble(0);
                    taxPaid += (period.path("taxPaidCash").asDouble(0) + period.path("taxPaidITC").asDouble(0));
                    if (period.path("lateFee").asDouble(0) > 0) {
                        latePayments++;
                    }
                }
            }
            
            dto.setTotalReturnsExpected(returnsExpected);
            dto.setTotalReturnsFiled(returnsFiled);
            dto.setDelayedReturns(delayed);
            dto.setAverageDelayDays(delayed > 0 ? totalDelay / delayed : 0);
            dto.setTotalTaxExpected(taxExpected);
            dto.setTotalTaxPaid(taxPaid);
            dto.setLatePayments(latePayments);
            
            String status = rootNode.path("gstStatus").asText();
            dto.setGstActive("Active".equalsIgnoreCase(status));
            dto.setSuspended("Suspended".equalsIgnoreCase(status));
            
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
