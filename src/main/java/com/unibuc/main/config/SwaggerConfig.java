package com.unibuc.main.config;
/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@EnableSwagger2
@Configuration
 */
public class SwaggerConfig {
/*
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Documentation")
                .description("Documentation for Java Project")
                .version("1.0.0")
                .build();
    }

    @Bean
    public Docket customImplementation() { //http://localhost:8080/swagger-ui/
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.unibuc.main.controller"))
                .apis(RequestHandlerSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }


    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }*/
}