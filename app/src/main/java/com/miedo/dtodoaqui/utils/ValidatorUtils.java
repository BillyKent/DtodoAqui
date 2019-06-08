package com.miedo.dtodoaqui.utils;

public final class ValidatorUtils {

    /**
     * Metodo estatico para validar emails.
     *
     * @param email email a validar
     * @return true si el email es valido y false de lo contrario.
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;

        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
