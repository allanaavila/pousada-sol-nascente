package pousada.solnascente.apiPousada.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pousada.solnascente.apiPousada.controller.dto.FuncionarioDTO;
import pousada.solnascente.apiPousada.model.Funcionario;
import pousada.solnascente.apiPousada.service.FuncionarioService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1/funcionarios")
public class FuncionarioController {
    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> listarTodosFuncionarios() {
        List<FuncionarioDTO> funcionarioDTO = funcionarioService.listarTodosFuncionarios();
        return ResponseEntity.ok(funcionarioDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> buscarFuncionarioPorId(@PathVariable Long id) {
        try {
            Funcionario funcionario = funcionarioService.buscarFuncionarioPorId(id);
            return ResponseEntity.ok(funcionario.toDTO());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<FuncionarioDTO> cadastrarFuncionario(@Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        FuncionarioDTO funcionarioCadastrado = funcionarioService.cadastrarFuncionario(funcionarioDTO).toDTO();
        return new ResponseEntity<>(funcionarioCadastrado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> alterarFuncionario(@PathVariable Long id, @Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        try {
            FuncionarioDTO funcionarioAlterado = funcionarioService.alterarFuncionario(id, funcionarioDTO).toDTO();
            return ResponseEntity.ok(funcionarioAlterado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarFuncionario(@PathVariable Long id) {
        try {
            funcionarioService.desativarFuncionario(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/reativar")
    public ResponseEntity<Void> reativarFuncionario(@PathVariable Long id) {
        try {
            funcionarioService.reativarFuncionario(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
