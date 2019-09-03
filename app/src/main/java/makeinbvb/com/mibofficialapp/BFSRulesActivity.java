package makeinbvb.com.mibofficialapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.zxing.integration.android.IntentIntegrator;

public class BFSRulesActivity extends AppCompatActivity {

    private PDFView pdfView;
    private Button btn_pdf_next;
    private int SPLASH_TIME_OUT = 15000;
    private int btn_enable_flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfsrules);

        pdfView = findViewById(R.id.pdf_viewer);
        btn_pdf_next = findViewById(R.id.btn_pdf_next);


        pdfView.fromAsset("BFSRulesPDF.pdf").load();


        new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            // This method will be executed once the timer is over
            // Start your app main activity
            btn_enable_flag = 1;
        }
    }, SPLASH_TIME_OUT);


        btn_pdf_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( btn_enable_flag == 1)
                {
                    Intent intent = new Intent(BFSRulesActivity.this, ChooseTeamActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BFSRulesActivity.this);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setTitle("Important!");
                    alertDialogBuilder.setMessage("Please read the whole document!\n These are rules, not terms and conditions. ");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
