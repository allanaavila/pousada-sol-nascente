package pousada.solnascente.apiPousada.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pousada.solnascente.apiPousada.expection.ValidacaoException;
import pousada.solnascente.apiPousada.model.Cliente;
import pousada.solnascente.apiPousada.repository.ClienteRepository;
import pousada.solnascente.apiPousada.util.CPFFormatter;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final CPFFormatter cpfFormatter;

    public ClienteServiceImpl(ClienteRepository clienteRepository, CPFFormatter cpfFormatter) {
        this.clienteRepository = clienteRepository;
        this.cpfFormatter = cpfFormatter;
    }

    @Override
    public Cliente cadastrarCliente(Cliente cliente) {
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        String cpfFormatado = cpfFormatter.formatAndValidate(cliente.getCpf());
        if (cpfFormatado == null) {
            throw new ValidacaoException("cpf", "CPF inválido.");
        }
        cliente.setCpf(cpfFormatado);
        cliente.setAtivo(true);
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID: " + id));
    }

    @Transactional
    @Override
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
            String cpfFormatado = cpfFormatter.formatAndValidate(clienteAtualizado.getCpf());
            if (cpfFormatado == null) {
                throw new ValidacaoException("cpf", "CPF inválido.");
            }
            clienteExistente.setCpf(cpfFormatado);
        }
        if (clienteAtualizado.getTelefone() != null) {
            clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        }

        return clienteRepository.save(clienteExistente);
    }

    @Transactional
    @Override
    public void desativarCliente(Long id) {
        Cliente clienteExistente = buscarClientePorId(id);
        clienteExistente.setAtivo(false);
        clienteRepository.save(clienteExistente);
    }

    @Override
    public List<Cliente> listarTodosClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        clientes.forEach(cliente -> {
            String cpfFormatado = cpfFormatter.formatAndValidate(cliente.getCpf());
            cliente.setCpf(cpfFormatado);
        });
        return clientes;
    }
}