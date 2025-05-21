package pousada.solnascente.apiPousada.service;

import pousada.solnascente.apiPousada.controller.dto.FuncionarioDTO;
import pousada.solnascente.apiPousada.model.Funcionario;

import java.util.List;

public interface FuncionarioService {
    Funcionario cadastrarFuncionario(FuncionarioDTO funcionarioDTO);
    Funcionario buscarFuncionarioPorId(Long id);
    Funcionario alterarFuncionario(Long id, FuncionarioDTO funcionarioAtualizado);
    void desativarFuncionario(Long id);
    void reativarFuncionario(Long id);
    List<FuncionarioDTO> listarTodosFuncionarios();
}
