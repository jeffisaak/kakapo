package kakapo.client.retrofit;

import kakapo.api.request.*;
import kakapo.api.response.*;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface RetrofitService {

    @GET("/api/v1/account/requestGuid")
    Call<RequestGuidResponse> requestGuid();

    @PUT("/api/v1/account/create")
    Call<Void> createAccount(@Body SignUpRequest request);

    @PUT("/api/v1/account/delete")
    Call<Void> deleteAccount(@Body DeleteAccountRequest request);

    @PUT("/api/v1/account/authenticate")
    Call<Void> authenticate(@Body AuthenticateRequest request);

    @PUT("/api/v1/account/publicKey")
    Call<FetchPublicKeyResponse> fetchPublicKey(@Body FetchPublicKeyRequest request);

    @PUT("/api/v1/account/quota")
    Call<QuotaResponse> fetchQuota(@Body QuotaRequest request);

    @PUT("/api/v1/account/upload")
    Call<UploadAccountResponse> uploadAccount(@Body UploadAccountRequest request);

    @PUT("/api/v1/account/download")
    Call<DownloadAccountResponse> downloadAccount(@Body DownloadAccountRequest request);

    @PUT("/api/v1/account/blacklist")
    Call<Void> blacklist(@Body BlacklistRequest request);

    @PUT("/api/v1/server/config")
    Call<ServerConfigResponse> serverConfig(@Body ServerConfigRequest request);

    @Multipart
    @POST("/api/v1/item/submit")
    Call<SubmitItemResponse> submitItem(@Part MultipartBody.Part json,
                                        @Part MultipartBody.Part header,
                                        @Part MultipartBody.Part content);

    @PUT("/api/v1/item/delete")
    Call<DeleteItemResponse> deleteItem(@Body DeleteItemRequest request);

    @PUT("/api/v1/item/headers")
    Call<FetchItemHeadersResponse> fetchItemHeaders(@Body FetchItemHeadersRequest request);

    @Streaming
    @PUT("/api/v1/item/content")
    Call<ResponseBody> streamItemContent(@Body StreamContentRequest request);

    @PUT("/api/v1/item/recipients")
    Call<FetchRecipientsResponse> fetchRecipients(@Body FetchRecipientsRequest request);
}
