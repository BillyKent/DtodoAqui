package com.miedo.dtodoaqui.presentation.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.utils.BitmapUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfilePhotoActivity extends AppCompatActivity {

    @BindView(R.id.profile)
    CircleImageView profile;

    @BindView(R.id.boton_cargar)
    Button botonCargar;

    private static final int REQUEST_LOGO = 10;

    Bitmap fotoReal, fotoFake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_photo);
        ButterKnife.bind(this);

        botonCargar.setOnClickListener(v -> {
            pickPhoto(REQUEST_LOGO);
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
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);

        }

    }
}
