package com.miedo.dtodoaqui.utils;

import com.google.android.gms.common.util.Strings;

public final class ValidatorUtils {

    /**
     * Metodo estatico para validar emails.
     *
     * @param email email a validar
     * @return true si el email es valido y false de lo contrario.
     */
    public static boolean isValidEmail(String email) {

        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static boolean isPasswordValid(String password) {
        if (password.length() < 6) return false;
        return true;
    }


}
