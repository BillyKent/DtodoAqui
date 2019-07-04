package com.miedo.dtodoaqui.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;

import butterknife.BindView;

public class PostReviewActivity extends BaseActivity {

    @BindView(R.id.my_toolbar)
    public Toolbar toolbar;

    @BindView(R.id.et_titulo)
    public TextInputEditText et_titulo;

    @BindView(R.id.et_descripcion)
    public TextInputEditText et_descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);


        toolbar.setTitle("Publica una reseña");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_review, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;

            case R.id.send_option:
                showMessage("Enviando");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
