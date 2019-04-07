package projekteszk.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import projekteszk.entities.Order;
import projekteszk.entities.Ticket;
import projekteszk.entities.User;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    public Iterable<Order> findByUser(User user);
    public Iterable<Order> findByTickets(Ticket tickets);
}