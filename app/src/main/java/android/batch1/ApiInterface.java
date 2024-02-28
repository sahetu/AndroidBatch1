package android.batch1;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("signup.php")
    Call<GetSignupData> getSignupData(
            @Field("username") String username,
            @Field("name") String name,
            @Field("email") String email,
            @Field("contact") String contact,
            @Field("password") String password,
            @Field("gender") String gender,
            @Field("city") String city
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<GetLoginData> getLoginData(
            @Field("email") String email,
            @Field("password") String password
    );

    /*@Multipart
    @POST("addOrganization/")
    Call<AddJobSeekerData> addOrganizationData(
            @Part("user_longitude") RequestBody user_longitude,
            @Part MultipartBody.Part imagePassport
    );*/

}