package spring.concurrent.service.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.concurrent.service.ProductService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class LockFacade {

    private ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

    private final ProductService productService;

    public void decrease(Long id) throws InterruptedException {
        Lock lock = locks.computeIfAbsent(String.valueOf(id), key -> new ReentrantLock());
        boolean acquiredLock = lock.tryLock(3, TimeUnit.SECONDS);
        if (!acquiredLock) {
            throw new RuntimeException("Lock 획득 실패");
        }
        try {
            productService.decrease(id);
        } finally {
            lock.unlock();
        }
    }
}
