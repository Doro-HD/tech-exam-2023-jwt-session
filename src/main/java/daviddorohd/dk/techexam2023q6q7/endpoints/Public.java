package daviddorohd.dk.techexam2023q6q7.endpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class Public {

    @GetMapping("/jwt")
    public String hello() {
        return "Hello Anonymous";
    }

    @GetMapping("/session")
    public String helloSession() {
        return "Hello Anonymous";
    }
}
