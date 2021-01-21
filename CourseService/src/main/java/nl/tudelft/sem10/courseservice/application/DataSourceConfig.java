package nl.tudelft.sem10.courseservice.application;

import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@SuppressWarnings("unused")
public class DataSourceConfig {

    @Autowired
    private transient Environment environment;

    @Autowired
    private transient BeanFactory beans;

    /**
     * Set up the connection to the database.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects
            .requireNonNull(environment.getProperty("jdbc.driverClassName")));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.user"));
        dataSource.setPassword(environment.getProperty("jdbc.pass"));

        return dataSource;
    }

    /**
     * Get the URL the authentication service connects to to validate tokens.
     *
     * @return the validation endpoint.
     */
    @Bean
    public String validateUrl() {
        return environment.getRequiredProperty("authservice.url");
    }

    /**
     * Get the auth service bean.
     *
     * @return the auth service.
     */
    @Bean
    public AuthService getAuthService() {
        return beans.getBean(environment.getRequiredProperty("authservice.impl"),
            AuthService.class);
    }

    /**
     * Get the course service bean.
     *
     * @return the course service.
     */
    @Bean
    public CourseService getCourseService() {
        return beans.getBean(environment.getRequiredProperty("courseservice.impl"),
            CourseService.class);
    }

    /**
     * Get the category service bean.
     *
     * @return the category service.
     */
    @Bean
    public CategoryService getCategoryService() {
        return beans.getBean(environment.getRequiredProperty("categoryservice.impl"),
            CategoryService.class);
    }
}
