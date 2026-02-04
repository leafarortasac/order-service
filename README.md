Order Service (Consumer & Worker) ⚙️📊

O Order Service é o componente responsável pelo processamento e persistência dos dados na arquitetura. Ele atua como um Worker, consumindo mensagens de forma assíncrona do RabbitMQ, realizando as transformações necessárias e armazenando os documentos no MongoDB Atlas.

🎯 Responsabilidades

Mensageria (Consumer): Escuta a fila pedido.queue e processa as mensagens recebidas em tempo real.
Persistência NoSQL: Gerencia o ciclo de vida dos pedidos dentro do MongoDB.
Segurança: Integração com o IAM-Service para proteção dos endpoints de consulta.
Transformação de Dados: Utiliza MapStruct para converter os contratos de integração (DTOs) em documentos de domínio.

🛠️ Tecnologias

Java 21
Spring Boot 3
Spring Data MongoDB: Persistência de documentos.
Spring AMQP: Consumo de filas RabbitMQ.
MapStruct: Mapeamento eficiente de objetos.
Lombok: Redução de código boilerplate.
Swagger/OpenAPI: Documentação da camada de consulta.

📖 Documentação da API (Swagger)
Embora sua função principal seja o consumo de mensagens, o serviço expõe endpoints para consulta dos pedidos processados. 🔗 Acesse em: http://localhost:8082/swagger-ui.html

Nota de Segurança: Este serviço exige um Token JWT válido emitido pelo IAM-Service no Header Authorization.

🔐 Configuração de Segurança e Ambiente
Para garantir a conectividade, o serviço utiliza as seguintes configurações (via variáveis de ambiente ou application.yml):

IAM Credentials: Necessárias para validação de tokens nas rotas de consulta.

MongoDB URI: String de conexão com o cluster (Atlas ou Local).

RabbitMQ Config: Endereço e credenciais para o nó de mensageria.

🔄 Fluxo de Processamento
O serviço permanece em listen na fila do RabbitMQ.

Assim que o pedido-service posta uma mensagem, o Order-Service a captura.

A mensagem é convertida para a entidade de domínio.

O pedido é persistido no MongoDB para consultas futuras e auditoria.

📦 Como rodar localmente
Certifique-se de que o Shared Contracts foi instalado no repositório local (mvn install).

Garanta que o MongoDB e o RabbitMQ estejam rodando (via Docker ou Local).

Execute o comando:

Bash
mvn spring-boot:run