package org.example.gateway.filter;

import com.nimbusds.jose.JWSObject;
import io.micrometer.core.instrument.util.StringUtils;
import org.dssc.demo.common.api.ResultCode;
import org.dssc.demo.common.constant.Auth;
import org.dssc.demo.common.exception.Asserts;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(Auth.JWT_TOKEN_HEADER);
        if (StringUtils.isBlank(token)) {
            return chain.filter(exchange);
        }
        try {
            String accessToken = token.replace(Auth.JWT_TOKEN_PREFIX, "");
            JWSObject object = JWSObject.parse(accessToken);
            String userId = (String) object.getPayload().toJSONObject().get(Auth.JWT_TOKEN_USER);

            // 从token中解析出userId字段设置到header中
            ServerHttpRequest request = exchange.getRequest().mutate().header(Auth.JWT_TOKEN_USER, userId).build();
            exchange = exchange.mutate().request(request).build();
        } catch (ParseException e) {
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

