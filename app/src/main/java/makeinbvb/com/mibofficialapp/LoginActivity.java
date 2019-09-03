package makeinbvb.com.mibofficialapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";
    private EditText et_email;
    private EditText et_password;
    private TextView tv_register;
    private TextView tv_forgotP;
    private DatabaseReference userDB;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private AwesomeValidation awesomeValidation;


    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();


        et_email = findViewById(R.id.et_email_login);
        et_password = findViewById(R.id.et_password_login);
        tv_register = findViewById(R.id.tv_register);
        tv_forgotP = findViewById(R.id.tv_forgotP);

        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);

        awesomeValidation.addValidation(this, R.id.et_email_login, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.et_password_login, "((?=.*\\d)((?=.*[a-z])|(?=.*[A-Z]))(?=.*[@#$%]).{8,20})", R.string.passworderror);

        tv_forgotP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

                et_email.setText("");
                et_password.setText("");
            }
        });
    }

    public void mLogin(View view) {
        String email = et_email.getText().toString();
        final String password = et_password.getText().toString();

        if (isNetworkAvailable())
        {
            if (awesomeValidation.validate())
            {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Logging in");
                progressDialog.setMessage("Please Wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                alertDialogBuilder.setCancelable(false);


                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");

                                    progressDialog.dismiss();

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithEmail:failure", task.getException());

                                    int exceptionNo = 0;
                                    String exception = task.getException().getMessage();
                                    String message = "Unable to login!";

                                    if (exception.startsWith("There"))
                                    {
                                        exceptionNo = 1;
                                        message = "Your account doesn't exist! Please register.";
                                    }
                                    else if (exception.startsWith("The"))
                                    {
                                        exceptionNo = 2;
                                        message = "Wrong password!. Enter again.";
                                    }

                                    Log.i(TAG, task.getException().getMessage());
                                    progressDialog.dismiss();
                                    alertDialogBuilder.setMessage(message);
                                    final int finalExceptionNo = exceptionNo;
                                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if (finalExceptionNo == 1)
                                            {
                                                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                                startActivity(intent);
                                            }
                                            else if (finalExceptionNo == 2)
                                            {
                                                et_password.setText("");
                                            }
                                        }
                                    });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            }
                        });
            }
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Connect to the internet!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}