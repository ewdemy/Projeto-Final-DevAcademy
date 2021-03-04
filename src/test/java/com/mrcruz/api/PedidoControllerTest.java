package com.mrcruz.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrcruz.api.model.ItemPedido;
import com.mrcruz.api.model.Pedido;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
public class PedidoControllerTest {

	@Value("${server.port}")
	private int porta;
	
	private RequestSpecification requisicao;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@BeforeEach
	private void prepararRequisicao() {
	      requisicao = new RequestSpecBuilder()
	              .setBasePath("/api/v1/pedidos")
	              .setPort(porta)
	              .setAccept(ContentType.JSON)
	              .setContentType(ContentType.JSON)
	              .log(LogDetail.ALL)
	              .build();
	}
	
	@Test
	public void deveReceberOk() {
		given()
		.spec(requisicao)
		.expect()
		.statusCode(200)
		.when()
		.get();
	}
	
	@Test
	public void deveCriarUmPedido() throws JsonProcessingException{
		Pedido pedidoCriado = 
				given()
				.spec(requisicao)
				.body(objectMapper.writeValueAsString(dadoUmPedido()))
				.when()
				.post()
				.then()
				.statusCode(201)
				.extract()
				.as(Pedido.class);
		
		assertNotNull(pedidoCriado, "pedido não foi cadastrado");
		assertNotNull(pedidoCriado.getId(), "ID do pedido não gerado");
	}
	
	@Test
	public void naoDeveCriarUmPedidoSemItens() throws JsonProcessingException{
				given()
				.spec(requisicao)
				.body(objectMapper.writeValueAsString(dadoUmPedidoSemItens()))
				.when()
				.post()
				.then()
				.statusCode(400);
	}
	
	@Test
	public void naoDeveCriarUmPedidoSemDescricaodoItem() throws JsonProcessingException{
				given()
				.spec(requisicao)
				.body(objectMapper.writeValueAsString(dadoUmPedidoSemDescricaoItem()))
				.when()
				.post()
				.then()
				.statusCode(400);
	}
	
	@Test
	public void naoDeveAlterarStatus() throws JsonProcessingException{
		Pedido pedidoCadastrado = 
				given()
				.spec(requisicao)
				.body(objectMapper.writeValueAsString(dadoUmPedido()))
				.when()
				.post()
				.then()
				.statusCode(201)
				.extract()
				.as(Pedido.class);
		
		assertNotNull(pedidoCadastrado, "pedido não foi cadastrado");
		assertNotNull(pedidoCadastrado.getId(), "ID do pedido não gerado");
		
		
				given()
				.spec(requisicao)
				.when()
				.post("/"+pedidoCadastrado.getId()+"/status/ENTREGUE")
				.then()
				.statusCode(422);
	}
	
	@Test
	public void deveAlterarUmPedido() throws JsonProcessingException{
		Pedido pedidoCadastrado = 
				given()
				.spec(requisicao)
				.body(objectMapper.writeValueAsString(dadoUmPedido()))
				.when()
				.post()
				.then()
				.statusCode(201)
				.extract()
				.as(Pedido.class);
		
		assertNotNull(pedidoCadastrado, "pedido não foi cadastrado");
		assertNotNull(pedidoCadastrado.getId(), "ID do pedido não gerado");
		
		pedidoCadastrado.setNomeCliente("Patrick");

		Pedido pedidoAlterado =
				given()
				.spec(requisicao)
				.body(objectMapper.writeValueAsString(pedidoCadastrado))
				.when()
				.put("/{id}", pedidoCadastrado.getId())
				.then()
				.statusCode(200)
				.extract()
				.as(Pedido.class);
		
		assertEquals(pedidoCadastrado.getNomeCliente(), pedidoAlterado.getNomeCliente(), "Nome do cliente não foi alterado");
	}
	
	@Test
	public void deveDeletarUmPedido() throws JsonProcessingException{
		Pedido pedidoCadastrado = 
				given()
				.spec(requisicao)
				.body(objectMapper.writeValueAsString(dadoUmPedido()))
				.when()
				.post()
				.then()
				.statusCode(201)
				.extract()
				.as(Pedido.class);
		
		assertNotNull(pedidoCadastrado, "pedido não foi cadastrado");
		assertNotNull(pedidoCadastrado.getId(), "ID do pedido não gerado");
		
				given()
				.spec(requisicao)
				.when()
				.delete("/{id}", pedidoCadastrado.getId())
				.then()
				.statusCode(204);
				
				given()
				.spec(requisicao)
				.when()
				.get("/{id}", pedidoCadastrado.getId())
				.then()
				.statusCode(404);
			
	}
	
	private Pedido dadoUmPedido() {
		Pedido pedido = new Pedido();
		ItemPedido item = new ItemPedido();
		item.setDescricao("Coxinha");
		item.setPrecoUnitario(new BigDecimal(3.5));
		item.setQuantidade(5);
		
		List<ItemPedido> itens = new ArrayList<ItemPedido>();
		itens.add(item);
		

		pedido.setNomeCliente("Bob Jhon");
		pedido.setEndereco("Rua A");
		pedido.setTaxa(new BigDecimal(2.5));
		pedido.setTelefone("9999898988");
		pedido.setItens(itens);
		pedido.setValorTotalProdutos();
		pedido.setValorTotal();
		
		return pedido;
	}
	
	private Pedido dadoUmPedidoSemItens() {
		Pedido pedido = new Pedido();

		
		List<ItemPedido> itens = new ArrayList<ItemPedido>();
		
		pedido.setNomeCliente("Bob Jhon");
		pedido.setEndereco("Rua A");
		pedido.setTaxa(new BigDecimal(2.5));
		pedido.setTelefone("9999898988");
		pedido.setItens(itens);

		
		return pedido;
	}
	
	private Pedido dadoUmPedidoSemDescricaoItem() {
		Pedido pedido = new Pedido();
		ItemPedido item = new ItemPedido();
		item.setPrecoUnitario(new BigDecimal(3.5));
		item.setQuantidade(5);
		
		List<ItemPedido> itens = new ArrayList<ItemPedido>();
		itens.add(item);
		
		pedido.setNomeCliente("Bob Jhon");
		pedido.setEndereco("Rua A");
		pedido.setTaxa(new BigDecimal(2.5));
		pedido.setTelefone("9999898988");
		pedido.setItens(itens);
		pedido.setValorTotalProdutos();
		pedido.setValorTotal();
		
		return pedido;
	}
	
	
	
}
