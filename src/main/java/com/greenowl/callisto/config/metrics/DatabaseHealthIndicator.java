package com.greenowl.callisto.config.metrics;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * SpringBoot Actuator HealthIndicator check for the Database.
 */
public class DatabaseHealthIndicator extends AbstractHealthIndicator {

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;


    private static final String TEST_QUERY = "SELECT 1";

    private String query = null;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        String query = detectQuery();
        if (StringUtils.hasText(query)) {
            try {
                builder.withDetail("hello",
                    this.jdbcTemplate.queryForObject(query, Object.class));
            } catch (Exception ex) {
                builder.down(ex);
            }
        }
    }

    private String getProduct() {
        return this.jdbcTemplate.execute(new ConnectionCallback<String>() {
            @Override
            public String doInConnection(Connection connection) throws SQLException,
                DataAccessException {

                return connection.getMetaData().getDatabaseProductName();
            }
        });
    }

    protected String detectQuery() {
        return TEST_QUERY;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
