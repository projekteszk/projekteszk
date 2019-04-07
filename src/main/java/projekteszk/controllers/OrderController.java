package projekteszk.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekteszk.entities.Order;
import projekteszk.entities.User;
import projekteszk.repositories.OrderRepository;
import projekteszk.repositories.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity<Iterable<Order>> getAll() {
        return ResponseEntity.ok(orderRepository.findAll());
    }
    
    @GetMapping("/{username}")
    @Secured({ "ROLE_USER" })
    public ResponseEntity<Iterable<Order>> get(@PathVariable String username) {
        Optional<User> oUser = userRepository.findByName(username);
        if (!oUser.isPresent()) {
            System.out.println("-----");
            System.out.println(username + " not found");
            System.out.println("-----");
            return ResponseEntity.badRequest().build();
        }
        Iterable<Order> orders = orderRepository.findByUser(oUser.get());
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping("")
    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    public ResponseEntity<Order> post(@RequestBody Order order) {
        order.setId(null);
        return ResponseEntity.ok(orderRepository.save(order));
    }
    
    @DeleteMapping("/{id}")
    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    public ResponseEntity delete(@PathVariable Integer id) {
        Optional<Order> oOrder = orderRepository.findById(id);
        if (!oOrder.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
            
        orderRepository.delete(oOrder.get());
        return ResponseEntity.ok().build();
    }
}
