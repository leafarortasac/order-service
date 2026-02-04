package com.br.order_service.mapper;

import com.br.order_service.document.OrderDocument;
import com.br.shared.contracts.model.*;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true)
)
public interface OrderMapper {

    @Mapping(target = "order", source = "pedido")
    OrderDocument toDocument(PedidoRepresentation pedido);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedidoOriginalId", source = "id")
    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "itens", source = "itens", qualifiedByName = "mapPedidoItens")
    OrderRepresentation toOrderRepresentation(PedidoRepresentation pedido);

    @Named("mapPedidoItens")
    default List<OrderItemRepresentation> mapPedidoItens(List<PedidoItemRepresentation> itens) {
        if (itens == null) return null;
        return itens.stream().map(item -> {
            OrderItemRepresentation orderItem = new OrderItemRepresentation();
            orderItem.setId(item.getId());
            orderItem.setItem(item.getItem());
            orderItem.setProdutoId(item.getProduto() != null ? item.getProduto().getId() : null);
            orderItem.setQtde(item.getQtde());
            orderItem.setPrecoUnitario(item.getPrecoUnitario());
            orderItem.setCodfilial(item.getCodfilial());

            if (item.getQtde() != null && item.getPrecoUnitario() != null) {
                orderItem.setSubTotal(item.getQtde() * item.getPrecoUnitario());
            }
            return orderItem;
        }).collect(Collectors.toList());
    }

    List<OrderDocumentRepresentation> toListOrcamentoDocumentoRepresentation(List<OrderDocument> list);
}