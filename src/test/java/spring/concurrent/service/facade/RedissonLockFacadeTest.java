package spring.concurrent.service.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.concurrent.domain.Product;
import spring.concurrent.domain.ProductRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedissonLockFacadeTest {
    @Autowired
    private RedissonLockFacade redissonLockFacade;

    @Autowired private ProductRepository productRepository;

    @Test
    void Redisson_락_테스트() throws InterruptedException {
        // given
        Long id = productRepository.saveAndFlush(new Product(1L, 100)).getId();

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockFacade.decrease(id);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Product product = productRepository.getById(id);
        assertThat(product.getQuantity()).isEqualTo(0);

        productRepository.deleteAll();
    }
}