package makeinbvb.com.mibofficialapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PupaActivity extends AppCompatActivity {

    private String TAG = "PupaActivityHere";
    private final static int REQUEST_CALL = 1;
    private TextView tv_quote;
    private TextView tv_quoteAuthor;
    private LinearLayout ll_teamDetails;
    private long startTimeStamp;
    private TextView tv_hours;
    private TextView tv_minutes;
    private TextView tv_seconds;
    private TextView tv_mentor1;
    private TextView tv_mentor2;
    private ImageButton btn_mentor1Call;
    private ImageButton btn_mentor2Call;
    private long endTimeStamp;
    private long timeLeftInMillis;
    private CountDownTimer countDownTimer;
    private DatabaseReference quoteDB;
    private DatabaseReference timeDB;
    private DatabaseReference teamRef;
    private String teamNo ;
    private String callNumber = "9972701091";
    private String dateString;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupa);

        tv_hours = findViewById(R.id.tv_hours);
        tv_minutes = findViewById(R.id.tv_minutes);
        tv_seconds = findViewById(R.id.tv_seconds);
        btn_mentor1Call = findViewById(R.id.btn_mentor1Call);
        btn_mentor2Call = findViewById(R.id.btn_mentor2Call);
        ll_teamDetails = findViewById(R.id.ll_teamDetails);
        tv_quote = findViewById(R.id.tv_quote);
        tv_quoteAuthor = findViewById(R.id.tv_quoteAuthor);
        tv_mentor1 = findViewById(R.id.tv_mentor1);
        tv_mentor2 = findViewById(R.id.tv_mentor2);


        SharedPreferences preferences = getSharedPreferences("User data", MODE_PRIVATE);
        teamNo = preferences.getString("Team number", "-1");

        quoteDB = FirebaseDatabase.getInstance().getReference("QuoteInfo");

        quoteDB.child("quote").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_quote.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        quoteDB.child("author").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_quoteAuthor.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        timeDB = FirebaseDatabase.getInstance().getReference("EventStatus");
        timeDB.child("1").child("countdownTimer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                dateString = dataSnapshot.getValue(String.class);

                Log.i(TAG, "inside addValueListener : " + dateString);

                if (dateString != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd','HH:mm:ss");
                    Date testDate = null;
                    try {
                        testDate = sdf.parse(dateString);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    endTimeStamp = testDate.getTime();
                }

                startTimeStamp = System.currentTimeMillis();


                timeLeftInMillis = endTimeStamp - startTimeStamp;


                countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeftInMillis = millisUntilFinished;
                        updateTimer();
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        teamRef = FirebaseDatabase.getInstance().getReference("Pupa").child("team").child(teamNo);

        teamRef.child("members").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String value = dataSnapshot.getValue(String.class);
                Log.i(TAG, value);
                TextView tv_member = new TextView(PupaActivity.this);
                tv_member.setText(value);
                tv_member.setTextSize(18);
                tv_member.setPadding(10,5,10,5);
                ll_teamDetails.addView(tv_member);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        teamRef.child("mentors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String mentor1Name = dataSnapshot.child("mentor1").child("name").getValue(String.class);
                String mentor2Name = dataSnapshot.child("mentor2").child("name").getValue(String.class);
                final String mentor1Number = dataSnapshot.child("mentor1").child("number").getValue(String.class);
                final String mentor2Number = dataSnapshot.child("mentor2").child("number").getValue(String.class);

                tv_mentor1.setText(mentor1Name);
                tv_mentor2.setText(mentor2Name);



                btn_mentor1Call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PupaActivity.this);
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setMessage("Confirm your call?");
                        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //SharedPreferences preferences = getSharedPreferences("Mentor Details", MODE_PRIVATE);
                                //callNumber = preferences.getString("mentor1Number", "0");
                                callMentor(mentor1Number);
                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        Log.i(TAG, "mentor1Number"+mentor1Number);

                    }
                });

                btn_mentor2Call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PupaActivity.this);
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setMessage("Confirm your call?");
                        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //SharedPreferences preferences = getSharedPreferences("Mentor Details", MODE_PRIVATE);
                                //callNumber = preferences.getString("mentor2Number", "0");
                                callMentor(mentor2Number);
                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        Log.i(TAG, "mentor2Number"+mentor2Number);

                    }
                });



                SharedPreferences preferences = getSharedPreferences("Mentor Details", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("mentor1Name", mentor1Name);
                editor.putString("mentor2Name", mentor2Name);
                editor.putString("mentor1Number", mentor1Number);
                editor.putString("mentor2Number", mentor2Number);
                editor.apply();

                Log.i(TAG, "mentor1Name "+mentor1Name);
                Log.i(TAG, "mentor2Name "+mentor2Name);
                Log.i(TAG, "mentor1Number "+mentor1Number);
                Log.i(TAG, "mentor1Number "+mentor2Number);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        SharedPreferences preferencesNo = getSharedPreferences("Mentor Details", MODE_PRIVATE);
        callNumber = preferencesNo.getString("mentor1Number", "0");
    }

    private void updateTimer()
    {
        long hours = TimeUnit.MILLISECONDS.toHours(timeLeftInMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeftInMillis));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis));

        tv_hours.setText(String.valueOf(hours + " hours"));
        tv_minutes.setText(String.valueOf(minutes + " minutes"));
        tv_seconds.setText(String.valueOf(seconds + " seconds"));

    }

    public void callMentor(String number) {

        if(ContextCompat.checkSelfPermission(PupaActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(PupaActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else
        {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         if(requestCode == REQUEST_CALL)
         {
             if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
             {
                 callMentor("9972701091");
             }
             else
             {
                 Toast.makeText(PupaActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
             }
         }
    }
}