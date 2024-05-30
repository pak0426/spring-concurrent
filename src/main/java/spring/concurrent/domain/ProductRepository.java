package spring.concurrent.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product getById(Long id) {
        return findById(id)
                .orElseThrow(NoSuchElementException::new);
    }
}
