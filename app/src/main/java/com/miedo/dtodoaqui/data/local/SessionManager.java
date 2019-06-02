package com.miedo.dtodoaqui.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

import com.miedo.dtodoaqui.data.UserTO;

public class SessionManager {

    private static final String PREFERENCE_NAME = "DTodoAquiClient";

    // String keys
    public static final String USER_NAME = "user_name";
    public static final String IS_LOGGED = "user_login";
    public static final String USER_PASSWORD = "user_pass";


    // Singleton
    private static SessionManager instance = null;

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }
    // Variables para acceder a las preferencias
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private UserTO currentUser;

    private SessionManager(Context context) {
        this.preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }


    public UserTO getCurrentUser() {
        return currentUser;
    }

    /**
     * Inicia sesion marcando el login a true
     * y seteando los datos de usuario en las preferencias
     *
     * @param user datos del login de usuario
     */
    public void StartSession(UserTO user) {
        editor.putBoolean(IS_LOGGED, true);
        editor.putString(USER_NAME, user.getUsername());
        editor.putString(USER_PASSWORD, user.getPassword());
        editor.commit();
        currentUser = user;
    }

    /**
     * Elimina los datos ingresados del usuario, si los hubiera
     * y marca el login como false
     */
    public void CloseSession() {
        editor.putBoolean(IS_LOGGED, false);
        editor.remove(USER_NAME);
        editor.remove(USER_PASSWORD);
        editor.commit();
        currentUser = null;
    }


    /**
     * Funcion que devuelve los datos del usuario en forma de un objeto @{@link UserTO}
     *
     * @return Objeto @{@link UserTO} con los datos de la sesion.
     */
    public UserTO getCurrentSession() {
        UserTO retorno = new UserTO();
        retorno.setUsername(preferences.getString(USER_NAME, ""));
        retorno.setPassword(preferences.getString(USER_PASSWORD, ""));
        return retorno;
    }

    /**
     * Verifica si hay un usuario loggeado
     *
     * @return true si la preferencia IS_LOGGED es true, false de lo contrario
     */

    public boolean isUserLogged() {
        return preferences.getBoolean(IS_LOGGED, false);
    }


}
