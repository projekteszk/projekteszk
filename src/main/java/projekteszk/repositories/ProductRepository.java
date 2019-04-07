package projekteszk.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import projekteszk.entities.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    public Optional<Product> findByName(String name);
    public Optional<Product> findByType(String type);
    public Optional<Product> findByManufacturer(String manufacturer);   
}