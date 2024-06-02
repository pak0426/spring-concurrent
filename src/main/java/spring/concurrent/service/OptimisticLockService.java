package spring.concurrent.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.concurrent.domain.Product;
import spring.concurrent.domain.ProductRepository;

@Service
@RequiredArgsConstructor
public class OptimisticLockService {

    private final ProductRepository productRepository;

    @Transactional
    public void decrease(Long id) {
        Product product = productRepository.findByWithOptimisticLock(id);
        product.decrease();
        productRepository.saveAndFlush(product);
    }
}
