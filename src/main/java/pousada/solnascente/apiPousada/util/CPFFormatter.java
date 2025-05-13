package pousada.solnascente.apiPousada.util;

import org.springframework.stereotype.Component;

@Component
public class CPFFormatter {
    public String formatAndValidate(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return null;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return null;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return null;
        }

        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Integer.parseInt(String.valueOf(cpf.charAt(i)));
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += digits[i] * (10 - i);
        }
        int remainder = sum % 11;
        int firstDigit = (remainder < 2) ? 0 : (11 - remainder);

        if (digits[9] != firstDigit) {
            return null;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += digits[i] * (11 - i);
        }
        remainder = sum % 11;
        int secondDigit = (remainder < 2) ? 0 : (11 - remainder);

        if (digits[10] != secondDigit) {
            return null;
        }

        return cpf.substring(0, 3) + "." +
                cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" +
                cpf.substring(9, 11);
    }
}
