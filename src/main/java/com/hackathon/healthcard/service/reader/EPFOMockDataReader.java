package com.hackathon.healthcard.service.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.EPFODto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

@Service
public class EPFOMockDataReader implements MockDataReader<EPFODto> {
    @Override
    public EPFODto readData(String msmeId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", msmeId, "EPFO.json").toFile();
            if (!jsonFile.exists()) {
                jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", "EPFO.json").toFile();
            }
            if (!jsonFile.exists()) return null;

            JsonNode rootNode = mapper.readTree(jsonFile);
            String fileMsmeId = rootNode.path("msmeId").asText();
            if (!msmeId.equals(fileMsmeId)) return null;

            EPFODto dto = new EPFODto();
            dto.setMsmeId(msmeId);
            
            JsonNode compliance = rootNode.path("complianceIndicators");
            dto.setOnTimeFilingRate(compliance.path("onTimeFilingRate").asDouble(0));
            dto.setMonthsFiledLast6(compliance.path("monthsFiledLast6").asInt(0));
            
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
