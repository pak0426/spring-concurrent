package spring.concurrent.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.concurrent.domain.Product;
import spring.concurrent.domain.ProductRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Long productId;

    @BeforeEach
    void setUp() {
        productId = productRepository.saveAndFlush(new Product(1L, 100))
                .getId();
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void 재고_1개_감소_로직_성공() {
        // given
        productService.decrease(productId);

        // when
        Product product = productRepository.getById(productId);

        // then
        assertThat(product.getQuantity()).isEqualTo(99);
    }

    @Test
    void 재고를_동시에_100개_감소_시_잔여_재고_오류() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productService.decrease(productId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Product product = productRepository.getById(productId);
        assertThat(product.getQuantity()).isEqualTo(0);
    }
}