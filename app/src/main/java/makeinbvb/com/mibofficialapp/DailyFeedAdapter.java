package makeinbvb.com.mibofficialapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DailyFeedAdapter extends RecyclerView.Adapter<DailyFeedAdapter.DailyFeedViewHolder>
{
    private Context ctx;
    private List<DailyFeedCardClass> feedList;



    public DailyFeedAdapter (Context ctx, List<DailyFeedCardClass> feedList)
    {
        this.ctx = ctx;
        this.feedList = feedList;
    }

    @NonNull
    @Override
    public DailyFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_daily_feed, null);

        return new DailyFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyFeedViewHolder holder, int position) {

        DailyFeedCardClass feedCardClass = feedList.get(position);
        holder.tv_feed_title.setText(feedCardClass.getFeed_title());
        //holder.img_view.setImageResource(feedCardClass.getFeed_img());
        //holder.img_view.setImageDrawable(ctx.getResources().getDrawable(feedCardClass.getFeed_img()));
        Picasso.get().load(feedCardClass.getFeed_img()).fit().into(holder.img_view);



    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class DailyFeedViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_view;
        TextView tv_feed_title;

        public DailyFeedViewHolder(View itemView) {
            super(itemView);

            img_view = itemView.findViewById(R.id.img_daily_feed);
            tv_feed_title = itemView.findViewById(R.id.tv_feed_title);

        }
    }
}
