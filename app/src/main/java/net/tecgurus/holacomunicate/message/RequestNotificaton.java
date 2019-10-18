package net.tecgurus.holacomunicate.message;
import com.google.gson.annotations.SerializedName;

public class RequestNotificaton {

    @SerializedName("to") //  "to" changed to token
    public String to;

    @SerializedName("notification")
    public Notification notification;

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getToken() {
        return to;
    }

    public void setToken(String token) {
        this.to = token;
    }

    @Override
    public String toString() {
        return "RequestNotificaton{" +
                "to='" + to + '\'' +
                ", notification=" + notification +
                '}';
    }
}