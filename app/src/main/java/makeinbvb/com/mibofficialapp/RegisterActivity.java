package makeinbvb.com.mibofficialapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_name;
    private EditText et_email;
    private EditText et_phone_number;
    private EditText et_password;
    private EditText et_con_password;
    private FirebaseAuth firebaseAuth;
    private String TAG = "RegisterActivity";
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_name = findViewById(R.id.et_name_reg);
        et_email = findViewById(R.id.et_email_reg);
        et_phone_number = findViewById(R.id.et_phone_number);
        et_password = findViewById(R.id.et_password_reg);
        et_con_password = findViewById(R.id.et_con_password_reg);

        firebaseAuth = FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);

        awesomeValidation.addValidation(this, R.id.et_name_reg, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.et_email_reg, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.et_phone_number, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.et_password_reg, "((?=.*\\d)((?=.*[a-z])|(?=.*[A-Z]))(?=.*[@#$%]).{8,20})", R.string.passworderror);
        awesomeValidation.addValidation(this, R.id.et_con_password_reg, "((?=.*\\d)((?=.*[a-z])|(?=.*[A-Z]))(?=.*[@#$%]).{8,20})", R.string.passmatcherror);

    }

    public void mRegister(View view) {
        final String name = et_name.getText().toString();
        final String number = et_phone_number.getText().toString();
        final String email = et_email.getText().toString();
        final String password = et_password.getText().toString();

        String confirm_password = et_con_password.getText().toString();

        if (isNetworkAvailable())
        {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
            alertDialogBuilder.setCancelable(false);

            if (awesomeValidation.validate() && password.equals(confirm_password))
            {

                SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userName", name);
                editor.putString("password", password);
                editor.putString("emailID", email);
                editor.putString("phoneNumber", number);
                editor.apply();

                Intent intent = new Intent(RegisterActivity.this, RegistrationChooseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }

            else if (!password.equals(confirm_password))
            {
                alertDialogBuilder.setMessage(R.string.passmatcherror);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                et_con_password.setText("");
            }
        }
        else
        {
            Toast.makeText(RegisterActivity.this, "Please connect to the internet!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "No internet connection.");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}