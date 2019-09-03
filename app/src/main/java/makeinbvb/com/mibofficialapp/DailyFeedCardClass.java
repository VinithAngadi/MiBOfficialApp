package makeinbvb.com.mibofficialapp;

public class DailyFeedCardClass {

    private String feed_title;
    private int feed_img;

    public DailyFeedCardClass (String feed_title, int feed_img)
    {
        this.feed_title = feed_title;
        this.feed_img = feed_img;
    }


    public String getFeed_title() {
        return feed_title;
    }

    public int getFeed_img() {
        return feed_img;
    }
}
