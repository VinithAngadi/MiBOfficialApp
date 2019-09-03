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

public class PupaReg2Activity extends AppCompatActivity {

    private EditText et_product_desc;
    private ImageButton img_btn_next_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupa_reg2);

        et_product_desc = findViewById(R.id.et_product_description);
        img_btn_next_2 = findViewById(R.id.img_btn_next_2);


        img_btn_next_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_product_desc.getText().toString().equals("")) {
                    SharedPreferences preferences = getSharedPreferences("PUPA_reg_details", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("product_description", et_product_desc.getText().toString());
                    editor.apply();

                    Intent intent = new Intent(PupaReg2Activity.this, PupaReg3Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {
                    AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PupaReg2Activity.this);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage("Please describe your product!");
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

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PupaReg2Activity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Warning!");
        alertDialogBuilder.setMessage("Do you want to cancel registration?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PupaReg2Activity.this, EventInfoActivity.class);
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
