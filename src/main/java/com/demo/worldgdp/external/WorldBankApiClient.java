package com.demo.worldgdp.external;

import com.demo.worldgdp.model.CountryGDP;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorldBankApiClient {

    String GDP_URL = "http://api.worldbank.org/countries/%s/indicators/NY.GDP.MKTP.CD?format=json&date=2013:2023";

    public List<CountryGDP> getGDP(String countryCode) throws ParseException {
        RestTemplate worldBankRestTemplate = new RestTemplate();
        ResponseEntity<String> response = worldBankRestTemplate.getForEntity(String.format(GDP_URL, countryCode), String.class);

        // The second element is the actual data and it is an array of object
        JSONParser parser = new JSONParser();
        JSONArray responseData = (JSONArray) parser.parse(response.getBody());
        JSONArray countryDataArr = (JSONArray) responseData.get(1);

        List<CountryGDP> data = new ArrayList<CountryGDP>();
        JSONObject countryDataYearWise = null;

        for (int index = 0; index < countryDataArr.size(); index++) {
            countryDataYearWise = (JSONObject) countryDataArr.get(index);

            String valueStr = "0";

            if (countryDataYearWise.get("value") != null) {
                valueStr = countryDataYearWise.get("value").toString();
            }

            String yearStr = countryDataYearWise.get("date").toString();
            CountryGDP gdp = new CountryGDP();
            gdp.setValue(valueStr != null ? Double.valueOf(valueStr) : null);
            gdp.setYear(Short.valueOf(yearStr));
            data.add(gdp);
        }

        return data;
    }

}
