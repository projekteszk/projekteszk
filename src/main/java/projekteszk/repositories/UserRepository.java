package projekteszk.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import projekteszk.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    public Optional<User> findByName(String name);
}