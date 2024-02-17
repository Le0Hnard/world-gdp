package com.demo.worldgdp.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @Value("${datasource.url}")
    private String jdbcUrl;
    @Value("${datasource.user}")
    private String username;
    @Value("${datasource.password}")
    private String password;
    @Value("${datasource.driverClassName}")
    private String className;

    @Bean
    public DataSource getDataSource() {
        HikariDataSource ds = new HikariDataSource();

        ds.setJdbcUrl(this.jdbcUrl);
        ds.setUsername(this.username);
        ds.setPassword(this.password);
        ds.setDriverClassName(this.className);

        return ds;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(this.getDataSource());

        return namedParamJdbcTemplate;
    }

}
