package com.hackathon.healthcard.service.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.EPFODto;
import org.springframework.stereotype.Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.InputStream;

@Service
public class EPFOMockDataReader implements MockDataReader<EPFODto> {
    @Override
    public EPFODto readData(String msmeId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new ClassPathResource("mockdata/" + msmeId + "/EPFO.json");
            if (!resource.exists()) {
                resource = new ClassPathResource("mockdata/EPFO.json");
            }
            if (!resource.exists()) return null;

            JsonNode rootNode;
            try (InputStream inputStream = resource.getInputStream()) {
                rootNode = mapper.readTree(inputStream);
            }
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
