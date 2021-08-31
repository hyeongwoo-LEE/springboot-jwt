package com.cos.jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음
//login 요청해서 username, password 전송하면 (post)
//UsernamePasswordAuthenticationFilter 동작함 -> but formLogin.disable시 작동 x -> securityConfig 에 필터 등록

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("로그인 시도중");

        //1. username, password 받아서
        try {
            ObjectMapper om = new ObjectMapper();

            User user = om.readValue(request.getInputStream(), User.class);

            System.out.println(user);

            UsernamePasswordAuthenticationToken  authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            //PrincipalDetailsService -> loadUserByUsername 함수가 실행된 후 정상이면 authentication 이 리턴됨
            //DB에 있는 username과 password가 일치한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            //잘 찍히면 로그인이 정상적으로 되었다는 뜻.
            System.out.println("============");
            System.out.println(principalDetails.getUser().getUsername());


            //authentication 객체가 session 영역에 저장을 해야함 -> 그 방이 authentication 을 return 해줌.
            //리턴의 이유는 권한 관리를 security 가 대신 해주기 때문에 편하려고 하는 것임.
            //굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없지만 단지 권한 처리를 위해 session 에 넣어줌
            return authentication;
        } catch (IOException e) {
        }
        System.out.println("================================");

        //2. 정상인지 로그인 시도를 해보는것.  authenticationManager 로 로그인 시도를 하면!! PrincipalDetailsService 호출됨
        //PricipalDetailsService 호촐


        //3. PrincipalDetails 세션에 담고 -> 세션에 담지 않으면 권한 관리가 안됨 (권한 관리를 위해)

        //4. JWT 토큰 response
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println("successfulAuthentication");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization", "Bearer " + jwtToken);


    }

}
