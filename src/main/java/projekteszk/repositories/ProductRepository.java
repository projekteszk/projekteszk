package projekteszk.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import projekteszk.entities.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    
}