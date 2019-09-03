package makeinbvb.com.mibofficialapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistrationChooseActivity extends AppCompatActivity
{

    private Button btn_student;
    private Button btn_not_a_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_choose);

        btn_student = findViewById(R.id.btn_student);
        btn_not_a_student = findViewById(R.id.btn_not_a_student);

        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();


        btn_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("userType", "student");
                editor.apply();
                Intent intent = new Intent(RegistrationChooseActivity.this, RegistrationStudentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


        btn_not_a_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("userType", "non-student");
                editor.apply();
                Intent intent = new Intent(RegistrationChooseActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationChooseActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Alert!");
        alertDialogBuilder.setMessage("Canel Registration?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(RegistrationChooseActivity.this, LoginActivity.class);
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
