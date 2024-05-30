package spring.concurrent.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    public Product(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public void decrease() {
        if (this.quantity == 0) {
            throw new RuntimeException("재고는 0 이하가 될 수 없습니다.");
        }
        this.quantity --;
    }
}
