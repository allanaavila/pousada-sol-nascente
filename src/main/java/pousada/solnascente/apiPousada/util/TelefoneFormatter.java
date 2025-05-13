package pousada.solnascente.apiPousada.util;

import org.springframework.stereotype.Component;

@Component
public class TelefoneFormatter {
    public boolean isCelularValido(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return true;
        }
        String numeroLimpo = telefone.replaceAll("[^0-9]", "");
        return numeroLimpo.matches("^\\d{2}9\\d{8}$");
    }

    public String formatCelular(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return null;
        }
        String numeroLimpo = telefone.replaceAll("[^0-9]", "");
        if (numeroLimpo.length() == 11) {
            return "(" + numeroLimpo.substring(0, 2) + ") " +
                    numeroLimpo.substring(2, 7) + "-" +
                    numeroLimpo.substring(7, 11);
        }
        return telefone;
    }
}
