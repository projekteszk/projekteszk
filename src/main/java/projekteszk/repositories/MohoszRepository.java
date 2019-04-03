package projekteszk.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import projekteszk.entities.Mohosz;

@Repository
public interface MohoszRepository extends CrudRepository<Mohosz, Integer> {
    
}
