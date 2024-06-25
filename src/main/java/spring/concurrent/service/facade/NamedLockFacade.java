package spring.concurrent.service.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.concurrent.domain.LockRepository;
import spring.concurrent.service.ProductService;

@Service
@RequiredArgsConstructor
public class NamedLockFacade {

    private final LockRepository lockRepository;
    private final ProductService productService;

    @Transactional
    public void decrease(Long id) {
        try {
            int acquiredLock = lockRepository.getLock(String.valueOf(id));

            if (acquiredLock != 1) {
                throw new RuntimeException(String.format("Lock 획득에 실패했습니다. [id: %d]", id));
            }
            productService.decrease(id);
        } finally {
            lockRepository.releaseLock(String.valueOf(id));
        }
    }
}
