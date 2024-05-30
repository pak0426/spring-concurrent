package spring.concurrent.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.concurrent.domain.Product;
import spring.concurrent.domain.ProductRepository;
import spring.concurrent.service.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping("/products")
    public void createProduct() {
        productRepository.save(new Product(1L, 100));
    }

    @GetMapping("/products/{id}/decrease")
    public void decreaseProduct(@PathVariable Long id) {
        productService.decrease(id);
    }
}
