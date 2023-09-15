package br.exitus.api.controller;

import br.exitus.api.constant.variable.RouteVAR;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RouteVAR.USER)
public class UserController {

    @GetMapping
    public String get() {
        return "Hello World!";
    }

}
