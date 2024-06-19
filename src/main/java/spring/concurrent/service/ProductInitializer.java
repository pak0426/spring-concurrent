package spring.concurrent.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spring.concurrent.domain.Product;
import spring.concurrent.domain.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductInitializer {
    private final ProductRepository productRepository;

    @PostConstruct
    public void init() {
        int batchSize = 1000;
        List<Product> products = new ArrayList<>();

        for (int i = 0; i <= 1000000; i++) {
            products.add(new Product((long) i, i));

            if (products.size() == batchSize) {
                productRepository.saveAll(products);
                products.clear();
            }
        }

        // 남은 데이터 저장
        if (!products.isEmpty()) {
            productRepository.saveAll(products);
        }
    }
}