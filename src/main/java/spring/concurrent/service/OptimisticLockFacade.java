package spring.concurrent.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockFacade {

    private final OptimisticLockService optimisticLockService;

    public void decrease(Long id) {
        while (true) {
            try {
                optimisticLockService.decrease(id);
                break;
            } catch (Exception e) {
                try {
                    Thread.sleep(50);
                } catch (StaleObjectStateException | InterruptedException ex) {
                    System.out.println("ERROR!!!");
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
