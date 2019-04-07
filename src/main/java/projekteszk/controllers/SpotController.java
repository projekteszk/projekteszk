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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projekteszk.entities.Spot;
import projekteszk.repositories.SpotRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/spots")
public class SpotController {
    @Autowired
    private SpotRepository spotRepository;
    
    @GetMapping("/{id}")
    public ResponseEntity<Spot> getById(@PathVariable Integer id) {
        Optional<Spot> oSpot = spotRepository.findById(id);
        if (!oSpot.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        
        return ResponseEntity.ok(oSpot.get());
    }
    
    @GetMapping("/")
    public ResponseEntity<Spot> getByName(@RequestParam(value="name") String name) {
        Optional<Spot> oSpot = spotRepository.findByName(name);
        if (!oSpot.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        
        return ResponseEntity.ok(oSpot.get());
    }
       
    @GetMapping("")
    public ResponseEntity<Iterable<Spot>> getAll() {
        Iterable<Spot> spots = spotRepository.findAll();
        return ResponseEntity.ok(spots);
    }
    
    @PostMapping("")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity<Spot> post(@RequestBody Spot spot) {
        spot.setId(null);
        return ResponseEntity.ok(spotRepository.save(spot));
    }
    
    @PutMapping("/{id}")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity<Spot> put(@PathVariable Integer id, @RequestBody Spot spot) {
        Optional<Spot> oSpot = spotRepository.findById(id);
        if (!oSpot.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        spot.setId(id);
        return ResponseEntity.ok(spotRepository.save(spot));
    }
    
    @DeleteMapping("/{id}")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity delete(@PathVariable Integer id) {
        Optional<Spot> oSpot = spotRepository.findById(id);
        if (!oSpot.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
            
        spotRepository.delete(oSpot.get());
        return ResponseEntity.ok().build();
    }
}
