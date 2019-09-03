package makeinbvb.com.mibofficialapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AnnouncementActivity extends AppCompatActivity
{
    private String TAG = "AnnouncementActivity";
    private DatabaseReference announcementsDB;
    private RecyclerView rv_announcements;
    private FirebaseRecyclerAdapter<AnnouncementCardClass,AnnouncementActivity.AnnouncementsViewHolder> firebaseRecyclerAdapter;

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    public static class AnnouncementsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public AnnouncementsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView tv_announcement_title = mView.findViewById(R.id.tv_announcement_title);
            tv_announcement_title.setText(title);
        }
        public void setMessage(String message){
            TextView tv_announcement_message = mView.findViewById(R.id.tv_announcement_message);
            tv_announcement_message.setText(message);
        }
        public void setTimestamp(String timestamp){
            TextView tv_timestamp =  mView.findViewById(R.id.tv_timestamp);
            tv_timestamp.setText(timestamp);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        announcementsDB = FirebaseDatabase.getInstance().getReference("Announcements");
        announcementsDB.keepSynced(true);

        DatabaseReference refDB = FirebaseDatabase.getInstance().getReference("Announcements");
        Query getQuery = refDB.orderByKey();

        rv_announcements = findViewById(R.id.rec_view_announcements);
        rv_announcements.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AnnouncementActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rv_announcements.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<AnnouncementCardClass>().setQuery(getQuery, AnnouncementCardClass.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AnnouncementCardClass, AnnouncementActivity.AnnouncementsViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(AnnouncementActivity.AnnouncementsViewHolder holder, final int position, final AnnouncementCardClass model) {
                holder.setTitle(model.getTitle());
                holder.setMessage(model.getMessage());
                holder.setTimestamp(model.getTimestamp());

                Log.i(TAG, "Title : "+model.getTitle()+" Status : "+model.getMessage()+" time : "+model.getTimestamp());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(AnnouncementActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public AnnouncementActivity.AnnouncementsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_announcements, parent, false);

                return new AnnouncementActivity.AnnouncementsViewHolder(view);
            }
        };

        rv_announcements.setAdapter(firebaseRecyclerAdapter);

    }
}