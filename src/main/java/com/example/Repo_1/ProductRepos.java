package com.example.Repo_1;

import com.example.model_1.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepos extends ReactiveMongoRepository<Product,String> {



}
