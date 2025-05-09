package pousada.solnascente.apiPousada.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pousada.solnascente.apiPousada.model.Cliente;
import pousada.solnascente.apiPousada.repository.ClienteRepository;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

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

    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID: " + id));
    }

    @Transactional
    public Cliente alterarCliente(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = buscarClientePorId(id);

        if (clienteAtualizado.getNome() != null) {
            clienteExistente.setNome(clienteAtualizado.getNome());
        }
        if (clienteAtualizado.getEmail() != null) {
            if (!clienteExistente.getEmail().equals(clienteAtualizado.getEmail()) && clienteRepository.findByEmail(clienteAtualizado.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
            clienteExistente.setEmail(clienteAtualizado.getEmail());
        }
        if (clienteAtualizado.getCpf() != null) {
            if (!clienteExistente.getCpf().equals(clienteAtualizado.getCpf()) && clienteRepository.findByCpf(clienteAtualizado.getCpf()).isPresent()) {
                throw new IllegalArgumentException("CPF já cadastrado");
            }
            clienteExistente.setCpf(clienteAtualizado.getCpf());
        }
        if (clienteAtualizado.getTelefone() != null) {
            clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        }

        return clienteRepository.save(clienteExistente);
    }

    @Transactional
    public void desativarCliente(Long id) {
        Cliente clienteExistente = buscarClientePorId(id);
        clienteExistente.setAtivo(false);
        clienteRepository.save(clienteExistente);
    }

    public List<Cliente> listarTodosClientes() {
        return clienteRepository.findAll();
    }
}