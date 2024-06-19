package spring.concurrent.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.concurrent.domain.Product;
import spring.concurrent.domain.ProductRepository;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

@SpringBootTest
public class ProductStreamTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 순차_스트림과_병렬_스트림_비교_테스트() {
        List<Product> products = productRepository.findAll();
        System.out.println("Total products: " + products.size());

        long sequentialStartTime = System.currentTimeMillis();
        long sequentialSum = products.stream()
                .mapToInt(Product::getQuantity)
                .filter(this::isPrime)
                .sum();
        long sequentialEndTime = System.currentTimeMillis();
        System.out.println("Sequential stream result: " + sequentialSum);
        System.out.println("Sequential stream processing time: " + (sequentialEndTime - sequentialStartTime) + "ms");

        long parallelStartTime = System.currentTimeMillis();
        long parallelSum = products.parallelStream()
                .mapToInt(Product::getQuantity)
                .filter(this::isPrime)
                .sum();
        long parallelEndTime = System.currentTimeMillis();
        System.out.println("Parallel stream result: " + parallelSum);
        System.out.println("Parallel stream processing time: " + (parallelEndTime - parallelStartTime) + "ms");
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
