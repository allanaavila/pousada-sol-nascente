package pousada.solnascente.apiPousada.service;

import pousada.solnascente.apiPousada.model.Cliente;

import java.util.List;

public interface ClienteService {
    public Cliente cadastrarCliente(Cliente cliente);
    public Cliente buscarClientePorId(Long id);
    public Cliente alterarCliente(Long id, Cliente clienteAtualizado);
    public void desativarCliente(Long id);
    public List<Cliente> listarTodosClientes();
}
