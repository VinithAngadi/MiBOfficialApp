package makeinbvb.com.mibofficialapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


public class EventsFragment extends Fragment
{
    private RecyclerView rec_events;
    private DatabaseReference eventsDB;
    private String TAG = "EventsFragment";
    private FirebaseRecyclerAdapter<EventCardClass, EventsFragment.Events_View_holder> firebaseRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_events, container, false);

        eventsDB = FirebaseDatabase.getInstance().getReference("EventStatus");

        eventsDB.keepSynced(true);

        rec_events = view.findViewById(R.id.events_rec_view);
        rec_events.setHasFixedSize(true);
        rec_events.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        DatabaseReference refDB = FirebaseDatabase.getInstance().getReference("EventStatus");

        Query getEvents = refDB.orderByKey();

        FirebaseRecyclerOptions recyclerOptions = new FirebaseRecyclerOptions.Builder<EventCardClass>().setQuery(getEvents, EventCardClass.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<EventCardClass, Events_View_holder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(EventsFragment.Events_View_holder holder, final int position, final EventCardClass model) {
                holder.setName(model.getName());
                holder.setCard_message(model.getCard_message());
                holder.setImageURL(model.getImageURL());

                Log.i(TAG, "Title : "+model.getName()+" Status : "+model.getCard_message()+" URL : "+model.getImageURL());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), EventInfoActivity.class);
                        intent.putExtra("EventName", position);
                        startActivity(intent);

                        Log.i(TAG, "Clicked "+position);

                    }
                });
            }

            @Override
            public EventsFragment.Events_View_holder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_events, parent, false);

                return new EventsFragment.Events_View_holder(view);
            }
        };

        rec_events.setAdapter(firebaseRecyclerAdapter);

        return view;
    }


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


    public static class Events_View_holder extends RecyclerView.ViewHolder{
        View mView;
        public Events_View_holder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView post_title = mView.findViewById(R.id.tv_event_title);
            post_title.setText(name);
        }
        public void setCard_message(String card_message){
            TextView post_desc = mView.findViewById(R.id.tv_event_status);
            post_desc.setText(card_message);
        }
        public void setImageURL(String imageURL){
            ImageView post_image =  mView.findViewById(R.id.img_event);
            Picasso.get().load(imageURL).fit().into(post_image);
        }
    }

}