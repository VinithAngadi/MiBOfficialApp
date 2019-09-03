package makeinbvb.com.mibofficialapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationStudentActivity extends AppCompatActivity {

    private String TAG = "RegistrationStudentActivityHere";
    private Spinner sp_degree;
    private Spinner sp_specialtization;
    private Button btn_confirm_reg;
    private EditText et_univ_name;
    private EditText et_urn;
    private ArrayAdapter<CharSequence> adapter_degree;
    private ArrayAdapter<CharSequence> adapter_specialization;
    private String degree_selection;
    private String specialization_selection;
    private ProgressDialog progressDialog;
    private DatabaseReference userDB;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_student);

        sp_degree = findViewById(R.id.sp_degree);
        sp_specialtization = findViewById(R.id.sp_specialization);
        btn_confirm_reg = findViewById(R.id.btn_confirm_reg);
        et_univ_name = findViewById(R.id.et_univ_name);
        et_urn = findViewById(R.id.et_urn);


        firebaseAuth = FirebaseAuth.getInstance();

        userDB = FirebaseDatabase.getInstance().getReference("Users");


        adapter_degree = ArrayAdapter.createFromResource(this,R.array.degree_list, android.R.layout.simple_spinner_item);
        adapter_degree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_degree.setAdapter(adapter_degree);
        sp_degree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                degree_selection = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                degree_selection = "--Select--";
            }
        });

        adapter_specialization = ArrayAdapter.createFromResource(this, R.array.specialization_list, android.R.layout.simple_spinner_item);
        adapter_specialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_specialtization.setAdapter(adapter_specialization);
        sp_specialtization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                specialization_selection = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                specialization_selection = "--Select--";

            }
        });

        btn_confirm_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("collegeName", et_univ_name.getText().toString());
                editor.putString("urn", et_urn.getText().toString());
                editor.putString("degree", degree_selection);
                editor.putString("specialization", specialization_selection);
                editor.apply();

                Log.i(TAG, "College name : "+et_univ_name.getText().toString()+" degree : "+degree_selection);

                if (et_univ_name.getText().toString().equals("") || et_urn.getText().toString().equals("") || degree_selection.equals("--Select--") || specialization_selection.equals("--Select--"))
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationStudentActivity.this);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage("Please fill up all the fields!");
                    alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else
                {
                    progressDialog = new ProgressDialog(RegistrationStudentActivity.this);
                    progressDialog.setTitle("Creating User");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    SharedPreferences preferences_upload = getSharedPreferences("UserData", MODE_PRIVATE);
                    final String userName = preferences_upload.getString("userName","");
                    final String password = preferences_upload.getString("password","");
                    final String emailID = preferences_upload.getString("emailID","");
                    final String phoneNumber = preferences_upload.getString("phoneNumber","");
                    final String userType = preferences_upload.getString("userType","");
                    final String collegeName = preferences_upload.getString("collegeName","");
                    final String urn = preferences_upload.getString("urn","");
                    final String degree = preferences_upload.getString("degree","");
                    final String specialization = preferences_upload.getString("specialization","");


                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationStudentActivity.this);
                    alertDialogBuilder.setCancelable(false);

                    Log.i(TAG, "User name : "+userName+" Password : "+password);

                    Log.i(TAG, "College name : "+collegeName+" degree : "+degree);


                    firebaseAuth.createUserWithEmailAndPassword(emailID, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Log.i(TAG, "createUserWithEmail:success");

                                UserData userData = new UserData(userName, password, emailID, phoneNumber, userType, collegeName, urn, degree, specialization, "0", "0", "0","0");

                                userDB.child(FirebaseAuth.getInstance().getUid()).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Log.i(TAG, "Successfully uploaded user data");
                                        progressDialog.dismiss();

                                        Intent intent = new Intent(RegistrationStudentActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        progressDialog.dismiss();
                                        Log.i(TAG, "Failed to upload user data");
                                    }
                                });


                            }
                            else
                            {
                                Log.i(TAG, "createUserWithEmail:failure", task.getException());

                                String message = task.getException().getMessage();

                                progressDialog.dismiss();

                                alertDialogBuilder.setMessage(message);
                                alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();

                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationStudentActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Canel Registration?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(RegistrationStudentActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}