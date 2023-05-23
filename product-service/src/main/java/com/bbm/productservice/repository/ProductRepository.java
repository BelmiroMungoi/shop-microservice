package com.bbm.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bbm.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

}
