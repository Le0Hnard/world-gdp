package com.demo.worldgdp.dao;

import com.demo.worldgdp.config.TestDBConfiguration;
import com.demo.worldgdp.model.City;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(classes = { TestDBConfiguration.class, CityDAO.class })
public class CityDAOTest {

    @Autowired
    CityDAO cityDao;
    @Autowired
    @Qualifier("testTemplate")
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before("")
    public void setup() {
        cityDao.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetCities() {
        List<City> cities = cityDao.getCities("IND", 1);

        assertThat(cities).hasSize(10);
    }

    @Test
    public void testGetCityDetail() {
        Long cityId = 1024l;
        City city = cityDao.getCityDetail(cityId);

        assertThat(city.toString()).isEqualTo("City(id=1024, name=Mumbai (Bombay), "
                + "countryCode=IND, country=null, district=Maharashtra,population=10500000)");
    }

    @Test
    public void testAddCity() {
        String countryCode = "IND";
        City city = new City();

        city.setCountryCode(countryCode);
        city.setDistrict("District");
        city.setName("City Name");
        city.setPopulation(101010l);

        long cityId = cityDao.addCity(countryCode, city);
        assertThat(cityId).isNotNull();

        City cityFromDb = cityDao.getCityDetail(cityId);
        assertThat(cityFromDb).isNotNull();
        assertThat(cityFromDb.getName()).isEqualTo("City Name");
    }

    @Test
    public void testDeleteCity() {
        Long cityId = addCity();
        cityDao.deleteCity(cityId);

        City cityFromDb = cityDao.getCityDetail(cityId);
        assertThat(cityFromDb).isNotNull();
    }

    private Long addCity() {
        String countryCode = "IND";
        City city = new City();

        city.setCountryCode(countryCode);
        city.setDistrict("District");
        city.setName("City Name");
        city.setPopulation(101010l);

        return cityDao.addCity(countryCode, city);
    }

}
