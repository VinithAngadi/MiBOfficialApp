package makeinbvb.com.mibofficialapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String TAG = "HomeActivityHere";
    private BottomNavigationView home_nav_bar;
    private FrameLayout home_main_frame;
    private EventsFragment eventsFragment;
    private DailyFeedFragment dailyFeedFragment;
    private AboutMiBFragment aboutMiBFragment;


    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() == null)
        {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pupa_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_feedback:
                Intent Email = new Intent(Intent.ACTION_SENDTO);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "vinith.va@gmail.com" });
                Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                Email.putExtra(Intent.EXTRA_TEXT, "Dear ...," + "");
                startActivity(Intent.createChooser(Email, "Send Feedback:"));
                break;

            case R.id.action_logout:
                if (isNetworkAvailable())
                {
                    firebaseAuth.signOut();
                    Intent intent_logout = new Intent(HomeActivity.this, LoginActivity.class);
                    intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_logout);
                    finish();
                }
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage("Connenct to the internet!");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;

            case R.id.action_notifications:
                    Intent intent = new Intent(HomeActivity.this, AnnouncementActivity.class);
                    startActivity(intent);
                    break;

            default: return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();

        home_main_frame = findViewById(R.id.home_main_frame);
        home_nav_bar = findViewById(R.id.home_nav_bar);

        eventsFragment = new EventsFragment();
        dailyFeedFragment = new DailyFeedFragment();
        aboutMiBFragment = new AboutMiBFragment();

        setFragment(eventsFragment);


        home_nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.nav_menu_1:
                        setFragment(eventsFragment);
                        return true;

                    case R.id.nav_menu_2:
                        setFragment(dailyFeedFragment);
                        return true;

                    case R.id.nav_menu_3:
                        setFragment(aboutMiBFragment);
                        return true;

                    default: return false;

                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_main_frame, fragment);
        fragmentTransaction.commit();
    }
}