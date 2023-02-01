package com.xzyler.microservices.netflixzuulapigatewayserver.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Not Used
 */
@Component
public class ModifyOauthRequestGatewayFilterFactory extends AbstractGatewayFilterFactory<ModifyOauthRequestGatewayFilterFactory.Config> {

    final Logger logger = LoggerFactory.getLogger(ModifyOauthRequestGatewayFilterFactory.class);

    public ModifyOauthRequestGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("defaultLocale");
    }

    /*@Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (exchange.getRequest()
                .getHeaders()
                .getAcceptLanguage()
                .isEmpty()) {

                String queryParamLocale = exchange.getRequest()
                    .getQueryParams()
                    .getFirst("locale");

                Locale requestLocale = Optional.ofNullable(queryParamLocale)
                    .map(l -> Locale.forLanguageTag(l))
                    .orElse(config.getDefaultLocale());

                exchange.getRequest()
                    .mutate()
                    .headers(h -> h.setAcceptLanguageAsLocales(Collections.singletonList(requestLocale)));
            }

            String allOutgoingRequestLanguages = exchange.getRequest()
                .getHeaders()
                .getAcceptLanguage()
                .stream()
                .map(range -> range.getRange())
                .collect(Collectors.joining(","));

            logger.info("Modify request output - Request contains Accept-Language header: {}", allOutgoingRequestLanguages);

            ServerWebExchange modifiedExchange = exchange.mutate()
                .request(originalRequest -> originalRequest.uri(UriComponentsBuilder.fromUri(exchange.getRequest()
                    .getURI())
                    .replaceQueryParams(new LinkedMultiValueMap<String, String>())
                    .build()
                    .toUri()))
                .build();

            logger.info("Removed all query params: {}", modifiedExchange.getRequest()
                .getURI());

            return chain.filter(modifiedExchange);
        };
    }*/

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            MultiValueMap<String, String> queryParams = getQueryParams(exchange.getRequest().getURI().getRawQuery());
            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .path("/asdfasdf")
                    .uri(URI.create(exchange.getRequest().getURI().getPath() + "?username=" + queryParams.getFirst("mobileno") + "&password=" + queryParams.getFirst("pin")))
                    .headers(httpHeaders -> {
                        exchange.getRequest().getHeaders().entrySet().forEach(h -> {
                            httpHeaders.put(h.getKey(), h.getValue());
                        });
                    })
//                    .header("username",queryParams.getFirst("mobileno"))
//                    .header("password",queryParams.getFirst("pin"))
                    .build();

            ServerWebExchange exchange1 = exchange.mutate().request(request).build();
            System.out.println(exchange1.getRequest().getHeaders().toString());
            System.out.println(exchange1.getRequest().getURI());
            return chain.filter(exchange1);

        };
    }

    public static class Config {
        private Locale defaultLocale;

        public Config() {
        }

        public Locale getDefaultLocale() {
            return defaultLocale;
        }

        public void setDefaultLocale(String defaultLocale) {
            this.defaultLocale = Locale.forLanguageTag(defaultLocale);
        }

        ;
    }

    public MultiValueMap<String, String> getQueryParams(String query) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap();
        if (query != null) {
            Matcher matcher = Pattern.compile("([^&=]+)(=?)([^&]+)?").matcher(query);

            while (matcher.find()) {
                String name = this.decodeQueryParam(matcher.group(1));
                String eq = matcher.group(2);
                String value = matcher.group(3);
                value = value != null ? this.decodeQueryParam(value) : (StringUtils.hasLength(eq) ? "" : null);
                queryParams.add(name, value);
            }

        }
        return queryParams;
    }

    private String decodeQueryParam(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Could not decode query value [" + value + "] as 'UTF-8'. Falling back on default encoding: " + var3.getMessage());
            }

            return URLDecoder.decode(value);
        }
    }

}
