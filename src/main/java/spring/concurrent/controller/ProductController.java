package spring.concurrent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.concurrent.domain.Product;
import spring.concurrent.domain.ProductRepository;
import spring.concurrent.service.facade.OptimisticLockFacade;
import spring.concurrent.service.PessimisticLockService;
import spring.concurrent.service.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    private final PessimisticLockService pessimisticLockService;
    private final OptimisticLockFacade optimisticLockFacade;

    @GetMapping("/products")
    public void createProduct() {
        productRepository.save(new Product(1L, 100));
    }

    @GetMapping("products/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.getById(id);
    }

    @GetMapping("/products/{id}/decrease")
    public void decreaseProduct(@PathVariable Long id) {
        optimisticLockFacade.decrease(id);
    }
}
