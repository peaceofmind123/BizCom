package bizcom.bizcom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfirmationActivity extends AppCompatActivity {
    Button btnConfirm;
    EditText confirmationEditText;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        confirmationEditText = findViewById(R.id.confirmationCode);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setEnabled(false);
        btnConfirm.setBackgroundColor(ConfirmationActivity.this.getResources().getColor(R.color.graycolor));
        phone = getIntent().getStringExtra(SignupActivity.EXTRA_PHONE);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InternetCheckHelper.isInternetConnected(ConfirmationActivity.this))
                {

                }
            }
        });
        confirmationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if(TextUtils.isEmpty(s.toString()))
                    {
                        btnConfirm.setEnabled(false);
                        btnConfirm.setBackgroundColor(ConfirmationActivity.this.getResources().getColor(R.color.graycolor));
                    }
                    else
                    {

                        btnConfirm.setEnabled(true);
                        btnConfirm.setBackgroundColor(ConfirmationActivity.this.getResources().getColor(R.color.colorPrimary));
                    }

            }
        });
    }
}
