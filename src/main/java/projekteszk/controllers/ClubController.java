package projekteszk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekteszk.repositories.ClubRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/clubs")
public class ClubController {
    @Autowired
    private ClubRepository clubRepository;
}
