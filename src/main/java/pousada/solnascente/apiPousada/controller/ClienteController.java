package pousada.solnascente.apiPousada.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pousada.solnascente.apiPousada.controller.dto.ClienteDTO;
import pousada.solnascente.apiPousada.model.Cliente;
import pousada.solnascente.apiPousada.service.ClienteService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarTodosClientes() {
        List<Cliente> clientes = clienteService.listarTodosClientes();
        List<ClienteDTO> clienteDTOs = clientes.stream()
                .map(ClienteDTO::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clienteDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarClientePorId(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.buscarClientePorId(id);
            return new ResponseEntity<>(ClienteDTO.toDTO(cliente), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> cadastrarCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente(
                clienteDTO.getNome(),
                clienteDTO.getEmail(),
                clienteDTO.getCpf(),
                clienteDTO.getTelefone(),
                true
        );
        Cliente clienteCadastrado = clienteService.cadastrarCliente(cliente);
        return new ResponseEntity<>(clienteDTO.toDTO(clienteCadastrado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> alterarCliente(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
        try {
            Cliente clienteAtualizado = new Cliente(
                    clienteDTO.getNome(),
                    clienteDTO.getEmail(),
                    clienteDTO.getCpf(),
                    clienteDTO.getTelefone(),
                    clienteDTO.isAtivo()
            );
            Cliente clienteAlterado = clienteService.alterarCliente(id, clienteAtualizado);
            return new ResponseEntity<>(clienteDTO.toDTO(clienteAlterado), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarCliente(@PathVariable Long id) {
        try {
            clienteService.desativarCliente(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}