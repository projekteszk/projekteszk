package projekteszk.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekteszk.repositories.UserRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import projekteszk.entities.User;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Bean
    private BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
    
    @GetMapping("/{id}")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity<User> get(@PathVariable Integer id) {
        Optional<User> oUser = userRepository.findById(id);
        if (!oUser.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        
        return ResponseEntity.ok(oUser.get());
    }
    
    @GetMapping("")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity<Iterable<User>> getAll() {
        Iterable<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity<User> getByName(@RequestParam(value="name") String name) {
        Optional<User> oUser = userRepository.findByName(name);
        if (!oUser.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        
        return ResponseEntity.ok(oUser.get());
    }
    
    @PostMapping("/register")
    public ResponseEntity<User> post(@RequestBody User user) {
        Optional<User> oUser = userRepository.findByName(user.getName());
        if (oUser.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        user.setId(null);
        user.setPass(passwordEncoder().encode(user.getPass()));
        user.setRole(oUser.get().getRole());
        return ResponseEntity.ok(userRepository.save(user));
    }
    
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody String username) {
        Optional<User> oUser = userRepository.findByName(username);
        if (!oUser.isPresent()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(oUser.get());
    }
    
    @PatchMapping("/{id}/to-admin")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity<User> patchToAdmin(@PathVariable Integer id,
                                              @RequestBody User user) {
        Optional<User> oUser = userRepository.findById(id);
        if (!oUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        user.setId(id);
        user.setName(oUser.get().getName());
        user.setPass(oUser.get().getPass());
        user.setEmail(oUser.get().getEmail());
        user.setPhone(oUser.get().getPhone());
        user.setAddress(oUser.get().getAddress());
        return ResponseEntity.ok(userRepository.save(user));
    }
    
    @PutMapping("/{id}")
    @Secured({ "ROLE_USER" })
    public ResponseEntity<User> put(@PathVariable Integer id, @RequestBody User user) {
        Optional<User> oUser = userRepository.findById(id);
        if (!oUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        user.setId(id);
        user.setRole(oUser.get().getRole());
        return ResponseEntity.ok(userRepository.save(user));
    }
    
    @DeleteMapping("/{id}")
    @Secured({ "ROLE_ADMIN" })
    public ResponseEntity delete(@PathVariable Integer id) {
        Optional<User> oUser = userRepository.findById(id);
        if (!oUser.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
            
        userRepository.delete(oUser.get());
        return ResponseEntity.ok().build();
    }
}
