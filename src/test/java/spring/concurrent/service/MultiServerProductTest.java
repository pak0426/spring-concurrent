package spring.concurrent.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import spring.concurrent.domain.Product;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiServerProductTest {
    @Test
    void 단일_서버_환경에서의_테스트() throws InterruptedException {
        // given
        int threadCount = 100;
        RestTemplate restTemplate = new RestTemplate();
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        CountDownLatch latch = new CountDownLatch(threadCount);

        restTemplate.getForObject("http://localhost:8080/products", Void.class);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
               try {
                   int port = 8080;
                   ResponseEntity<Void> forEntity = restTemplate
                        .getForEntity("http://localhost:" + port + "/products/1/decrease", Void.class);
               } finally {
                   latch.countDown();
               }
            });
        }
        latch.await();
    }

    @Test
    void 다중_서버_환경에서의_테스트() throws InterruptedException {
        // given
        int threadCount = 100;
        RestTemplate restTemplate = new RestTemplate();
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        CountDownLatch latch = new CountDownLatch(threadCount);


        // when
        for (int i = 0; i < threadCount; i++) {
            final int ii = i;
            executorService.submit(() -> {
                try {
                    int port = (ii % 2 == 0) ? 8080 : 8085;
                    ResponseEntity<Void> forEntity = restTemplate
                            .getForEntity("http://localhost:" + port + "/products/1/decrease", Void.class);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }
}
