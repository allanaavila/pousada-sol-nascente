package pousada.solnascente.apiPousada.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pousada.solnascente.apiPousada.model.Cliente;
import pousada.solnascente.apiPousada.service.ClienteService;


@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(@Valid @RequestBody Cliente cliente) {
        Cliente clienteCadastrado = clienteService.cadastrarCliente(cliente);
        return new ResponseEntity<>(clienteCadastrado, HttpStatus.CREATED);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("API está online!");
    }
}