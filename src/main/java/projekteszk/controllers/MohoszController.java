package projekteszk.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekteszk.entities.Mohosz;
import projekteszk.repositories.MohoszRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/mohosz")
public class MohoszController {
    @Autowired
    private MohoszRepository mohoszRepository;
    
    @GetMapping("")
    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    public ResponseEntity<Iterable<Mohosz>> getAll() {
        return ResponseEntity.ok(mohoszRepository.findAll());
    }
    
    @GetMapping("/{id}")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity<Mohosz> get(@PathVariable Integer id) {
        Optional<Mohosz> oMohosz = mohoszRepository.findById(id);
        if (!oMohosz.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        
        return ResponseEntity.ok(oMohosz.get());
    }
    
}
