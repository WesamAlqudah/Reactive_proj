package com.example.Controller_1;


import com.example.Repo_1.ProductRepos;
import com.example.model_1.Product;
import com.example.model_1.ProductEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/products")
public class ProductCont {

    private ProductRepos productRepos;

    public ProductCont(ProductRepos productRepos){

        this.productRepos=productRepos;
    }

    @GetMapping
    public Flux<Product>getAllProducts(){

        return  productRepos.findAll();
    }

    @GetMapping("/{id}")
    public Mono <ResponseEntity<Product>> getAllProducts(@PathVariable String id){
//        return productRepos.findById(id)
//                .map(product -> ResponseEntity.ok(product));
        // if it's empty and avoid null:
        return productRepos.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> saveProduct(@RequestBody Product product){

        return productRepos.save(product);
    }

    @PutMapping("/{id}")
    public Mono <ResponseEntity<Product>> updateProduct(@PathVariable (value="id") String id,
                                                        @RequestBody Product product){

        return productRepos.findById(id)
                .flatMap(existingProduct ->{
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    return productRepos.save(existingProduct);
                })
                .map(updateProduct-> ResponseEntity.ok(updateProduct))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable(value= "id") String id){

        return productRepos.findById(id)
                .flatMap(existingProduct ->
                        productRepos.delete(existingProduct)
                                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<Void> deleteAllProducts(){

        return productRepos.deleteAll();
    }

    @GetMapping (value= "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductEvent> getProductEvents(){

        return Flux.interval(Duration.ofSeconds(1))
                .map( val ->
                        new ProductEvent(val, "Product Event")
                );
    }
}
