package com.miedo.dtodoaqui.presentation.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.utils.BitmapUtils;
import com.miedo.dtodoaqui.viewmodels.RegisterEstablishmentViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepThreeRE extends BaseFragment implements View.OnClickListener {
    private static final String TAG = StepThreeRE.class.getSimpleName();

    private static final int REQUEST_LOGO = 10;
    Bitmap fotoReal, fotoFake;

    RegisterEstablishmentViewModel viewModel;

    @BindView(R.id.et_horario)
    public TextInputEditText et_horario;

    @BindView(R.id.register_button)
    public Button registerButton;

    @BindView(R.id.spinner_location)
    public TextView spinner_location;

    @BindView(R.id.bt_cargar_imagen)
    public Button uploadButton;

    @BindView(R.id.iv_foto)
    public ImageView iv_foto;

    // Cosntructor necesario
    public StepThreeRE() {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_LOGO && resultCode == FragmentActivity.RESULT_OK) {
            if (data.getData() != null) {
                // this case will occur in case of picking image from the Gallery,
                // but not when taking picture with a camera
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());

                    if (bitmap != null) {
                        fotoReal = bitmap;
                        fotoFake = BitmapUtils.getScaledDownBitmap(bitmap, 500, true);
                        iv_foto.setImageBitmap(fotoFake);
                        new GuardarTask().execute(fotoFake);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step_three_re, container, false);

        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(requireActivity()).get(RegisterEstablishmentViewModel.class);

        spinner_location.setOnClickListener(this);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewModel.getRegisterState().setValue(RegisterEstablishmentViewModel.RegisterState.NORMAL);
        final NavController navController = NavHostFragment.findNavController(this);

        viewModel.getRegisterState().observe(getViewLifecycleOwner(),
                registerState -> {
                    switch (registerState) {
                        case TO_REGISTER:
                            viewModel.getEstablishment().setUserId(SessionManager.getInstance(requireContext()).getCurrentSession()
                                    .getId());
                            navController.navigate(R.id.register_action);
                            break;

                    }
                });

        registerButton.setOnClickListener(v -> {
            viewModel.getEstablishment().setOpeningHours(et_horario.getText().toString().trim());
            if (viewModel.validarTercerPaso()) {
                viewModel.getRegisterState().setValue(RegisterEstablishmentViewModel.RegisterState.TO_REGISTER);
            } else {
                showMessage("Asegúrate de cargar una foto e ingresar datos válidos.");
            }
        });

        uploadButton.setOnClickListener(v -> {
            pickPhoto(REQUEST_LOGO);
        });


    }

    public void pickPhoto(int requestCode) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (galleryIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(galleryIntent,
                    requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(requireContext());
        builderSingle.setIcon(R.drawable.ic_location_on_black_24dp);
        builderSingle.setTitle("Seleccionar Ubicación");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), R.layout.checked_text_item,
                viewModel.getLocationsNombres()
        );


        builderSingle.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                viewModel.getEstablishment().setLocationId(viewModel.getIndicesLocations().get(which));

                spinner_location.setText(viewModel.getLocationsNombres().get(which));
            }
        });
        builderSingle.show();
    }


    private class GuardarTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected void onPreExecute() {
            registerButton.setEnabled(false);
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

            Call<ResponseBody> call = api.uploadImage("Bearer " + SessionManager.getInstance(requireContext()).getCurrentSession().getJwt(), requestBody);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 201) {
                        try {
                            String nombreFoto = fetchResponse(response.body().string());

                            if (nombreFoto != null) {
                                viewModel.getEstablishment().setSlug(nombreFoto);
                            } else {
                                showMessageError("Ocurrió un error");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        viewModel.getEstablishment().setSlug(null);
                        showMessageError("Ocurrio un error");
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showMessageError("Ocurrio un error");
                    viewModel.getEstablishment().setSlug(null);
                }
            });
            return encoded;
        }

        @Override
        protected void onPostExecute(String s) {
            registerButton.setEnabled(true);
        }
    }

    private String fetchResponse(String string) throws JSONException {
        String retorno = null;
        retorno = new JSONObject(string).getJSONObject("data").getString("image_name");
        return retorno;

    }
}