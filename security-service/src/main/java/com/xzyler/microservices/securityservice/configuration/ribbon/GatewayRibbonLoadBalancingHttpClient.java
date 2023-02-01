//package com.xzyler.microservices.securityservice.configuration.ribbon;
//
//import com.netflix.client.config.IClientConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.client.HttpClient;
//import org.apache.http.conn.ssl.NoopHostnameVerifier;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.ssl.SSLContextBuilder;
//import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
//import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
//
//import javax.net.ssl.SSLContext;
//import java.security.GeneralSecurityException;
//
//@Slf4j
//public class GatewayRibbonLoadBalancingHttpClient extends RibbonLoadBalancingHttpClient {
//    /**
//     * @param config
//     * @param serverIntrospector
//     */
//    public GatewayRibbonLoadBalancingHttpClient(IClientConfig config, ServerIntrospector serverIntrospector) {
//        super(config, serverIntrospector);
//    }
//
//
//    @Override
//    protected CloseableHttpClient createDelegate(IClientConfig config) {
//        return (CloseableHttpClient) httpClient();
//    }
//
//    public static HttpClient httpClient() {
//        TrustSelfSignedStrategy trustStrategy = new TrustSelfSignedStrategy();
//
//        SSLContext sslContext = null;
//        try {
//            sslContext = new SSLContextBuilder().loadTrustMaterial(trustStrategy).build();
//        } catch (GeneralSecurityException e) {
//            log.error("Error initializing ssl context.", e);
//            throw new RuntimeException(e.getLocalizedMessage());
//
//        }
//        SSLConnectionSocketFactory socketFactory =
//                new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
//        return HttpClientBuilder.create().setSSLSocketFactory(socketFactory).build();
//    }
//
//}
