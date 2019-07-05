package com.miedo.dtodoaqui.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

import com.miedo.dtodoaqui.data.UserTO;

public class SessionManager {

    private static final String PREFERENCE_NAME = "DTodoAquiClient";

    // String keys
    public static final String IS_LOGGED = "user_login";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "user_pass";
    public static final String USER_ID = "user_id";
    public static final String JWT_TOKEN = "jwt_token";

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
    private UserTO currentUser = null;

    private SessionManager(Context context) {
        this.preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.editor = preferences.edit();

        currentUser = getCurrentSession();
    }

    /**
     * Inicia sesion marcando el login a true
     * y seteando los datos de usuario en las preferencias
     *
     * @param user datos del login de usuario
     */
    public void startSession(UserTO user) {
        editor.putBoolean(IS_LOGGED, true);
        editor.putString(USER_NAME, user.getUsername());
        editor.putString(USER_PASSWORD, user.getPassword());
        editor.putString(USER_ID, user.getId());
        editor.putString(JWT_TOKEN, user.getJwt());
        editor.commit();
        currentUser = user;
    }

    /**
     * Elimina los datos ingresados del usuario, si los hubiera
     * y marca el login como false
     */
    public void closeSession() {
        editor.putBoolean(IS_LOGGED, false);
        editor.remove(USER_NAME);
        editor.remove(USER_PASSWORD);
        editor.remove(JWT_TOKEN);
        editor.remove(USER_ID);
        editor.commit();
        currentUser = null;
    }


    /**
     * Funcion que devuelve los datos del usuario en forma de un objeto @{@link UserTO}
     *
     * @return Objeto @{@link UserTO} con los datos de la sesion.
     */
    public UserTO getCurrentSession() {

        if (currentUser != null) return currentUser;

        currentUser = new UserTO();
        currentUser.setUsername(preferences.getString(USER_NAME, ""));
        currentUser.setPassword(preferences.getString(USER_PASSWORD, ""));
        currentUser.setId(preferences.getString(USER_ID, ""));
        currentUser.setJwt(preferences.getString(JWT_TOKEN, ""));
        return currentUser;
    }

    /**
     * Verifica si hay un usuario loggeado
     *
     * @return true si la preferencia IS_LOGGED es true, false de lo contrario
     */
    public boolean isUserLogged() {
        return preferences.getBoolean(IS_LOGGED, false);
    }

    public void setJwtToken(String jwt) {
        editor.putString(JWT_TOKEN, jwt);
        currentUser.setJwt(jwt);
        editor.commit();
    }

    public void setUserID(String id) {
        editor.putString(USER_ID, id);
        currentUser.setId(id);
        editor.commit();
    }


}
