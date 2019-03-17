package projekteszk.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import projekteszk.entities.Club;

@Repository
public interface ClubRepository extends CrudRepository<Club, Integer> {
    public Optional<Club> findByName(String name);
}
