package com.br.order_service.repository.custom.impl;

import com.br.order_service.document.OrderDocument;
import com.br.order_service.repository.custom.OrderRepositoryCustom;
import com.br.shared.contracts.model.OrderStatusRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<OrderDocument> findOrdersCustom(
            final String codfilial,
            final String pedidoOriginalId,
            final String clienteId,
            final OrderStatusRepresentation status,
            final Integer limit,
            final Boolean unPaged) {

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (codfilial != null) criteriaList.add(Criteria.where("codfilial").is(codfilial));
        if (pedidoOriginalId != null) criteriaList.add(Criteria.where("pedidoOriginalId").is(pedidoOriginalId));

        if (clienteId != null) criteriaList.add(Criteria.where("clienteId").is(clienteId));

        if (status != null) criteriaList.add(Criteria.where("status").is(status));

        if (!criteriaList.isEmpty()){
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "dataProcessamento");

        Pageable pageable = unPaged ? Pageable.unpaged() : PageRequest.of(0, limit, sort);

        query.with(sort);

        if (pageable.isPaged()) {
            query.with(pageable);
        }

        List<OrderDocument> list = mongoTemplate.find(query, OrderDocument.class);

        return PageableExecutionUtils.getPage(
                list,
                pageable,
                () -> mongoTemplate.count(query.skip(-1).limit(-1), OrderDocument.class));
    }
}