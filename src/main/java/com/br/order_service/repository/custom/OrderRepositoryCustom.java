package com.br.order_service.repository.custom;

import com.br.order_service.document.OrderDocument;
import com.br.shared.contracts.model.OrderStatusRepresentation;
import org.springframework.data.domain.Page;

public interface OrderRepositoryCustom {

    Page<OrderDocument> findOrdersCustom(
            final String codfilial,
            final String pedidoOriginalId,
            final String clienteId,
            final OrderStatusRepresentation status,
            final Integer limit,
            final Boolean unPaged);
}
