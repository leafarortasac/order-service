package com.br.order_service.repository;

import com.br.order_service.document.OrderDocument;
import com.br.order_service.repository.custom.OrderRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<OrderDocument, String>, OrderRepositoryCustom {

    @Query("{ 'order.id' : ?0, 'order.codfilial' : ?1 }")
    Optional<OrderDocument> getByIdAndCodFilial(
            final String id,
            final String codfilial);
}
