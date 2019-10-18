package net.tecgurus.holacomunicate.message;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author  Abraham Casas Aguilar
 */
public class ApiClient {

    public static final String BASE_URL = "https://fcm.googleapis.com/fcm/";
    public static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}