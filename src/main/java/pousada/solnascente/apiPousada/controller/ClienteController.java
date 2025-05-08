package pousada.solnascente.apiPousada.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pousada.solnascente.apiPousada.model.Cliente;
import pousada.solnascente.apiPousada.service.ClienteService;

@RestController
@RequestMapping("clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Object> cadastrarCliente(@RequestBody @Valid Cliente cliente) {
        try {
            Cliente clienteCadastrado = clienteService.cadastrarCliente(cliente);
            return new ResponseEntity<>(clienteCadastrado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
