package makeinbvb.com.mibofficialapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

public class BFSReg1Activity extends AppCompatActivity {

    private RadioGroup rdgrp_bfs;
    private String noOfMembers = "0";
    private ImageButton img_btn_next_reg1_bfs;
    private EditText et_product_team_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfs1);

        rdgrp_bfs = findViewById(R.id.rdbtn_grp_BFS);
        et_product_team_name = findViewById(R.id.et_team_product_name);
        img_btn_next_reg1_bfs = findViewById(R.id.img_btn_next_bfs);

        img_btn_next_reg1_bfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String prod_name = et_product_team_name.getText().toString();


                if(!noOfMembers.equals("0") || !prod_name.equals(""))
                {
                    SharedPreferences preferences = getSharedPreferences("BFS_reg_details", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("team_name", et_product_team_name.getText().toString());
                    editor.putString("no_of_members", noOfMembers);
                    editor.apply();

                    AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(BFSReg1Activity.this);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage(R.string.BFS_ques_string);
                    alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(BFSReg1Activity.this, BFSReg2Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(BFSReg1Activity.this);
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

    public void memberSelect(View view)
    {
        int radioId = rdgrp_bfs.getId();

        switch (radioId)
        {
            case R.id.rdbtn_2_bfs : noOfMembers = "2";
                break;

            case R.id.rdbtn_3_bfs : noOfMembers = "3";
                break;

            case R.id.rdbtn_4_bfs : noOfMembers = "4";
                break;

            default: noOfMembers = "0";
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(BFSReg1Activity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Warning!");
        alertDialogBuilder.setMessage("Do you want to cancel registration?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(BFSReg1Activity.this, EventInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EventName", 1);
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
