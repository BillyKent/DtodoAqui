package com.miedo.dtodoaqui.utils;

import android.util.Log;

import com.miedo.dtodoaqui.data.ProfileTO;

import org.json.JSONException;
import org.json.JSONObject;

public final class JSONUtils {

    public static final String TAG = JSONUtils.class.getSimpleName();

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


    /**
     * Retorna un objeto profile de la respuesta al momento de consultar profile
     *
     * @param response
     * @return
     */
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

            Log.i(TAG, "Objeto ProfileTO parseado : " + retorno);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;
    }

    /**
     * Obtiene el String para el request body de los endpoint para la creacion
     * y actualizacion de profile.
     *
     * @param p perfil apra actualizar o crear
     * @return String con el request body
     */
    public static String getProfileRequestBodyJSON(ProfileTO p) {
        String retorno = "";

        JSONObject body = new JSONObject();
        JSONObject prof = new JSONObject();

        try {
            prof.put("first_name", p.getFirstName());
            prof.put("last_name", p.getLastName());
            prof.put("phone", p.getPhone());
            prof.put("facebook", p.getFacebookUrl());
            prof.put("description", p.getDescription());
            prof.put("country", p.getCountry());
            prof.put("address", p.getAddress());

            body.put("profile", prof);

            retorno = body.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;
    }


}
