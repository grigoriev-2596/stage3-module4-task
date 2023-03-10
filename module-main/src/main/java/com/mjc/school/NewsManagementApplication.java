package com.mjc.school;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@EnableJpaAuditing
@EnableTransactionManagement
@EntityScan(basePackages = "com.mjc.school.repository.model")
@SpringBootApplication
public class NewsManagementApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(NewsManagementApplication.class, args);
    }

    @Bean
    public javax.validation.Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }
}

