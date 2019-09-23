package Server;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class ServerController {

    @RequestMapping("/")
    public String greeting() {
        return "Welcome to Durak";
    }
}
