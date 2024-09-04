package com.user.IntArea.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/*
    application.yml 의 ddl-auto = create 인 경우에만 (테이블 새로 생성시에만) data.sql 실행
 */

@Component
public class DataSqlInitializer {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    private final DataSource dataSource;

    public DataSqlInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public ApplicationRunner initializeData() {
        return args -> {
            if ("create".equals(ddlAuto)) {
                ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(new ClassPathResource("data.sql"));
                resourceDatabasePopulator.execute(dataSource);
            }
        };
    }
}