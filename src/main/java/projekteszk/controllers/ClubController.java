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
import projekteszk.entities.Club;
import projekteszk.repositories.ClubRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/clubs")
public class ClubController {
    @Autowired
    private ClubRepository clubRepository;
    
    @GetMapping("/{id}")
    public ResponseEntity<Club> getById(@PathVariable Integer id) {
        Optional<Club> oClub = clubRepository.findById(id);
        if (!oClub.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        
        return ResponseEntity.ok(oClub.get());
    }
    
    @GetMapping("/")
    public ResponseEntity<Club> getByName(@RequestParam(value="name") String name) {
        Optional<Club> oClub = clubRepository.findByName(name);
        if (!oClub.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        
        return ResponseEntity.ok(oClub.get());
    }
    
    @GetMapping("")
    public ResponseEntity<Iterable<Club>> getAll() {
        Iterable<Club> clubs = clubRepository.findAll();
        return ResponseEntity.ok(clubs);
    }
    
    @PostMapping("")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity<Club> post(@RequestBody Club club) {
        club.setId(null);
        return ResponseEntity.ok(clubRepository.save(club));
    }
    
    @PutMapping("/{id}")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity<Club> put(@PathVariable Integer id, @RequestBody Club club) {
        Optional<Club> oClub = clubRepository.findById(id);
        if (!oClub.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        club.setId(id);
        return ResponseEntity.ok(clubRepository.save(club));
    }
    
    @DeleteMapping("/{id}")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity delete(@PathVariable Integer id) {
        Optional<Club> oClub = clubRepository.findById(id);
        if (!oClub.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
            
        clubRepository.delete(oClub.get());
        return ResponseEntity.ok().build();
    }
}
