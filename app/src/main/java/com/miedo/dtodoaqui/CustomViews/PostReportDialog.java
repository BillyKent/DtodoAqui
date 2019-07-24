package com.miedo.dtodoaqui.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.data.ReportTO;
import com.miedo.dtodoaqui.model.ReportsModel;
import com.miedo.dtodoaqui.utils.CallbackUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostReportDialog extends Dialog{

    @BindView(R.id.establishment_report_message_et)
    EditText messageParm;
    @BindView(R.id.establishment_report_btn)
    MaterialButton postBtn;

    ReportsModel reportsModel = new ReportsModel();

    private int establishmentId;
    private int userId;
    public PostReportDialog(@NonNull Context context, int establishmentId, int userId) {
        super(context);
        this.establishmentId = establishmentId;
        this.userId = userId;
    }

    public PostReportDialog(@NonNull Context context, int themeResId, int establishmentId, int userId) {
        super(context, themeResId);
        this.establishmentId = establishmentId;
        this.userId = userId;
    }

    protected PostReportDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, int establishmentId, int userId) {
        super(context, cancelable, cancelListener);
        this.establishmentId = establishmentId;
        this.userId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_report_establishment);

        Window window = getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean publish = true;
                if(messageParm.getText().toString().isEmpty()){
                    messageParm.setError("Debe ingresar un mensaje");
                    publish = false;
                }

                if(publish){
                    reportsModel.PostReport(new ReportTO(0,false, establishmentId,messageParm.getText().toString(), userId)
                            , new CallbackUtils.SimpleCallback<ReportTO>() {
                                @Override
                                public void OnResult(ReportTO reportTO) {
                                    Toast.makeText(getContext(), "Denuncia publicada con éxito", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }

                                @Override
                                public void OnFailure(String response) {
                                    Toast.makeText(getContext(), "No se pudo publicar su denuncia", Toast.LENGTH_SHORT).show();
                                }

                            });
                }
            }
        });
    }
}
