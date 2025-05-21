package pousada.solnascente.apiPousada.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pousada.solnascente.apiPousada.controller.dto.FuncionarioDTO;
import pousada.solnascente.apiPousada.expection.ValidacaoException;
import pousada.solnascente.apiPousada.model.Funcionario;
import pousada.solnascente.apiPousada.repository.FuncionarioRepository;
import pousada.solnascente.apiPousada.util.CPFFormatter;
import pousada.solnascente.apiPousada.util.EmailFormatter;
import pousada.solnascente.apiPousada.util.TelefoneFormatter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FuncionarioServiceImpl implements FuncionarioService{

    private final FuncionarioRepository funcionarioRepository;
    private final CPFFormatter cpfFormatter;
    private final TelefoneFormatter telefoneFormatter;
    private final EmailFormatter emailFormatter;

    public FuncionarioServiceImpl(FuncionarioRepository funcionarioRepository,
                                  TelefoneFormatter telefoneFormatter,
                                  EmailFormatter emailFormatter,
                                  CPFFormatter cpfFormatter) {
        this.funcionarioRepository = funcionarioRepository;
        this.telefoneFormatter = telefoneFormatter;
        this.emailFormatter = emailFormatter;
        this.cpfFormatter = cpfFormatter;
    }


    @Override
    public Funcionario cadastrarFuncionario(FuncionarioDTO funcionarioDTO) {

        if (funcionarioRepository.findByEmail(funcionarioDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (!emailFormatter.isEmailValido(funcionarioDTO.email())) {
            throw new ValidacaoException("email", "Formato de email inválido.");
        }
        String emailFormatado = emailFormatter.formatToLowercase(funcionarioDTO.email());

        if (funcionarioRepository.findByCpf(funcionarioDTO.cpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        String cpfFormatado = cpfFormatter.formatAndValidate(funcionarioDTO.cpf());
        if (cpfFormatado == null) {
            throw new ValidacaoException("cpf", "CPF inválido.");
        }

        if (!telefoneFormatter.isCelularValido(funcionarioDTO.telefone())) {
            throw new ValidacaoException("telefone", "Telefone celular inválido.");
        }
        String telefoneFormatado = telefoneFormatter.formatCelular(funcionarioDTO.telefone());


        Funcionario funcionario = new Funcionario();
        funcionario.setNome(funcionarioDTO.nome());
        funcionario.setEmail(emailFormatado);
        funcionario.setCpf(cpfFormatado);
        funcionario.setTelefone(telefoneFormatado);
        funcionario.setCargo(funcionarioDTO.cargo());
        funcionario.setPerfil(funcionarioDTO.perfil());
        funcionario.setAtivo(true);

        return funcionarioRepository.save(funcionario);
    }

    @Override
    public Funcionario buscarFuncionarioPorId(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Funcionário não encontrado com o ID: " + id));
    }

    @Transactional
    @Override
    public Funcionario alterarFuncionario(Long id, FuncionarioDTO funcionarioAtualizado) {
        Funcionario funcionarioExistente = buscarFuncionarioPorId(id);

        if (funcionarioAtualizado.nome() != null) {
            funcionarioExistente.setNome(funcionarioAtualizado.nome());
        }

        if (funcionarioAtualizado.email() != null) {
            if (!funcionarioExistente.getEmail().equals(funcionarioAtualizado.email())
                    && funcionarioRepository.findByEmail(funcionarioAtualizado.email()).isPresent()) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
            if (!emailFormatter.isEmailValido(funcionarioAtualizado.email())) {
                throw new ValidacaoException("email", "Formato de email inválido.");
            }
            funcionarioExistente.setEmail(emailFormatter.formatToLowercase(funcionarioAtualizado.email()));
        }


        if (funcionarioAtualizado.cpf() != null) {
            if (!funcionarioExistente.getCpf().equals(funcionarioAtualizado.cpf())
                    && funcionarioRepository.findByCpf(funcionarioAtualizado.cpf()).isPresent()) {
                throw new IllegalArgumentException("CPF já cadastrado");
            }
            String cpfFormatado = cpfFormatter.formatAndValidate(funcionarioAtualizado.cpf());
            if (cpfFormatado == null) {
                throw new ValidacaoException("cpf", "CPF inválido.");
            }
            funcionarioExistente.setCpf(cpfFormatado);
        }


        if (funcionarioAtualizado.telefone() != null) {
            if (!telefoneFormatter.isCelularValido(funcionarioAtualizado.telefone())) {
                throw new ValidacaoException("telefone", "Telefone celular inválido.");
            }
            funcionarioExistente.setTelefone(telefoneFormatter.formatCelular(funcionarioAtualizado.telefone()));
        }

        return funcionarioRepository.save(funcionarioExistente);
    }

    @Transactional
    @Override
    public void desativarFuncionario(Long id) {
        Funcionario funcionarioExistente = buscarFuncionarioPorId(id);

        if (!funcionarioExistente.isAtivo()) {
            return;
        }

        funcionarioExistente.setAtivo(false);
        funcionarioRepository.save(funcionarioExistente);
    }

    @Transactional
    @Override
    public void reativarFuncionario(Long id) {
        Funcionario funcionario = buscarFuncionarioPorId(id);

        if (funcionario.isAtivo()) {
            return;
        }

        funcionario.setAtivo(true);
        funcionarioRepository.save(funcionario);
    }

    @Override
    public List<FuncionarioDTO> listarTodosFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return funcionarios.stream()
                .map(funcionario -> {
                    String cpfFormatado = cpfFormatter.formatAndValidate(funcionario.getCpf());
                    String telefoneFormatado = telefoneFormatter.formatCelular(funcionario.getTelefone());
                    String emailFormatado = emailFormatter.formatToLowercase(funcionario.getEmail());
                    return new FuncionarioDTO(
                            funcionario.getNome(),
                            emailFormatado,
                            cpfFormatado,
                            telefoneFormatado,
                            funcionario.getCargo(),
                            funcionario.getPerfil(),
                            funcionario.isAtivo()
                    );
                })
                .collect(Collectors.toList());
    }
}
