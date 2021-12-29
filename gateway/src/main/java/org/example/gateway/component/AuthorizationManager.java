package org.example.gateway.component;

import io.micrometer.core.instrument.util.StringUtils;
import org.dssc.demo.common.constant.Auth;
import org.example.gateway.config.WhiteListUrlsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Service
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private WhiteListUrlsConfig whiteListUrlsConfig;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        URI uri = request.getURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }

        List<String> ignoreUrls = whiteListUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, uri.getPath())) {
                return Mono.just(new AuthorizationDecision(true));
            }
        }

        // 如果token以"Bearer "为前缀，到此方法里说明JWT有效即已认证，其他前缀的token则拦截
        String token = request.getHeaders().getFirst(Auth.JWT_TOKEN_HEADER);
        if (StringUtils.isBlank(token) || !token.startsWith(Auth.JWT_TOKEN_PREFIX)) {
            return Mono.just(new AuthorizationDecision(false));
        }

        return Mono.just(new AuthorizationDecision(true));
    }
}

