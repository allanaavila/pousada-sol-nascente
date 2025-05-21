package pousada.solnascente.apiPousada.service;

<<<<<<< HEAD

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pousada.solnascente.apiPousada.model.Cliente;
import pousada.solnascente.apiPousada.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrarCliente(Cliente cliente) {
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        cliente.setAtivo(true);
        return clienteRepository.save(cliente);
    }
=======
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
>>>>>>> feature/endpoint-cliente
}
