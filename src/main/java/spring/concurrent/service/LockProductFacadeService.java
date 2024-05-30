package spring.concurrent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class LockProductFacadeService {

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
