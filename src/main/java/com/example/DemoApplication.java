package com.example;

import com.example.Repo_1.ProductRepos;
import com.example.model_1.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	//----------1
	@Bean
	CommandLineRunner init (ProductRepos productRepos){
		return args -> {

			Flux<Product> productFlux= Flux.just(
					new Product(null,"Big Latte",2.99),
					new Product(null,"Big Decaf",2.49),
					new Product(null,"Big Tea",1.99))
//					.flatMap(p-> productRepos.save(p))// first way
					.flatMap(productRepos::save);//sec way
			// after save them ( just to make sure save operation has finished
			// then vs thenMany -> thenmany take any publisher flux or mono / then take no parameters
			productFlux
					 .thenMany(productRepos.findAll())// to find them to print
					.subscribe(System.out::println);

		};
	}
	//----------1

}
