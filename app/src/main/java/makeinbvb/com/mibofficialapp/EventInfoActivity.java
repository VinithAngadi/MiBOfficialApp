package makeinbvb.com.mibofficialapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventInfoActivity extends AppCompatActivity {

    private String TAG = "EventInfoActivityHere";
    private ViewPager viewPager;
    private Integer [] img ;
    private String title;
    private String desc;
    private TextView tv_title;
    private TextView tv_desc;
    private String reg_title;
    private Button btn_reg_nxt;
    private String btn_text;
    private Class cls, reg_cls;
    private FirebaseAuth mAuth;
    private DatabaseReference accessDB;
    private DatabaseReference userDB;
    private int reg_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        Log.i(TAG, "onCreate");

        mAuth = FirebaseAuth.getInstance();

        btn_reg_nxt = findViewById(R.id.btn_reg_next_event);

        userDB = FirebaseDatabase.getInstance().getReference("Users");
        accessDB = FirebaseDatabase.getInstance().getReference("EventStatus");

        Intent intent = getIntent();
        final int choice = intent.getIntExtra("EventName", -1);

        switch (choice)
        {
            case -1: Log.i("Event_info_activity", "error in choice");
                break;
            case 0: img = new Integer[]{R.drawable.pupa_img, R.drawable.e_summit, R.drawable.intel_ideation_img};
                title = "PUPA";
                desc = getResources().getString(R.string.PUPA);
                reg_title = "pupa_reg";
                reg_cls = ChooseTeamActivity.class;
                cls = PupaActivity.class;
                break;

            case 1: img = new Integer[]{R.drawable.e_summit, R.drawable.intel_ideation_img, R.drawable.butterfly};
                title = "E-SUMMIT";
                desc = getResources().getString(R.string.E_Summit);
                reg_title = "esummit_reg";
                reg_cls = ChooseTeamActivity.class;
                cls = E_SummitActivity.class;
                break;

            case 2: img = new Integer[]{R.drawable.intel_ideation_img, R.drawable.butterfly, R.drawable.pupa_img};
                title = "INTEL IDEATION CAMP";
                desc = getResources().getString(R.string.Ideation);
                reg_title = "intel_ideation_reg";
                reg_cls = ChooseTeamActivity.class;
                cls = IntelIdeationActivity.class;
                break;

            case 3: img = new Integer[]{R.drawable.butterfly, R.drawable.pupa_img, R.drawable.e_summit};
                title = "BUTTERFLY";
                desc = getResources().getString(R.string.Butterfly);
                reg_title = "butterfly_reg";
                reg_cls = ChooseTeamActivity.class;
                cls = ButterflyActivity.class;
                break;

            default: Log.i("Event_info_activity", "default choice");
        }


        Log.i(TAG, "Uid : " + mAuth.getUid());
        userDB.child(mAuth.getUid()).child(reg_title).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);
                Log.i(TAG, reg_title+" : "+String.valueOf(value));
                if (value.equals("0"))
                {
                    btn_reg_nxt.setVisibility(View.INVISIBLE);
                    reg_flag = 0;
                    btn_reg_nxt.setClickable(false);
                }
                else
                {
                    btn_reg_nxt.setVisibility(View.VISIBLE);
                    reg_flag = 1;
                    btn_reg_nxt.setClickable(true);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.i(TAG, "Choice : "+String.valueOf(choice+1));

        accessDB.child(String.valueOf(choice+1)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String event_status = dataSnapshot.child("event_status").getValue(String.class);
                String reg_status = dataSnapshot.child("reg_status").getValue(String.class);
                Log.i(TAG, String.valueOf(reg_flag));
                if (event_status.equals("active"))
                {
                    if (reg_status.equals("open"))
                    {
                        if (reg_flag == 1)
                        {
                            btn_reg_nxt.setClickable(true);
                            btn_reg_nxt.setVisibility(View.VISIBLE);
                            btn_reg_nxt.setText("NEXT");
                            btn_text = "NEXT";
                        }
                        else
                        {
                            btn_reg_nxt.setClickable(true);
                            btn_reg_nxt.setVisibility(View.VISIBLE);
                            btn_reg_nxt.setText("REGISTER");
                            btn_text = "REGISTER";
                        }
                    }
                    else
                    {
                        if (reg_flag == 1)
                        {
                            btn_reg_nxt.setClickable(true);
                            btn_reg_nxt.setVisibility(View.VISIBLE);
                            btn_reg_nxt.setText("NEXT");
                            btn_text = "NEXT";
                        }
                        else
                        {
                            btn_reg_nxt.setClickable(false);
                            btn_reg_nxt.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                else
                {
                    btn_reg_nxt.setClickable(false);
                    btn_reg_nxt.setVisibility(View.INVISIBLE);
                }

                btn_reg_nxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isNetworkAvailable())
                        {
                            Log.i(TAG, "Button Clicked : "+btn_text);

                            if(btn_text.equals("REGISTER"))
                            {
                                Intent intent = new Intent(EventInfoActivity.this, reg_cls);
                                startActivity(intent);
                            }
                            else if (btn_text.equals("NEXT"))
                            {
                                Intent intent = new Intent(EventInfoActivity.this, cls);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else
                        {
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EventInfoActivity.this);
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setTitle("No internet!");
                            alertDialogBuilder.setMessage("Please connect to the internet.");
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);

        tv_title.setText(title);
        tv_desc.setText(desc);

        viewPager = findViewById(R.id.view_pager_event_info);


        ViewPagerAdapter_Event_Info adapter = new ViewPagerAdapter_Event_Info(EventInfoActivity.this, img);

        viewPager.setAdapter(adapter);

        btn_text = btn_reg_nxt.getText().toString();
        Log.i(TAG, "Button text : "+btn_text);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}