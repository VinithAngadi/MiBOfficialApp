package makeinbvb.com.mibofficialapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class BFSReg2Activity extends AppCompatActivity {

    private EditText et_product_desc_bfs;
    private ImageButton img_btn_next_reg2_bfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfsreg2);

        et_product_desc_bfs = findViewById(R.id.et_product_description_bfs);
        img_btn_next_reg2_bfs = findViewById(R.id.img_btn_next_reg2_bfs);

        img_btn_next_reg2_bfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product_decs = et_product_desc_bfs.getText().toString();
                if (product_decs.length() < 50 || product_decs.length() > 350)
                {
                    AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(BFSReg2Activity.this);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage(R.string.BFS_ques_string);
                    alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

                else
                {
                    SharedPreferences preferences = getSharedPreferences("BFS_reg_details", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("product_description", product_decs);
                    editor.apply();

                    Intent intent = new Intent(BFSReg2Activity.this, PupaReg3Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }
}
