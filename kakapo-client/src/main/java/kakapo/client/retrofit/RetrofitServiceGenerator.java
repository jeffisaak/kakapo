package kakapo.client.retrofit;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitServiceGenerator {

    public static <S> S createService(Class<S> serviceClass, String baseUrl, boolean debug) {

        OkHttpClient.Builder httpClient = null;

        // If we're running in debug mode, create an HTTP client that trusts everybody and
        // everything. Otherwise, create a regular HTTP client.
        if (debug) {
            httpClient = buildTrustingHttpClient();
        } else {
            httpClient = new OkHttpClient.Builder();
        }

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
