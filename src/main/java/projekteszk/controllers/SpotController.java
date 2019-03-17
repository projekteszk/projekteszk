package projekteszk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekteszk.repositories.SpotRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/spots")
public class SpotController {
    @Autowired
    private SpotRepository spotRepository;
}
