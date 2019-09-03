package makeinbvb.com.mibofficialapp;

public class EventCardClass
{
    private String name;
    private String card_message;
    private String imageURL;

    public EventCardClass()
    {

    }

    public EventCardClass(String name, String card_message, String imageURL) {
        this.name = name;
        this.card_message = card_message;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCard_message() {
        return card_message;
    }

    public void setCard_message(String event_status) {
        this.card_message = event_status;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
