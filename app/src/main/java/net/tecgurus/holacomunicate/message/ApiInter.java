package net.tecgurus.holacomunicate.message;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @author  Abraham Casas Aguilar
 */
public interface ApiInter {


    @Headers({"Authorization:key=AAAAAIHGD0w:APA91bGaKlsQhEeiwd5rdua4QJR9JvJl2C4g1tA0v1r9EbTQztwbKenGUAwrKA9vVBYo-F99jHqsu8_475DJBNiq-q1OKe0U_yytTJvJhP2jNgQ51sZQWRF-uV4A8Bq8l3jeAWa4h7Ag","Content-Type:application/json"})
    @POST("send")
    Call<ResponseBody> sendChatNotification(@Body RequestNotificaton requestNotificaton);


    @Headers({"Authorization: key=AAAAAIHGD0w:APA91bGaKlsQhEeiwd5rdua4QJR9JvJl2C4g1tA0v1r9EbTQztwbKenGUAwrKA9vVBYo-F99jHqsu8_475DJBNiq-q1OKe0U_yytTJvJhP2jNgQ51sZQWRF-uV4A8Bq8l3jeAWa4h7Ag","Content-Type:application/json"})
    @POST("send")
    Call<ResponseBody> sendNotification(
            @Field("token") String token,
            @Field("title") String title,
            @Field("body") String body
    );
}
