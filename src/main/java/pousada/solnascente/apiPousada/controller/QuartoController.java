package pousada.solnascente.apiPousada.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pousada.solnascente.apiPousada.controller.dto.QuartoDTO;
import pousada.solnascente.apiPousada.service.QuartoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/quartos")
public class QuartoController {

    private final QuartoService quartoService;

    public QuartoController(QuartoService quartoService) {
        this.quartoService = quartoService;
    }

    @PostMapping
    public ResponseEntity<QuartoDTO> cadastrar(@RequestBody QuartoDTO quartoDTO) {
        QuartoDTO criado = quartoService.cadastrarQuarto(quartoDTO);
        return ResponseEntity.created(URI.create("/v1/quartos/" + criado.numeroQuarto())).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuartoDTO> buscarPorId(@PathVariable Long id) {
        QuartoDTO quarto = quartoService.buscarPorId(id);
        return ResponseEntity.ok(quarto);
    }

    @GetMapping
    public ResponseEntity<List<QuartoDTO>> listarTodos() {
        List<QuartoDTO> quartos = quartoService.listarTodos();
        return ResponseEntity.ok(quartos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuartoDTO> alterar(@PathVariable Long id, @RequestBody QuartoDTO quartoDTO) {
        QuartoDTO atualizado = quartoService.alterarQuarto(id, quartoDTO);
        return ResponseEntity.ok(atualizado);
    }

    @PutMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        quartoService.desativarQuarto(id);
        return ResponseEntity.noContent().build();
    }
}
