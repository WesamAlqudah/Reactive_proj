package com.example.handler;


import com.example.Repo_1.ProductRepos;
import com.example.model_1.Product;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@AllArgsConstructor
@Component
public class ProductHandler {
//Handler Func // with Rout
    private ProductRepos productRepos;

    public Mono<ServerResponse> getAllProducts(ServerRequest request){

        Flux<Product> productFlux = productRepos.findAll();

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(productFlux, Product.class);
    }

    public Mono<ServerResponse> getProducts(ServerRequest request){

        String id= request.pathVariable("id");

        Mono<Product> productMono = this.productRepos.findById(id);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return productMono
                .flatMap(product ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(fromValue(product)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> saveProduct (ServerRequest request){

        Mono<Product> productMono = request.bodyToMono(Product.class);

        return productMono.flatMap(product ->
                ServerResponse.status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .body(productRepos.save(product), Product.class));
    }

    public Mono<ServerResponse> updateProduct (ServerRequest request){

        String id= request.pathVariable("id");

        Mono<Product> existingProductMono = this.productRepos.findById(id);
        Mono<Product> productMono = request.bodyToMono(Product.class);

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return productMono.zipWith(existingProductMono,
                (product, existingProduct) ->
                    new Product((existingProduct.getId()), product.getName(), product.getPrice())
        ).flatMap(product ->
                ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(productRepos.save(product), Product.class)
        ).switchIfEmpty(notFound);
    }

    }
