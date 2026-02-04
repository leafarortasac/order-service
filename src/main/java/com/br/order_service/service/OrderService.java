package com.br.order_service.service;

import com.br.order_service.document.OrderDocument;
import com.br.order_service.repository.OrderRepository;
import com.br.shared.contracts.model.OrderStatusRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public Page<OrderDocument> getOrders(
            final String codfilial,
            final String pedidoOriginalId,
            final String clienteId,
            final OrderStatusRepresentation status,
            final Integer limit,
            final Boolean unPaged) {


        return repository.findOrdersCustom(
                codfilial, pedidoOriginalId, clienteId, status, limit, unPaged);
    }
}
