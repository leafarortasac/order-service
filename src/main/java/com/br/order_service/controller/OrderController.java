package com.br.order_service.controller;

import com.br.order_service.document.OrderDocument;
import com.br.order_service.mapper.OrderMapper;
import com.br.order_service.service.OrderService;
import com.br.shared.contracts.api.OrderApi;
import com.br.shared.contracts.model.OrderDocumentResponseRepresentation;
import com.br.shared.contracts.model.OrderStatusRepresentation;
import com.br.shared.contracts.model.PaginaRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService service;

    private final OrderMapper mapper;

    @Override
    public ResponseEntity<OrderDocumentResponseRepresentation> listOrders(
            final String codfilial,
            final String pedidoOriginalId,
            final String clienteId,
            final OrderStatusRepresentation status,
            final Integer limit,
            final Boolean unPaged) {

        Page<OrderDocument> page = service.getOrders(
                codfilial, pedidoOriginalId, clienteId, status, limit, unPaged);

        List<OrderDocument> registros = page.getContent();

        var paginaInfo = new PaginaRepresentation();
        paginaInfo.setTotalPaginas(page.getTotalPages());
        paginaInfo.setTotalElementos(page.getTotalElements());

        var response = new OrderDocumentResponseRepresentation();
        response.setRegistros(mapper.toListOrcamentoDocumentoRepresentation(registros));
        response.setPagina(paginaInfo);

        return ResponseEntity.ok(response);
    }
}
