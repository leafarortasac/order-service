package controller;

import com.br.order_service.controller.OrderController;
import com.br.order_service.document.OrderDocument;
import com.br.order_service.mapper.OrderMapper;
import com.br.order_service.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService service;
    @Mock private OrderMapper mapper;
    @InjectMocks
    private OrderController controller;

    @Test
    @DisplayName("Deve retornar lista de pedidos paginada com sucesso")
    void deveRetornarListaPaginada() {

        OrderDocument doc = new OrderDocument();
        Page<OrderDocument> page = new PageImpl<>(List.of(doc));

        when(service.getOrders(any(), any(), any(), any(), any(), any())).thenReturn(page);
        when(mapper.toListOrcamentoDocumentoRepresentation(any())).thenReturn(new ArrayList<>());

        var response = controller.listOrders("001", null, null, null, 10, false);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(Objects.requireNonNull(response.getBody()).getPagina());
        assertEquals(1, response.getBody().getPagina().getTotalElementos());
        verify(service, times(1)).getOrders(any(), any(), any(), any(), any(), any());
    }
}