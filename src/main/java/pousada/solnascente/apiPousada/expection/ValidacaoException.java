package pousada.solnascente.apiPousada.expection;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidacaoException extends RuntimeException {
    private String campo;
    private String mensagem;

    public ValidacaoException(String campo, String mensagem) {
        super(String.format("Erro de validação no campo '%s': %s", campo, mensagem));
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
