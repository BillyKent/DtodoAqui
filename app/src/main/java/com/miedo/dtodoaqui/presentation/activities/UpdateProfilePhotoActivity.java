package com.miedo.dtodoaqui.presentation.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;
import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.presentation.fragments.LoggedFragment;
import com.miedo.dtodoaqui.utils.BitmapUtils;
import com.miedo.dtodoaqui.utils.JSONUtils;
import com.miedo.dtodoaqui.viewmodels.ModifyProfileViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfilePhotoActivity extends BaseActivity {

    public static final String TAG = UpdateProfilePhotoActivity.class.getSimpleName();

    @BindView(R.id.profile)
    CircleImageView profile;

    @BindView(R.id.boton_cargar)
    Button botonCargar;

    @BindView(R.id.boton_guardar)
    Button botonGuardar;

    @BindView(R.id.tv_nombre)
    TextView nombre;

    private ProfileTO currentProfile;

    private static final int REQUEST_LOGO = 10;

    Bitmap fotoReal, fotoFake;

    ModifyProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_photo);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(ModifyProfileViewModel.class);
        viewModel.setJwt(SessionManager.getInstance(getApplicationContext()).getCurrentSession().getJwt());

        currentProfile = (ProfileTO) getIntent().getSerializableExtra("profile");
        nombre.setText(currentProfile.getFirstName() + " " + currentProfile.getLastName());

        if (currentProfile.getAvatarName() != null && currentProfile.getAvatarName() != "empty.png") {
            Picasso.get().load("http://35.226.8.87/" + currentProfile.getAvatarName()).into(profile);
        }

        botonCargar.setOnClickListener(v -> {
            pickPhoto(REQUEST_LOGO);
        });

        botonGuardar.setOnClickListener(v -> {
            new GuardarTask().execute(fotoFake);
        });

        viewModel.getCurrentState().observe(this, modifyProfileState ->
        {
            switch (modifyProfileState) {
                case UPDATING:
                    showMessage("Actualizando perfil");
                    break;

                case SUCCESSFUL:
                    showToastMessage("Imagen cargada correctamente");
                    setResult(LoggedFragment.UPDATE_PHOTO_OK);
                    finish();
                    break;
                case ERROR:
                    botonGuardar.setEnabled(true);
                    break;

            }

        });

    }

    public void pickPhoto(int requestCode) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(galleryIntent,
                    requestCode);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_LOGO && resultCode == FragmentActivity.RESULT_OK) {
            if (data.getData() != null) {
                // this case will occur in case of picking image from the Gallery,
                // but not when taking picture with a camera
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    if (bitmap != null) {
                        fotoReal = bitmap;
                        fotoFake = BitmapUtils.getScaledDownBitmap(bitmap, 200, false);

                        profile.setImageBitmap(fotoFake);
                        botonGuardar.setEnabled(true);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private class GuardarTask extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected void onPreExecute() {
            botonGuardar.setEnabled(false);
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.i(TAG, "encodeado xd:" + encoded);

            String jsonStringRequest = "{\n" +
                    "\t\"image\": {\n" +
                    "\t\t\"image_base64\": \"data:image/png;base64," + encoded.replace("\n", "") + "\",\n" +
                    "\t\t\"entity_id\": 1,\n" +
                    "\t\t\"entity_name\": \"profile\"\n" +
                    "\t}\n" +
                    "}";

            Log.i(TAG, "JSON REQUEST BODY:" + jsonStringRequest);

            // Creamos el RequestBody para la peticion
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonStringRequest);

            DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

            Call<ResponseBody> call = api.uploadImage("Bearer " + SessionManager.getInstance(getApplicationContext()).getCurrentSession().getJwt(), requestBody);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 201) {
                        try {
                            String nombreFoto = fetchResponse(response.body().string());

                            if (nombreFoto != null) {
                                currentProfile.setAvatarName(nombreFoto);
                                viewModel.createOrUpdateProfile(currentProfile, false);
                            } else {
                                showMessageError("Ocurrió un error");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        showMessageError("Ocurrio un error");
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showMessageError("Ocurrio un error");
                    botonGuardar.setEnabled(true);
                }
            });


            return encoded;
        }

    }

    private String fetchResponse(String string) throws JSONException {

        String retorno = null;

        retorno = new JSONObject(string).getJSONObject("data").getString("image_name");

        return retorno;

    }


}
