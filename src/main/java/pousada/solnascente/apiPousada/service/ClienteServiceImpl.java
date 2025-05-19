package pousada.solnascente.apiPousada.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pousada.solnascente.apiPousada.controller.dto.ClienteDTO;
import pousada.solnascente.apiPousada.expection.ValidacaoException;
import pousada.solnascente.apiPousada.model.Cliente;
import pousada.solnascente.apiPousada.repository.ClienteRepository;
import pousada.solnascente.apiPousada.util.CPFFormatter;
import pousada.solnascente.apiPousada.util.EmailFormatter;
import pousada.solnascente.apiPousada.util.TelefoneFormatter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final CPFFormatter cpfFormatter;
    private final TelefoneFormatter telefoneFormatter;
    private final EmailFormatter emailFormatter;

    public ClienteServiceImpl(ClienteRepository clienteRepository, CPFFormatter cpfFormatter, TelefoneFormatter telefoneFormatter, EmailFormatter emailFormatter) {
        this.clienteRepository = clienteRepository;
        this.cpfFormatter = cpfFormatter;
        this.telefoneFormatter = telefoneFormatter;
        this.emailFormatter = emailFormatter;
    }

    @Override
    public Cliente cadastrarCliente(ClienteDTO clienteDTO ) {
        if (clienteRepository.findByEmail(clienteDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (!emailFormatter.isEmailValido(clienteDTO.email())) {
            throw new ValidacaoException("email", "Formato de email inválido.");
        }
        String emailFormatado = emailFormatter.formatToLowercase(clienteDTO.email());

        if (clienteRepository.findByCpf(clienteDTO.cpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        String cpfFormatado = cpfFormatter.formatAndValidate(clienteDTO.cpf());
        if (cpfFormatado == null) {
            throw new ValidacaoException("cpf", "CPF inválido.");
        }

        if (!telefoneFormatter.isCelularValido(clienteDTO.telefone())) {
            throw new ValidacaoException("telefone", "Telefone celular inválido.");
        }
        String telefoneFormatado = telefoneFormatter.formatCelular(clienteDTO.telefone());

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.nome());
        cliente.setEmail(emailFormatado);
        cliente.setCpf(cpfFormatado);
        cliente.setTelefone(telefoneFormatado);
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
    public Cliente alterarCliente(Long id, ClienteDTO clienteAtualizado) {
        Cliente clienteExistente = buscarClientePorId(id);

        if (clienteAtualizado.nome() != null) {
            clienteExistente.setNome(clienteAtualizado.nome());
        }

        if (clienteAtualizado.email() != null) {
            if (!clienteExistente.getEmail().equals(clienteAtualizado.email())
                    && clienteRepository.findByEmail(clienteAtualizado.email()).isPresent()) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
            if (!emailFormatter.isEmailValido(clienteAtualizado.email())) {
                throw new ValidacaoException("email", "Formato de email inválido.");
            }
            clienteExistente.setEmail(emailFormatter.formatToLowercase(clienteAtualizado.email()));
        }

        if (clienteAtualizado.cpf() != null) {
            if (!clienteExistente.getCpf().equals(clienteAtualizado.cpf())
                    && clienteRepository.findByCpf(clienteAtualizado.cpf()).isPresent()) {
                throw new IllegalArgumentException("CPF já cadastrado");
            }
            String cpfFormatado = cpfFormatter.formatAndValidate(clienteAtualizado.cpf());
            if (cpfFormatado == null) {
                throw new ValidacaoException("cpf", "CPF inválido.");
            }
            clienteExistente.setCpf(cpfFormatado);
        }

        if (clienteAtualizado.telefone() != null) {
            if (!telefoneFormatter.isCelularValido(clienteAtualizado.telefone())) {
                throw new ValidacaoException("telefone", "Telefone celular inválido.");
            }
            clienteExistente.setTelefone(telefoneFormatter.formatCelular(clienteAtualizado.telefone()));
        }

        return clienteRepository.save(clienteExistente);
    }

    @Transactional
    @Override
    public void desativarCliente(Long id) {
        Cliente clienteExistente = buscarClientePorId(id);

        if (!clienteExistente.isAtivo()) {
            return;
        }

        clienteExistente.setAtivo(false);
        clienteRepository.save(clienteExistente);
    }

    @Override
    public List<ClienteDTO> listarTodosClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> {
                    String cpfFormatado = cpfFormatter.formatAndValidate(cliente.getCpf());
                    String telefoneFormatado = telefoneFormatter.formatCelular(cliente.getTelefone());
                    String emailFormatado = emailFormatter.formatToLowercase(cliente.getEmail());
                    return new ClienteDTO(
                            cliente.getNome(),
                            cpfFormatado,
                            emailFormatado,
                            telefoneFormatado,
                            cliente.isAtivo()
                    );
                })
                .collect(Collectors.toList());
    }

}