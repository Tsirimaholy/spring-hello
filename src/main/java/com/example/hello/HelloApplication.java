package com.example.hello;

import controller.HelloController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
@ComponentScan({"com.example.hello","controller"})
public class HelloApplication {

	public static void main(String[] args) {
		new File(HelloController.uploadDirectory).mkdir();
		SpringApplication.run(HelloApplication.class, args);
	}

}
