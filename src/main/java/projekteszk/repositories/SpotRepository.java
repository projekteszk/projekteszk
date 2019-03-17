package projekteszk.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import projekteszk.entities.Spot;

@Repository
public interface SpotRepository extends CrudRepository<Spot, Integer> {
    
}