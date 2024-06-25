package spring.concurrent.service.facade;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import spring.concurrent.service.ProductService;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonLockFacade {

    private final RedissonClient redissonClient;
    private final ProductService productService;

    public void decrease(Long id) {
        RLock lock = redissonClient.getLock(String.valueOf(id));

        try {
            boolean acquireLock = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!acquireLock) {
                System.out.println("Lock 획득 실패");
                return;
            }
            productService.decrease(id);
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }
}
