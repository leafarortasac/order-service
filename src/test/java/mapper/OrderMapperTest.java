package mapper;

import com.br.order_service.document.OrderDocument;
import com.br.order_service.mapper.OrderMapper;
import com.br.shared.contracts.model.PedidoItemRepresentation;
import com.br.shared.contracts.model.PedidoRepresentation;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class OrderMapperTest {

    @Autowired
    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    private final EasyRandom generator = new EasyRandom();

    @Test
    @DisplayName("Deve mapear pedido para documento e calcular subtotal dos itens")
    void deveMapearItensComSubtotal() {

        var item = new PedidoItemRepresentation();
        item.setQtde(5.0);
        item.setPrecoUnitario(10.0);

        var pedido = new PedidoRepresentation();
        pedido.setItens(List.of(item));

        var representation = mapper.toOrderRepresentation(pedido);

        assertEquals(50.0, representation.getItens().getFirst().getSubTotal());
        assertEquals(item.getQtde(), representation.getItens().getFirst().getQtde());
    }

    @Test
    @DisplayName("Deve mapear uma lista de OrderDocument para OrderDocumentRepresentation")
    void deveMapearListaDeDocumentosParaRepresentations() {

        List<OrderDocument> listaDocumentos = generator.objects(OrderDocument.class, 2)
                .toList();

        listaDocumentos.getFirst().getOrder().setId("id1");
        listaDocumentos.getLast().getOrder().setId("id2");

        var resultado = mapper.toListOrcamentoDocumentoRepresentation(listaDocumentos);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        assertEquals(listaDocumentos.getFirst().getOrder().getId(), resultado.getFirst().getOrder().getId());
        assertEquals(listaDocumentos.getLast().getOrder().getId(), resultado.getLast().getOrder().getId());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ou nula quando a entrada for nula")
    void deveLidarComListaNula() {

        var resultado = mapper.toListOrcamentoDocumentoRepresentation(null);
        assertNull(resultado);
    }
}