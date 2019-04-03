package projekteszk.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import projekteszk.entities.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    
}