package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;
    @Value("${spring.datasource.username}")
    private String dataSourceUsername;
    @Value("${spring.datasource.password}")
    private String dataSourcePassword;
    @Value("${spring.datasource.driver}")
    private String dataSourceDriver;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;
    @Value("${spring.datasource.hikari.minimum-idle}")
    private int minimumIdle;
    @Value("${spring.datasource.hikari.connection-timeout}")
    private long connectionTimeout;
    @Value("${spring.datasource.hikari.idle-timeout}")
    private long idleTimeout;
    @Value("${spring.datasource.hikari.max-lifetime}")
    private long maxLifetime;


    @Value("${spring.datasource.hikari.data-source-properties.rewrite-batched-inserts}")
    private String rewriteBatchedInserts;
    @Value("${spring.jpa.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(dataSourceUrl);
        config.setUsername(dataSourceUsername);
        config.setPassword(dataSourcePassword);
        config.setDriverClassName(dataSourceDriver);

        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);

        config.setAutoCommit(true);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);

        config.addDataSourceProperty("reWriteBatchedInserts", rewriteBatchedInserts);

        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setFetchSize(batchSize);
        return jdbcTemplate;
    }
}
