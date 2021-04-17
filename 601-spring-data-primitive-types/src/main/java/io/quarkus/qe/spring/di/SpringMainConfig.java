package io.quarkus.qe.spring.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringMainConfig {

    @Bean
    public BookService bookServiceGenerator() {
        return new BookServiceImpl();
    }

}
