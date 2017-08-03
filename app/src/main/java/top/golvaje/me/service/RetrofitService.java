package top.golvaje.me.service;


import top.golvaje.me.model.UserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Rohit on 7/19/2016.
 */
public interface RetrofitService {

    @FormUrlEncoded
    @POST("index.php/App_controller/fixer_login")
    Call<ResponseBody> login(@Body UserModel userModel);

//
//    @FormUrlEncoded
//    @POST("index.php/app_controller/user_register")
//    Call<ResponseBody> user_register(@FieldMap HashMap<String, Object> hm);
//
//   /* @GET("api/getcategories")
//    Call<ResponseBody> categary();
//
//    @GET("api/getcategories/{id}")
//    Call<ResponseBody> subCategary(@Path("id") String subCategoriesId);
//*/
//
//
//    @POST("index.php/App_controller/List_Categories")
//    Call<ResponseBody> categary();
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/edit_category")
//    Call<ResponseBody> edit_category(@FieldMap HashMap<String, Object> hashMap);
//
//
//    @FormUrlEncoded
//    @POST("index.php/Common_control/Get_states")
//    Call<ResponseBody> getstate(@FieldMap HashMap<String, String> hm);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/fbuser_login")
//    Call<ResponseBody> fb_login(@FieldMap HashMap<String, Object> hashMap);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/googleuser_login")
//    Call<ResponseBody> google_login(@FieldMap HashMap<String, Object> hashMap);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/verifycode_login")
//    Call<ResponseBody> verifycode_login(@FieldMap HashMap<String, Object> hashMap);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/password_reset")
//    Call<ResponseBody> password_reset(@FieldMap HashMap<String, Object> hashMap);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/fgreq_contact")
//    Call<ResponseBody> fgreq_contact(@FieldMap HashMap<String, Object> hashMap);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/Remove_Category")
//    Call<ResponseBody> Remove_Category(@FieldMap HashMap<String, Object> hashMap);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/category_list")
//    Call<ResponseBody> category_list(@FieldMap HashMap<String, Object> hashMap);
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/category_listing")
//    Call<ResponseBody> category_listing(@FieldMap HashMap<String, Object> hashMap);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/update_category")
//    Call<ResponseBody> update_category(@FieldMap HashMap<String, Object> hashMap);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/add_category")
//    Call<ResponseBody> add_category(@FieldMap HashMap<String, String> hashMap);
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/Get_citys")
//    Call<ResponseBody> getCitys(@FieldMap HashMap<String, String> hm);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/fixer_update_profile")
//    Call<ResponseBody> fixerupdateProfile(@FieldMap HashMap<String, Object> hm);
//
//
//
//
//   /* @FormUrlEncoded
//    @POST("index.php/App_controller/fixer_update_profile")
//    Call<ResponseBody> fixerupdateProfile(@FieldMap HashMap<String, String> hm);*/
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/task_details")
//    Call<ResponseBody> task_details(@FieldMap HashMap<String, Object> hm);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/chat_module")
//    Call<ResponseBody> send_message(@FieldMap HashMap<String, String> hm);
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/get_chats")
//    Call<ResponseBody> previous_chat(@FieldMap HashMap<String, String> hm);
//
//
//    @FormUrlEncoded
//    @POST("index.php/app_controller/get_user_chats")
//    Call<ResponseBody> get_user_chats(@FieldMap HashMap<String, Object> hm);
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/fixer_deactivate_profile")
//    Call<ResponseBody> Deactivate_Account(@FieldMap HashMap<String, String> hm);
//
//
//
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/Get_states")
//    Call<ResponseBody> State(@FieldMap HashMap<String, String> hm);
//
//
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/add_work_location")
//    Call<ResponseBody> add_area_of_work(@FieldMap HashMap<String, String> hm);
//
//
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/view_work_location")
//    Call<ResponseBody> show_area_of_work(@FieldMap HashMap<String, String> hm);
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/edit_work_location")
//    Call<ResponseBody> editArea(@FieldMap HashMap<String, String> hm);
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/delete_work_location")
//    Call<ResponseBody> deleteArea(@FieldMap HashMap<String, String> hm);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/payment_method")
//    Call<ResponseBody> payment(@FieldMap HashMap<String, String> hm);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/profile_info")
//    Call<ResponseBody> profile_detail(@FieldMap HashMap<String, String> hm);
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/update_vehicleinfo")
//    Call<ResponseBody> vehicle_info(@FieldMap HashMap<String, String> hm);
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/task_details")
//    Call<ResponseBody> task_history(@FieldMap HashMap<String, String> hm);
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/show_active_task")
//    Call<ResponseBody> active_task(@FieldMap HashMap<String, String> hm);
//
//
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/online_status")
//    Call<ResponseBody> sandStatus(@FieldMap HashMap<String, String> hm);
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/show_confirm_task")
//    Call<ResponseBody> confirmTask(@FieldMap HashMap<String, String> hm);
//
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/transaction")
//    Call<ResponseBody> transectionList(@FieldMap HashMap<String, String> hm);
//
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/confirm_task")
//    Call<ResponseBody> confirmTask_status(@FieldMap HashMap<String, String> hm);
//
//
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/payment_request")
//    Call<ResponseBody> RequestToPay(@FieldMap HashMap<String, String> hm);
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/show_payment_detail")
//    Call<ResponseBody> getpayment(@FieldMap HashMap<String, String> hm);
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/edit_confirmation")
//    Call<ResponseBody> edit_PaymentInfo(@FieldMap HashMap<String, String> hm);
//
//
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/update_payment_details")
//    Call<ResponseBody> confirmation_PaymentInfo(@FieldMap HashMap<String, String> hm);
//
//
//    @FormUrlEncoded
//    @POST("index.php/App_controller/review")
//    Call<ResponseBody> rating(@FieldMap HashMap<String, String> hm);


    // http://www.argalon.net/comefixme/index.php/App_controller/fixer_update_profile


   /* @GET("getcategories/index/{id}")
    Call<ResponseBody> subCategary(@Path("id") String subCategoriesId);

    @GET("subcategorydetails/index/{id}")
    Call<ResponseBody> getDetail(@Path("id") String Id);

    @POST("test.php")
    Call<ResponseBody> index(@Body HashMap<String, String> hm);

   // http://ruby.argalon.net/users/attempt_login

    @POST("users/attempt_login")
    Call<ResponseBody> create_login(@Body UserInfoModel userInfirmationModel);

    @POST("users/createuser")
    Call<ResponseBody> create_json(@Body UserInfoModel userInfirmationModel);

    @GET("admin_users/list")
    Call<ResponseBody> getTasker();*/

}
