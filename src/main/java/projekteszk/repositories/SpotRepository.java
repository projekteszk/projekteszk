package projekteszk.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import projekteszk.entities.Spot;

@Repository
public interface SpotRepository extends CrudRepository<Spot, Integer> {
    public Optional<Spot> findByName(String name);
}