package spring.concurrent.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LockRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT GET_LOCK(:key, 300)", nativeQuery = true)
    Integer getLock(String key);

    @Query(value = "SELECT RELEASE_LOCK(:key)", nativeQuery = true)
    Integer releaseLock(String key);
}
