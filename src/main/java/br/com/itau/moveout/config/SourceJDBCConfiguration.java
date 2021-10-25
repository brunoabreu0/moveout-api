package br.com.itau.moveout.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class SourceJDBCConfiguration {
    @Bean(name = "sourceDB")
    @ConfigurationProperties(prefix = "source.datasource")
    public DataSource sourceDB() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sourceDBTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("sourceDB") DataSource sourceDB) {
        return new JdbcTemplate(sourceDB);
    }
}
