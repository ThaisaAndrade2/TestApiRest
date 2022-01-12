package modulos.produtos;

import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;


@DisplayName("Testes de API Rest do modulo de Produto")

public class ProdutoTest {
    private String token;

    @BeforeEach
    public void beforeEach() {
        // Configurando os dados da API Rest da Lojinha
        baseURI = "http://165.227.93.41";
        //port = 8080;
        basePath = "/lojinha";

        // Obter o token do usuario admin


        this.token = given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.requestComUsuarioAdm())
                .when()
                .post("/v2/login")
                .then()
                .extract()
                .path("data.token");
        //System.out.println(token);
    }

    @Test
    @DisplayName("Validar os limites proibidos do valor do produto igual 0.00")
    public void testValidarLimiteProibidoValorProduto01() {
        // Tentar inserir um produto com valor 0.00 e validar mensagem de erro foi apresentada e o
        // staus code retornado foi 422

        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body(ProdutoDataFactory.criarProdutoComumComOValorIgualA(0.00))
                .when()
                .post("/v2/produtos")
                .then()
                .assertThat()
                .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                .statusCode(422);
    }

    @Test
    @DisplayName("Validar os limites proibidos do valor do produto igual a 7000.01")
    public void testValidarLimiteProibidoValorProduto02() {
        // Tentar inserir um produto com valor 7001.00 e validar mensagem de erro foi apresentada e o
        // staus code retornado foi 422

        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body(ProdutoDataFactory.criarProdutoComumComOValorIgualA(7000.01))
                .when()
                .post("/v2/produtos")
                .then()
                .assertThat()
                .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                .statusCode(422);
    }
}
