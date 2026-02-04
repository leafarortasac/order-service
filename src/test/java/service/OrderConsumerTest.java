package service;

import com.br.order_service.consumer.OrderConsumer;
import com.br.order_service.document.OrderDocument;
import com.br.order_service.mapper.OrderMapper;
import com.br.order_service.repository.OrderRepository;
import com.br.shared.contracts.model.OrderRepresentation;
import com.br.shared.contracts.model.PedidoItemRepresentation;
import com.br.shared.contracts.model.PedidoRepresentation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderConsumerTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private OrderConsumer consumer;

    @Test
    @DisplayName("Deve processar pedido, calcular total e salvar no banco")
    void deveProcessarESalvarPedidoComSucesso() {

        var mensagem = new PedidoRepresentation();
        mensagem.setId("PED-123");

        var item = new PedidoItemRepresentation();
        item.setQtde(2.0);
        item.setPrecoUnitario(50.0);
        mensagem.setItens(List.of(item));

        var docSemCalculo = new OrderDocument();
        docSemCalculo.setOrder(new OrderRepresentation());

        when(mapper.toDocument(any(PedidoRepresentation.class))).thenReturn(docSemCalculo);

        consumer.consume(mensagem);

        ArgumentCaptor<OrderDocument> captor = ArgumentCaptor.forClass(OrderDocument.class);
        verify(repository, times(1)).save(captor.capture());

        OrderDocument docSalvo = captor.getValue();

        assertEquals(100.0, docSalvo.getOrder().getTotal());
        assertEquals("CALCULADO", docSalvo.getOrder().getStatus().toString());

        verify(mapper, times(1)).toDocument(any());
    }

    @Test
    @DisplayName("Deve capturar e logar erro quando ocorrer uma exceção no processamento")
    void deveCapturarErroNoProcessamento() {

        PedidoRepresentation mensagem = new PedidoRepresentation();
        mensagem.setId("PED-ERRO");

        when(mapper.toDocument(any(PedidoRepresentation.class)))
                .thenThrow(new RuntimeException("Erro simulado no Mapper"));

        consumer.consume(mensagem);

        verify(repository, never()).save(any());

        verify(mapper, times(1)).toDocument(any());
    }
}
