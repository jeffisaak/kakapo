package kakapo.client.retrofit;

import kakapo.api.request.SignUpRequest;
import kakapo.api.request.UploadPreKeysRequest;
import kakapo.api.response.*;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface RetrofitService {

    @POST("/api/v1/account")
    Call<SignUpResponse> createAccount(@Body SignUpRequest request);

    @POST("/api/v1/account/preKeys")
    Call<UploadPreKeysResponse> uploadPreKeys(@Header("Kakapo-ID") String userGuid,
                                              @Header("Kakapo-API-Key") String apiKey,
                                              @Body UploadPreKeysRequest request);

    @DELETE("/api/v1/account/{guid}")
    Call<Void> deleteAccount(@Path("guid") String guidToDelete,
                             @Header("Kakapo-ID") String userGuid,
                             @Header("Kakapo-API-Key") String apiKey);

    @GET("/api/v1/account/{guid}/preKey")
    Call<FetchPreKeyResponse> fetchPreKey(@Path("guid") String targetUserGuid,
                                          @Header("Kakapo-ID") String userGuid,
                                          @Header("Kakapo-API-Key") String apiKey);

    @GET("/api/v1/account/{guid}/quota")
    Call<QuotaResponse> fetchQuota(@Path("guid") String targetUserGuid,
                                   @Header("Kakapo-ID") String userGuid,
                                   @Header("Kakapo-API-Key") String apiKey);

    @GET("/api/v1/account/{guid}/publicKey")
    Call<FetchPublicKeyResponse> fetchPublicKey(@Path("guid") String targetUserGuid,
                                                @Header("Kakapo-ID") String userGuid,
                                                @Header("Kakapo-API-Key") String apiKey);

    //
//    @PUT("/api/v1/account/upload")
//    Call<UploadAccountResponse> uploadAccount(@Body UploadAccountRequest request);
//
//    @PUT("/api/v1/account/download")
//    Call<DownloadAccountResponse> downloadAccount(@Body DownloadAccountRequest request);
//
//    @PUT("/api/v1/account/blacklist")
//    Call<Void> blacklist(@Body BlacklistRequest request);
//
    @PUT("/api/v1/server/config")
    Call<ServerConfigResponse> serverConfig(@Header("Kakapo-ID") String userGuid,
                                            @Header("Kakapo-API-Key") String apiKey);

    @Multipart
    @POST("/api/v1/item")
    Call<SubmitItemResponse> submitItem(@Header("Kakapo-ID") String userGuid,
                                        @Header("Kakapo-API-Key") String apiKey,
                                        @Part MultipartBody.Part json,
                                        @Part MultipartBody.Part header,
                                        @Part MultipartBody.Part content);

    @GET("/api/v1/item/headers")
    Call<FetchItemHeadersResponse> fetchItemHeaders(@Header("Kakapo-ID") String userGuid,
                                                    @Header("Kakapo-API-Key") String apiKey,
                                                    @Query("count") Integer itemCount,
                                                    @Query("last") Long lastItemRemoteId,
                                                    @Query("parent") Long parentItemRemoteId,
                                                    @Query("id") Long itemRemoteId);

    @GET("/api/v1/item/{itemRemoteId}/recipients")
    Call<FetchRecipientsResponse> fetchRecipients(@Path("itemRemoteId") Long itemRemoteId,
                                                  @Header("Kakapo-ID") String userGuid,
                                                  @Header("Kakapo-API-Key") String apiKey);


    @DELETE("/api/v1/item/{itemRemoteId}")
    Call<DeleteItemResponse> deleteItem(@Path("itemRemoteId") Long itemRemoteId,
                                        @Header("Kakapo-ID") String userGuid,
                                        @Header("Kakapo-API-Key") String apiKey);

    @Streaming
    @GET("/api/v1/item/{itemRemoteId}/content")
    Call<ResponseBody> streamItemContent(@Path("itemRemoteId") Long itemRemoteId,
                                         @Header("Kakapo-ID") String userGuid,
                                         @Header("Kakapo-API-Key") String apiKey);

}
