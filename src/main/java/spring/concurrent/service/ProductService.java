package spring.concurrent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.concurrent.domain.Product;
import spring.concurrent.domain.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void decrease(Long id) {
        Product product = productRepository.getById(id);
        product.decrease();
        productRepository.saveAndFlush(product);
    }
}