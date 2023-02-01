package com.xzyler.microservices.netflixzuulapigatewayserver;

import brave.sampler.Sampler;
import com.xzyler.microservices.netflixzuulapigatewayserver.filter.ModifyOauthRequestGatewayFilterFactory;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.RouteMetadataUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
@EnableDiscoveryClient
public class NetflixZuulApiGatewayServerApplication {

    private final String DEFAULT_USER = "xzyler";
    private final String DEFAULT_PASS = "xzyler@123";

    public static void main(String[] args) {
        SpringApplication.run(NetflixZuulApiGatewayServerApplication.class, args);
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, ModifyOauthRequestGatewayFilterFactory modifyOauthRequestGatewayFilterFactory) {
        return builder.routes()
                .route(p -> p.path("/login")
                        .filters(f ->
                                        f
                                                .rewritePath("/login", "/oauth/token")
                                                .addRequestHeader(HttpHeaders.AUTHORIZATION, "Basic " + new String(
                                                        Base64.encode(
                                                                (DEFAULT_USER + ":" + DEFAULT_PASS).getBytes(StandardCharsets.ISO_8859_1))
                                                ))
                                                .addRequestParameter("grant_type", "password")
                                                .modifyRequestBody(String.class, String.class, (exchange, originalRequest) -> {
                                                    String body = changeRequestBody(exchange, originalRequest, f);
                                                    return Mono.just(body);
                                                })
//                                                .hystrix(c -> c.setName("securityservice").setFallbackUri("forward:/fallback"))
                        ).uri("lb://security-service"))
                .route(p -> p.path("/security/**")
                        .filters(f ->
                                        f
                                                .rewritePath("/security/", "/")
//                         .filter(filterFactory.apply())
                                                .hystrix(c -> c.setName("security-service").setFallbackUri("forward:/fallback"))
                        ).uri("lb://security-service")
                        .metadata(RouteMetadataUtils.RESPONSE_TIMEOUT_ATTR, 2000)
                        .metadata(RouteMetadataUtils.CONNECT_TIMEOUT_ATTR, 2000))
                .route(p -> p.path("/blog/**")
                        .filters(f ->
                                        f
                                                .rewritePath("/blog/", "/")
//                         .filter(filterFactory.apply())
                                                .hystrix(c -> c.setName("blog-service").setFallbackUri("forward:/fallback"))
                        ).uri("lb://blog-service")
                        .metadata(RouteMetadataUtils.RESPONSE_TIMEOUT_ATTR, 2000)
                        .metadata(RouteMetadataUtils.CONNECT_TIMEOUT_ATTR, 2000))

                .build();
    }

    private String changeRequestBody(ServerWebExchange exchange, String originalRequest, GatewayFilterSpec f) {
        /**
         * change request param in realtime
         */
        //changeRequest if possible
//		((HttpMessageReader)serverCodecConfigurer.getReaders().stream().filter(
//				(reader) -> {
//					reader.
////					return reader.canRead(ResolvableType.forClassWithGenerics(MultiValueMap.class, new Class[]{String.class, Part.class}), MediaType.MULTIPART_FORM_DATA);
//				}));

        //log
//        ServerHttpRequest serverHttpRequest = exchange.getRequest();
//        ModifyOauthRequestGatewayFilterFactory modifyOauthRequestGatewayFilterFactory = new ModifyOauthRequestGatewayFilterFactory();
//		MultiValueMap<String, String> queryParams = modifyOauthRequestGatewayFilterFactory.getQueryParams(exchange.getRequest().getURI().getRawQuery());
//		exchange.getFormData().
//		Mono<MultiValueMap<String, String>> fooRequestBody = exchange.getFormData();
//		System.out.println(fooRequestBody.);
//		System.out.println(fooRequestBody.toString());

//		f.addRequestParameter("username","9851146448");
////		f.addRequestParameter("username",queryParams.getFirst("mobileno"));
//        f.addRequestParameter("password","12345");
//        f.addRequestParameter("password",queryParams.getFirst("pin"));

//        MBMobileUserPojo mbMobileUserPojo = MBMobileUserPojo.builder()
//                .mbl_IMEI(serverHttpRequest.getQueryParams().getFirst("IMEI"))
//                .date(new Date())
//                .databaseId(new Long(serverHttpRequest.getQueryParams().getFirst("databaseId")))
//                .mobileNo(serverHttpRequest.getQueryParams().getFirst("username"))
//                .build();
//        mobileLoginLogger.log(mbMobileUserPojo);

        //if request body is changed pass body as string
        return originalRequest;
    }
}
