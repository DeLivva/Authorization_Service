package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.ConfirmationToken;
import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.authorization_service.domain.UserState;
import com.vention.authorization_service.dto.request.UserLoginRequestDto;
import com.vention.authorization_service.dto.request.UserRegistrationRequestDTO;
import com.vention.authorization_service.dto.response.JwtResponse;
import com.vention.authorization_service.dto.response.UserRegistrationResponseDTO;
import com.vention.authorization_service.exception.ConfirmationTokenExpiredException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.exception.LoginFailedException;
import com.vention.authorization_service.mapper.SecurityCredentialMapper;
import com.vention.authorization_service.mapper.UserMapper;
import com.vention.authorization_service.service.AuthenticationService;
import com.vention.authorization_service.service.JwtService;
import com.vention.authorization_service.service.MailSendingService;
import com.vention.authorization_service.service.SecurityCredentialService;
import com.vention.authorization_service.service.UserRoleService;
import com.vention.authorization_service.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final SecurityCredentialService securityCredentialService;
    private final UserRoleService userRoleService;
    private final UserMapper userMapper;
    private final SecurityCredentialMapper credentialMapper;
    public final String DEFAULT_ROLE = "USER";
    private final MailSendingService mailSendingService;
    private final JwtService jwtService;
    private final CustomUserDetailsServiceImpl userDetailsService;


    @SneakyThrows
    @Override
    @Transactional
    public UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO request) {
        if (!userService.isEligibleForRegistration(request.getEmail())) {
            throw new DuplicateDataException("This email has already been registered!!!");
        }
        UserRoleEntity userRoleEntity = userRoleService.getByName(DEFAULT_ROLE);
        SecurityCredentialEntity savedCredentials = securityCredentialService.saveCredentials(
                credentialMapper.mapDataToSecurityCredentials(request.getPassword(), userRoleEntity)
        );
        UserEntity savedUser = userService
                .saveUser(userMapper.mapRegistrationRequestToUserEntity(request.getEmail(), savedCredentials));

        mailSendingService.sendConfirmationToken(savedUser);
        return UserRegistrationResponseDTO.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .build();
    }

    @Override
    public void confirmEmail(String token) {
        ConfirmationToken confirmationToken = mailSendingService.getConfirmationToken(token);
        if (confirmationToken.getExpiredAt().after(Timestamp.valueOf(LocalDateTime.now()))) {
            confirmationToken.setConfirmedAt(Timestamp.valueOf(LocalDateTime.now()));
            UserEntity user = confirmationToken.getUser();
            user.setUserState(UserState.VERIFIED);
            mailSendingService.saveToken(confirmationToken);
        } else {
            throw new ConfirmationTokenExpiredException("Confirmation token expired");
        }
    }

    @SneakyThrows
    @Override
    public void sendConfirmationToken(String email) {
        UserEntity user = userService.getByEmail(email);
        mailSendingService.sendConfirmationToken(user);
    }

    @Override
    public JwtResponse loginOAuth(OAuth2AuthenticationToken token) {
        String email = (String) token.getPrincipal().getAttributes().get("email");
        UserEntity user = userService.getByEmail(email);
        return checkUserState(user);
    }

    @Override
    public JwtResponse login(UserLoginRequestDto userLoginRequestDto) {
        String login = userLoginRequestDto.getLogin();
        // checks if email or username is used to log in
        if (login.contains("@")) {
            UserEntity userByEmail = userService.getByEmail(userLoginRequestDto.getLogin());
            if(userLoginRequestDto.getPassword().equals(userByEmail.getPassword())) {
                return checkUserState(userByEmail);
            } else {
                throw new LoginFailedException("Invalid email or password");
            }
        } else {
            UserEntity userByUsername = userService.getByUsername(userLoginRequestDto.getLogin());
            if(userLoginRequestDto.getPassword().equals(userByUsername.getPassword())) {
                return checkUserState(userByUsername);
            } else {
                throw new LoginFailedException("Invalid username or password");
            }
        }
    }

    private JwtResponse checkUserState(UserEntity user) {
        if (!user.getUserState().equals(UserState.AUTHORIZED)) {
            throw new LoginFailedException(user.getUserState().name());
        } else {
            return JwtResponse.builder().accessToken(jwtService.generateAccessToken(user)).build();
        }
    }

    @Override
    public void authenticate(String token, HttpServletRequest request) {
        Claims claims = jwtService.extractToken(token).getBody();
        String email = claims.getSubject();
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (jwtService.isTokenValid(token)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(),
                                null,
                                userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }
}