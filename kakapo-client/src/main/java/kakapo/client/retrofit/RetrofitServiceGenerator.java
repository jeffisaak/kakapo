package kakapo.client.retrofit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

public class RetrofitServiceGenerator {

    public static final String CONNECT_TIMEOUT = "CONNECT_TIMEOUT";
    public static final String READ_TIMEOUT = "READ_TIMEOUT";
    public static final String WRITE_TIMEOUT = "WRITE_TIMEOUT";

    public static <S> S createService(Class<S> serviceClass, String baseUrl, boolean debug) {

        OkHttpClient.Builder httpClient = null;

        // If we're running in debug mode, create an HTTP client that trusts everybody and
        // everything. Otherwise, create a regular HTTP client.
        if (debug) {
            httpClient = buildTrustingHttpClient();
        } else {
            httpClient = new OkHttpClient.Builder();
        }

        // Set up an interceptor for custom timeouts.
        Interceptor timeoutInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                int connectTimeout = chain.connectTimeoutMillis();
                int readTimeout = chain.readTimeoutMillis();
                int writeTimeout = chain.writeTimeoutMillis();

                String connectNew = request.header(CONNECT_TIMEOUT);
                String readNew = request.header(READ_TIMEOUT);
                String writeNew = request.header(WRITE_TIMEOUT);

                if (connectNew != null) {
                    connectTimeout = Integer.valueOf(connectNew);
                }
                if (readNew != null) {
                    readTimeout = Integer.valueOf(readNew);
                }
                if (writeNew != null) {
                    writeTimeout = Integer.valueOf(writeNew);
                }

                Request.Builder builder = request.newBuilder();
                builder.removeHeader(CONNECT_TIMEOUT);
                builder.removeHeader(READ_TIMEOUT);
                builder.removeHeader(WRITE_TIMEOUT);

                return chain
                        .withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                        .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
                        .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                        .proceed(builder.build());
            }
        };

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(JacksonConverterFactory.create());

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    private static OkHttpClient.Builder buildTrustingHttpClient() {

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                        // Noop.
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                        // Noop.
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            // Rethrow this as a RuntimeException.
            throw new RuntimeException(e);
        }
        try {
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            // Rethrow this as a RuntimeException.
            throw new RuntimeException(e);
        }

        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        httpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        return httpClient;
    }
}
