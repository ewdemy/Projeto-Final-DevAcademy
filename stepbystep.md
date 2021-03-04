# Trabalho Final DevAcademy Casa Magalhães
### API Pedido de Venda

## Requerimentos

1. Java 11

2. gradle

## Passos para o Setup

git clone https://github.com/ewdemy/Projeto-Final-DevAcademy.git

./gradlew bootRun

## Endpoints

GET /api​/v1​/pedidos​/{id} - Busca um pedido por Id.

GET /api​/v1​/pedidos​/busca​/{nomeCliente} - Lista os pedidos pelo nome ou parte do nome do cliente.

GET /api​/v1​/pedidos - Lista todos os pedidos

GET /api​/v1​/pedidos​/paginacao?=numPage={numeroDaPagina}&tamPage={totalRegistrosPorPagina} - Lista pedidos com paginação.

POST /api​/v1​/pedidos - Adiciona um pedido.

POST /api​/v1​/pedidos​/{id}​/status​/{status} - Muda o status do pedido.

PUT ​/api​/v1​/pedidos​/{id} - Atualiza um pedido.

DELETE /api​/v1​/pedidos​/{id} - Deleta um pedido.

