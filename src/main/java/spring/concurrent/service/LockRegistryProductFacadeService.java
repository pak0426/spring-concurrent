package spring.concurrent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class LockRegistryProductFacadeService {

    private final LockRegistry lockRegistry;
    private final ProductService productService;

    public void decrease(Long id) throws InterruptedException {
        lockRegistry.executeLocked(String.valueOf(id), () -> {
            productService.decrease(id);
        });
    }
}
