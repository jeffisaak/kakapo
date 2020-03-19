package kakapo.client.retrofit;

import kakapo.api.request.SignUpRequest;
import kakapo.api.request.UploadPreKeysRequest;
import kakapo.api.response.*;
import okhttp3.MultipartBody;
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

    @PUT("/api/v1/account/{guid}/quota")
    Call<QuotaResponse> fetchQuota(@Path("guid") String targetUserGuid,
                                   @Header("Kakapo-ID") String userGuid,
                                   @Header("Kakapo-API-Key") String apiKey);


//
//    @PUT("/api/v1/account/publicKey")
//    Call<FetchPublicKeyResponse> fetchPublicKey(@Body FetchPublicKeyRequest request);
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
//    @PUT("/api/v1/server/config")
//    Call<ServerConfigResponse> serverConfig(@Body ServerConfigRequest request);
//

    @Multipart
    @POST("/api/v1/item")
    Call<SubmitItemResponse> submitItem(@Header("Kakapo-ID") String userGuid,
                                        @Header("Kakapo-API-Key") String apiKey,
                                        @Part MultipartBody.Part json,
                                        @Part MultipartBody.Part header,
                                        @Part MultipartBody.Part content);
//
//    @PUT("/api/v1/item/delete")
//    Call<DeleteItemResponse> deleteItem(@Body DeleteItemRequest request);
//
//    @PUT("/api/v1/item/headers")
//    Call<FetchItemHeadersResponse> fetchItemHeaders(@Body FetchItemHeadersRequest request);
//
//    @Streaming
//    @PUT("/api/v1/item/content")
//    Call<ResponseBody> streamItemContent(@Body StreamContentRequest request);
//
//    @PUT("/api/v1/item/recipients")
//    Call<FetchRecipientsResponse> fetchRecipients(@Body FetchRecipientsRequest request);
}
