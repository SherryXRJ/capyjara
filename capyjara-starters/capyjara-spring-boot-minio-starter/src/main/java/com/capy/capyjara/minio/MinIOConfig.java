package com.capy.capyjara.minio;

import io.minio.MinioClient;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.Arrays;

@Configuration
@ConditionalOnProperty(prefix = "minio", name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MinIOProperties.class)
public class MinIOConfig {

    @Bean
    public MinioClient minioClient(MinIOProperties minIOProperties) {
        return new MinioClient
                .Builder()
                .credentials(minIOProperties.getAccessKey(), minIOProperties.getSecretKey())
                .endpoint(minIOProperties.getEndpoint(), minIOProperties.getPort(), minIOProperties.isSecure())
                .region(minIOProperties.getRegion())
                .httpClient(unsafeOkHttpClient())
//                .credentialsProvider()
                .build();
    }

    @Bean
    public OkHttpClient unsafeOkHttpClient(){
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            X509TrustManager trustManager = null;
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            trustManager = (X509TrustManager) trustManagers[0];

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .protocols(Arrays.asList(Protocol.HTTP_1_1))
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
