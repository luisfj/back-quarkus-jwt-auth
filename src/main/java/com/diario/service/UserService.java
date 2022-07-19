package com.diario.service;

import com.diario.dto.auth.UserChangePasswordDTO;
import com.diario.dto.auth.UserInfoDTO;
import com.diario.dto.auth.UserRegisterDTO;
import com.diario.exception.AuthenticationPasswordException;
import com.diario.exception.AuthenticationUsernameException;
import com.diario.exception.model.GenericApplicationException;
import com.diario.model.User;
import com.diario.repository.UserRepository;
import com.diario.resource.auth.AuthenticationRequest;
import com.diario.serviceUtil.CryptoService;
import com.diario.valueobjects.Role;
import io.smallrye.jwt.build.Jwt;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.util.stream.Collectors;

@ApplicationScoped
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Inject))
public class UserService {

    private final UserRepository userRepository;
    private final CryptoService cryptoService;

    public String authenticate(final AuthenticationRequest authRequest)
            throws AuthenticationUsernameException, AuthenticationPasswordException {
        final User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(AuthenticationUsernameException::new);
        if (user.getPassword().equals(cryptoService.encrypt(authRequest.getPassword()))){
            return generateToken(user);
        }
        throw new AuthenticationPasswordException();
    }

    public UserInfoDTO getUserByEmail(String email){
        final User user = userRepository.findByEmail(email).get();
        UserInfoDTO dto = new UserInfoDTO(user.getEmail(), user.getImageUrl(), user.getName());
        return dto;
    }

    @Transactional
    public void save(UserRegisterDTO dto) {
        User user = new User(dto.getEmail(), cryptoService.encrypt(dto.getPassword()),
                dto.getFullName(), null, null);
        userRepository.persist(user);
    }

    @Transactional
    public void update(UserInfoDTO dto) {
        final User user = userRepository.findByEmail(dto.getEmail()).get();
        user.setName(dto.getName());
        user.setImageUrl(dto.getImageUrl());
        userRepository.persist(user);
    }

    private String generateToken(final User user) {
        return Jwt.issuer("https://diario.trade/issuer")
                .upn(user.getEmail())
                .expiresIn(Duration.ofDays(1))
                .issuer(user.getEmail())
                .groups(user.getRoles().stream().map(Role::getDescricao).collect(Collectors.toSet()))
                .sign();
    }


    @Transactional
    public void changePassword(UserChangePasswordDTO userDto, String email) throws GenericApplicationException {
        final User user = userRepository.findByEmail(email).get();
        if(!user.getPassword().equals(cryptoService.encrypt(userDto.getOldPassword())))
            throw new GenericApplicationException(Response.Status.FORBIDDEN, "Senha atual", "Senha atual incorreta");
        user.setPassword(cryptoService.encrypt(userDto.getPassword()));
        userRepository.persist(user);
    }
}