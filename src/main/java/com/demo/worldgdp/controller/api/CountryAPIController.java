package com.demo.worldgdp.controller.api;

import com.demo.worldgdp.dao.CountryDAO;
import com.demo.worldgdp.external.WorldBankApiClient;
import com.demo.worldgdp.model.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/countries")
@Slf4j
public class CountryAPIController {

    @Autowired
    CountryDAO countryDao;

    @Autowired
    WorldBankApiClient worldBankApiClient;

    @GetMapping
    public ResponseEntity<?> getCountries(
            @RequestParam(name = "search", required = false) String searchTerm,
            @RequestParam(name = "continent", required = false) String continent,
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "pageNo", required = false) Integer pageNo
    ) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("search", searchTerm);
            params.put("continent", continent);
            params.put("region", region);

            if (pageNo != null) {
                params.put("pageNo", pageNo.toString());
            }

            List<Country> countires = countryDao.getCountries(params);
            Map<String, Object> response = new HashMap<String, Object>();

            response.put("list", countires);
            response.put("count", countryDao.getCountriesCount(params));

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error while getting countries", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while getting countries");
        }
    }

    @PostMapping(value = "/{countryCode}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> editCountry(
            @PathVariable String countryCode,
            @Valid @RequestBody Country country
    ) {
        try {
            countryDao.editCountryDetail(countryCode, country);
            Country countryFromDb = countryDao.getCountryDetail(countryCode);

            return ResponseEntity.ok(countryFromDb);
        } catch (Exception ex) {
            log.error("Error while editing the country: {} with data: {}", countryCode, country, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while editing this country.");
        }
    }

    @GetMapping("/{countryCode}/gdp")
    public ResponseEntity<?> getGDP(@PathVariable String countryCode) {
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

}
