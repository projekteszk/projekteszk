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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projekteszk.entities.Ticket;
import projekteszk.repositories.TicketRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketRepository ticketRepository;
    
    @GetMapping("")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity<Iterable<Ticket>> getAll() {
        return ResponseEntity.ok(ticketRepository.findAll());
    }
    
    @GetMapping("/")
    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    public ResponseEntity<Ticket> getBySpot(@RequestParam(value="spot") Integer spot) {
        Optional<Ticket> oTicket = ticketRepository.findBySpot(spot);
        if (!oTicket.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        
        return ResponseEntity.ok(oTicket.get());
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
            
        ticketRepository.delete(oTicket.get());
        return ResponseEntity.ok().build();
    }
}
