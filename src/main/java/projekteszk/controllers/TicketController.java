package projekteszk.controllers;

import java.util.Iterator;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projekteszk.entities.Order;
import projekteszk.entities.Ticket;
import projekteszk.repositories.OrderRepository;
import projekteszk.repositories.TicketRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketRepository ticketRepository;
    //@Autowired
    //private OrderRepository orderRepository;
    
    @GetMapping("")
    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    public ResponseEntity<Iterable<Ticket>> getAll() {
        return ResponseEntity.ok(ticketRepository.findAll());
    }
    
    @PostMapping("")
    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    public ResponseEntity<Ticket> post(@RequestBody Ticket ticket) {
        ticket.setId(null);
        return ResponseEntity.ok(ticketRepository.save(ticket));
    }
    
    @DeleteMapping("/{id}")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity delete(@PathVariable Integer id) {
        Optional<Ticket> oTicket = ticketRepository.findById(id);
        if (!oTicket.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        
        /*
        Iterable<Order> oOrder = orderRepository.findAll();
        Iterator<Order> it = oOrder.iterator();
        while(it.hasNext()){
            Order tmp = it.next();
            for(int i=0; i<tmp.getTickets().size(); ++i) {
                if(tmp.getTickets().get(i).equals(oTicket.get())){
                    tmp.getTickets().remove(i);
                    break;
                }
            }
        }
        */
            
        ticketRepository.delete(oTicket.get());
        return ResponseEntity.ok().build();
    }
}
