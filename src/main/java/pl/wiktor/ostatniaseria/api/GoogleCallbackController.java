package pl.wiktor.ostatniaseria.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleCallbackController {

    //todo handle code
    @GetMapping("/Callback")
    public String handleGoogleCallback(@RequestParam("code") String code) {
        return "Callback received";
    }
}