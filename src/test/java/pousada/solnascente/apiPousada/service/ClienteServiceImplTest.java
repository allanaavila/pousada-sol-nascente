package pousada.solnascente.apiPousada.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pousada.solnascente.apiPousada.repository.ClienteRepository;
import pousada.solnascente.apiPousada.util.CPFFormatter;
import pousada.solnascente.apiPousada.util.EmailFormatter;
import pousada.solnascente.apiPousada.util.TelefoneFormatter;

public class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CPFFormatter cpfFormatter;

    @Mock
    private TelefoneFormatter telefoneFormatter;

    @Mock
    private EmailFormatter emailFormatter;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @BeforeEach
    void setup() {
        //inicia os mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve cadastrar um cliente válido")
    void testeCadastroDoCliente() {
        //Arrange


        //Act


        //Assert
    }
}
