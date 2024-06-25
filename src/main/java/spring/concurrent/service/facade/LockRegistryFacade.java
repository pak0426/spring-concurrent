package spring.concurrent.service.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;
import spring.concurrent.service.ProductService;

@Service
@RequiredArgsConstructor
public class LockRegistryFacade {

    private final LockRegistry lockRegistry;
    private final ProductService productService;

    public void decrease(Long id) throws InterruptedException {
        lockRegistry.executeLocked(String.valueOf(id), () -> {
            productService.decrease(id);
        });
    }
}
