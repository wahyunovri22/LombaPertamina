package com.sc.semicolon.lombapertamina.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by cis on 07/04/2018.
 */

public interface ApiService {

    @GET("getAgen.php")
    Call<ResponseBody> getAgen();

    @GET("getBarang.php")
    Call<ResponseBody> getBarang();

    @GET("getReview.php")
    Call<ResponseBody> getReview();

    @GET("login.php")
    Call<ResponseBody> login();

    @GET("getTransaksi.php")
    Call<ResponseBody> getTransaksi();


    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> register(@Field("username") String username,
                                @Field("email") String email,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("updateStatus.php")
    Call<ResponseBody> updateStatus(@Field("id_transaksi") String id,
                                    @Field("status") String status);

    @FormUrlEncoded
    @POST("insertReview.php")
    Call<ResponseBody> insertReview(@Field("id_agen") String username,
                                    @Field("id_user") String email,
                                    @Field("nama") String password,
                                    @Field("review") String review);

    @FormUrlEncoded
    @POST("updatePembeli.php")
    Call<ResponseBody> uppdateProfil(@Field("id_user") String email,
                                     @Field("nama_pembeli") String password,
                                     @Field("alamat_pembeli") String review,
                                     @Field("telepon_pembeli") String telp,
                                     @Field("foto_pembeli") String foto);

    @FormUrlEncoded
    @POST("updateAgen.php")
    Call<ResponseBody> daftarAgen(@Field("id_user") String email,
                                  @Field("nama_agen") String password,
                                  @Field("nomor_agen") String telp,
                                  @Field("slogan_agen") String slogan,
                                  @Field("alamat_agen") String review,
                                  @Field("telepon_agen") String tlp,
                                  @Field("status") String status,
                                  @Field("avatar_agen") String avatar,
                                  @Field("latitude") double latitude,
                                  @Field("longitude") double longitude);

    @FormUrlEncoded
    @POST("editAgen.php")
    Call<ResponseBody> editAgen(@Field("id_user") String email,
                                @Field("nama_agen") String password,
                                @Field("slogan_agen") String slogan,
                                @Field("alamat_agen") String review,
                                @Field("telepon_agen") String tlp,
                                @Field("avatar_agen") String avatar);

    @FormUrlEncoded
    @POST("insertBarang.php")
    Call<ResponseBody> inputBarang(@Field("id_agen") String email,
                                   @Field("nama_barang") String password,
                                   @Field("stok") int slogan,
                                   @Field("harga_barang") int review,
                                   @Field("avatar_barang") String avatar);

    @Multipart
    @POST("upload.php")
    Call<Result> postIMmage(@Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("insertPembeliAwal.php")
    Call<ResponseBody> insertPembeli(@Field("id_user") String username,
                                     @Field("nama_pembeli") String email);

    @FormUrlEncoded
    @POST("insertAgen.php")
    Call<ResponseBody> insertAgen(@Field("id_user") String id_user);

    @FormUrlEncoded
    @POST("updateNamaReview.php")
    Call<ResponseBody> updateNamaReview(@Field("id_user") String id_user,
                                        @Field("nama") String nama);
}
