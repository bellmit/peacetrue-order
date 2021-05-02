package com.github.peacetrue.order;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

/**
 * @author xiayx
 */
@SpringBootTest(classes = TestControllerOrderAutoConfiguration.class)
@ActiveProfiles({"order-controller-test", "order-service-test"})
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    @org.junit.jupiter.api.Order(10)
    void add() {
        this.client.post().uri("/orders")
                .bodyValue(OrderServiceImplTest.ADD)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(OrderVO.class).value((Consumer<OrderVO>) vo -> OrderServiceImplTest.vo = vo);
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    void queryForPage() {
        this.client.get()
                .uri("/orders?page=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.totalElements").isEqualTo(1);
    }

    @Test
    @org.junit.jupiter.api.Order(30)
    void queryForList() {
        this.client.get()
                .uri("/orders")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.size()").isEqualTo(1);
    }

    @Test
    @org.junit.jupiter.api.Order(40)
    void get() {
        this.client.get()
                .uri("/orders/{0}", OrderServiceImplTest.vo.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(OrderVO.class).isEqualTo(OrderServiceImplTest.vo);
    }


    @Test
    @org.junit.jupiter.api.Order(50)
    void modify() {
        OrderModify modify = OrderServiceImplTest.MODIFY;
        modify.setId(OrderServiceImplTest.vo.getId());
        this.client.put()
                .uri("/orders/{id}", OrderServiceImplTest.vo.getId())
                .bodyValue(modify)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Integer.class).isEqualTo(1);
    }

    @Test
    @org.junit.jupiter.api.Order(60)
    void delete() {
        this.client.delete()
                .uri("/orders/{0}", OrderServiceImplTest.vo.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Integer.class).isEqualTo(1);
    }

}
