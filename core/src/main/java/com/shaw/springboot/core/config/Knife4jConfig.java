package com.shaw.springboot.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

  @Bean
  public OpenAPI openAPI() {
    Contact contact = new Contact();
    contact.setName("Shaw996");
    contact.setEmail("shawliu996@gmail.com");
    contact.setUrl("https://github.com/Shaw996");

    return new OpenAPI().info(new Info().title("瞬间")
        .version("1.0")
        .description("moment项目接口文档")
        .contact(contact));
  }

}
