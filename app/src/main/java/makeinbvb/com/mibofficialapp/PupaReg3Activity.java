package makeinbvb.com.mibofficialapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class PupaReg3Activity extends AppCompatActivity {

    private Button btn_add_members;
    private LinearLayout ll_team_members;
    private String [] team_members;
    private int i = 0;
    private DatabaseReference db_team;
    private DatabaseReference db_user_details;
    private TextView tv_member_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupa_reg3);

        btn_add_members = findViewById(R.id.btn_add_members);
        db_team = FirebaseDatabase.getInstance().getReference().child("Pupa").child("team");
        db_user_details = FirebaseDatabase.getInstance().getReference().child("Users");

        ll_team_members = findViewById(R.id.ll_team_members);

        btn_add_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(PupaReg3Activity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
        {
            if (result.getContents() == null)
            {
                Toast.makeText(PupaReg3Activity.this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else
            {
                addMembers(result.getContents());
                Toast.makeText(PupaReg3Activity.this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addMembers(String member_id)
    {
        db_user_details.child(member_id).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue().toString();
                tv_member_name = findViewById(R.id.tv_member_name);
                tv_member_name.setText(name);
                db_team.child("");

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.linear_layout_team_member, null);
                // Add the new row before the add field button.
                ll_team_members.addView(rowView, ll_team_members.getChildCount() - 1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onDelete(View v) {
        ll_team_members.removeView((View) v.getParent());
    }

}