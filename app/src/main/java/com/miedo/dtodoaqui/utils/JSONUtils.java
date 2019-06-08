package com.miedo.dtodoaqui.utils;

import com.miedo.dtodoaqui.data.ProfileTO;

import org.json.JSONException;
import org.json.JSONObject;

public final class JSONUtils {

    /**
     * Funcion que retorna un {@link JSONObject} para el registro de usuario.
     *
     * @param email    Direccion email del usuario a registrar.
     * @param username Username del usuario a registrar.
     * @param password Password del usuario a registrar
     * @return JSONObject para el request body del registro de usuarios.
     */
    public static String getRegisterUserRequestJSON(String email, String username, String password) {
        JSONObject retorno = new JSONObject();
        JSONObject user = new JSONObject();

        try {
            user.put("email", email);
            user.put("username", username);
            user.put("password", password);
            user.put("password_confirmation", password);

            retorno.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retorno.toString();
    }


    /**
     * Funcion Factory que crea un {@link JSONObject} para el request body del login
     * de usuario.
     *
     * @param username Username a loguear.
     * @param password Password a loguear.
     * @return JSONObject con el formato del body para el request del login.
     */
    public static String getLoginRequestJSON(String username, String password) {
        JSONObject retorno = new JSONObject();
        try {
            retorno.put("username", username);
            retorno.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retorno.toString();
    }


    public static String getJWT(String responseBody) {
        String retorno = "";

        try {
            JSONObject response = new JSONObject(responseBody);
            retorno = response.getString("jwt");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;
    }

    public static ProfileTO getProfileFromJSONString(String response) {
        ProfileTO retorno = null;

        try {
            JSONObject rpta = new JSONObject(response);
            retorno = new ProfileTO();
            retorno.setId(rpta.getInt("id"));
            retorno.setAddress(rpta.getString("address"));
            retorno.setCountry(rpta.getString("country"));
            retorno.setDescription(rpta.getString("description"));
            retorno.setFacebookUrl(rpta.getString("facebook"));
            retorno.setFirstName(rpta.getString("first_name"));
            retorno.setLastName(rpta.getString("last_name"));
            retorno.setPhone(rpta.getString("phone"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;
    }


}
