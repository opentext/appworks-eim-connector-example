/**
 * Copyright © 2018 Open Text.  All Rights Reserved.
 */
package com.appworks.eim.connector.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.appworks.eim.connector.example")
public class Config {
}
