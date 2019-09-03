package makeinbvb.com.mibofficialapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DailyFeedFragment extends Fragment
{

    private String TAG = "AnnouncementsFragment";
    private RecyclerView rec_feed;
    private DailyFeedAdapter adapter;
    private List<DailyFeedCardClass> feedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_daily_feed, container, false);

        rec_feed = view.findViewById(R.id.daily_feed_rec_view);
        rec_feed.setHasFixedSize(true);
        rec_feed.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        feedList = new ArrayList<>();

        feedList.add(new DailyFeedCardClass("Monday!", R.drawable.investment_tips));
        feedList.add(new DailyFeedCardClass("Tuesday!", R.drawable.investment_tips));
        feedList.add(new DailyFeedCardClass("Wednesday!", R.drawable.investment_tips));
        feedList.add(new DailyFeedCardClass("Thursday!", R.drawable.investment_tips));
        feedList.add(new DailyFeedCardClass("Friday!", R.drawable.investment_tips));

        adapter = new DailyFeedAdapter(this.getActivity(), feedList);

        rec_feed.setAdapter(adapter);

        return view;
    }
}
