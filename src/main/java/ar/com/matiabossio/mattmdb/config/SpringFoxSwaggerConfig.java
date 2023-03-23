package ar.com.matiabossio.mattmdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SpringFoxSwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ar.com.matiabossio.mattmdb.controller"))
                .paths(PathSelectors.any())         // OPTIONAL: select all paths
                .build()
                .apiInfo(appInfo());
    }

    private ApiInfo appInfo() {
        return new ApiInfo(
                "MatTMDB Springboot API",
                "API for registering users, creating media and adding them to user's favorites for MatTMDB App.",
                "V2",
                "",
                new Contact("Matias Abossio", "https://www.matiabossio.com.ar/", "abossio@gmail.com"),
                "GitHub Repository",
                "https://github.com/Agrossio/mattmdb_springboot_back",
                Collections.emptyList()
        );
    }

}
