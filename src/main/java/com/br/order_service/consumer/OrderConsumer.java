package com.br.order_service.consumer;

import com.br.order_service.config.RabbitMQConfig;
import com.br.order_service.document.OrderDocument;
import com.br.order_service.mapper.OrderMapper;
import com.br.order_service.repository.OrderRepository;
import com.br.shared.contracts.model.OrderStatusRepresentation;
import com.br.shared.contracts.model.PedidoRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(PedidoRepresentation message) {
        log.info("Processando novo pedido: {}", message.getId());

        try {
            OrderDocument document = mapper.toDocument(message);

            double totalPedido = 0.0;
            if (message.getItens() != null) {
                totalPedido = message.getItens().stream()
                        .mapToDouble(item -> (item.getPrecoUnitario() != null && item.getQtde() != null)
                                ? item.getPrecoUnitario() * item.getQtde()
                                : 0.0)
                        .sum();
            }

            if (document.getOrder() != null) {
                document.getOrder().setTotal(totalPedido);
                document.getOrder().setStatus(OrderStatusRepresentation.CALCULADO);
                document.getOrder().setDataProcessamento(LocalDateTime.now());
            }

            repository.save(document);

            log.info("Pedido {} processado com sucesso. Total: R$ {}", message.getId(), totalPedido);

        } catch (Exception e) {
            log.error("Erro ao processar pedido {}: {}", message.getId(), e.getMessage());
        }
    }
}