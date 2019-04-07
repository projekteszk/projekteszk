package projekteszk.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import projekteszk.entities.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    
}