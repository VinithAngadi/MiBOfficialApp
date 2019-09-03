package makeinbvb.com.mibofficialapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

public class PupaReg1Activity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private String noOfMembers = "0";
    private ImageButton imgbtn_next;
    private EditText et_teamName;
    private EditText et_productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupa_reg1);

        et_teamName = findViewById(R.id.et_teamName);
        et_productName = findViewById(R.id.et_productName);
        radioGroup = findViewById(R.id.rdbtn_grp);
        imgbtn_next = findViewById(R.id.img_btn_next_1);

        imgbtn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!noOfMembers.equals("0") || !et_teamName.getText().toString().equals("") || !et_productName.getText().toString().equals(""))
                {
                    SharedPreferences preferences = getSharedPreferences("PUPA_reg_details", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("team_name", et_teamName.getText().toString());
                    editor.putString("product_name", et_productName.getText().toString());
                    editor.putString("no_of_members", noOfMembers);
                    editor.apply();

                    Intent intent = new Intent(PupaReg1Activity.this, PupaReg2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PupaReg1Activity.this);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage("Please complete all the fields!");
                    alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        });



    }

    public void memberSelect(View view) {
        int radioId = radioGroup.getId();

        switch (radioId)
        {
            case R.id.rdbtn_2 : noOfMembers = "2";
                break;

            case R.id.rdbtn_3 : noOfMembers = "3";
                break;

            case R.id.rdbtn_4 : noOfMembers = "4";
                break;

            default: noOfMembers = "0";
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PupaReg1Activity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Warning!");
        alertDialogBuilder.setMessage("Do you want to cancel registration?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PupaReg1Activity.this, EventInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EventName", 0);
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
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
