package com.br.order_service.document;

import com.br.shared.contracts.model.OrderRepresentation;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "order")
@CompoundIndex(name = "idx_order_unique", def = "{'pedidoOriginalId': 1, 'codfilial': 1}", unique = true)
@CompoundIndex(name = "idx_order_query", def = "{'codfilial': 1, 'dataProcessamento': -1}")
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderDocument extends BaseDocument {

    @org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable
    private OrderRepresentation order;
}
