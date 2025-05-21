package pousada.solnascente.apiPousada.service;

import pousada.solnascente.apiPousada.controller.dto.ClienteDTO;
import pousada.solnascente.apiPousada.model.Cliente;

import java.util.List;

public interface ClienteService {
    public Cliente cadastrarCliente(ClienteDTO clienteDTO);
    public Cliente buscarClientePorId(Long id);
    public Cliente alterarCliente(Long id, ClienteDTO clienteAtualizado);
    public void desativarCliente(Long id);
    public List<ClienteDTO> listarTodosClientes();
    public void reativarCliente(Long id);
}