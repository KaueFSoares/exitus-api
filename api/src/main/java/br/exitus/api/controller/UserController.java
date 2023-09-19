package br.exitus.api.controller;

import br.exitus.api.constant.variable.RouteVAR;
import br.exitus.api.domain.user.dto.CodeResponseDTO;
import br.exitus.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RouteVAR.USER)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping(RouteVAR.CODE)
    public ResponseEntity<CodeResponseDTO> code() {
        return ResponseEntity.ok(userService.code());
    }

    @GetMapping(RouteVAR.UPDATE_CODE)
    public ResponseEntity<CodeResponseDTO> refreshCode() {
        return ResponseEntity.ok(userService.refreshCode());
    }

}
