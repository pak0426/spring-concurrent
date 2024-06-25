package spring.concurrent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.concurrent.domain.RedisLockRepository;

@Service
@RequiredArgsConstructor
public class LettuceLockService {

    private final RedisLockRepository redisLockRepository;
    private final ProductService productService;

    public void decrease(Long id) {
        while (!redisLockRepository.lock(id)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            productService.decrease(id);
        } finally {
            redisLockRepository.unlock(id);
        }
    }
}
