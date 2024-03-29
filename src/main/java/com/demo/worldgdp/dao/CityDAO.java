package com.demo.worldgdp.dao;

import com.demo.worldgdp.dao.mapper.CityRowMapper;
import com.demo.worldgdp.model.City;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Setter
public class CityDAO {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final Integer PAGE_SIZE = 10;

    public List<City> getCities(String countryCode, Integer pageNo) {
        String query = "SELECT "
                        + " id, name, countrycode country_code, district, population "
                        + " FROM city WHERE countrycode = :code"
                        + " ORDER BY Population DESC";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", countryCode);

        if (pageNo != null) {
            Integer offset = (pageNo - 1) * PAGE_SIZE;
            params.put("offset", offset);
            params.put("size", PAGE_SIZE);
        }

        return namedParameterJdbcTemplate.query(query, params, new CityRowMapper());
    }

    public List<City> getCities(String countryCode) {
        return getCities(countryCode, null);
    }

    public City getCityDetail(Long cityId) {
        String query = "SELECT id, "
                        + " name, countrycode country_code, "
                        + " district, population "
                        + " FROM city WHERE id = :id";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", cityId);

        return namedParameterJdbcTemplate.queryForObject(query, params, new CityRowMapper());
    }

    public Long addCity(String countryCode, City city) {
        String query = "INSERT INTO city("
                        + " name, countrycode, "
                        + " district, population) "
                        + " VALUES (:name, :country_code, "
                        + " :district, :population )";
        SqlParameterSource paramSource = new MapSqlParameterSource();
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(query, paramSource, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private Map<String, Object> getMapForCity(String countryCode, City city) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("name", city.getName());
        map.put("country_code", countryCode);
        map.put("district", city.getDistrict());
        map.put("populaton", city.getPopulation());

        return map;
    }

    public void deleteCity(Long cityId) {
        String query = "DELETE FROM city WHERE id = :id";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", cityId);
        namedParameterJdbcTemplate.update(query, params);
    }

}
