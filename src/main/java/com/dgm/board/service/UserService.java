package com.dgm.board.service;


import com.dgm.board.exception.user.UserAlreadyExistsException;
import com.dgm.board.exception.user.UserNotAllowedException;
import com.dgm.board.exception.user.UserNotFoundException;
import com.dgm.board.model.entity.UserEntity;
import com.dgm.board.model.user.User;
import com.dgm.board.model.user.UserAuthenticationResponse;
import com.dgm.board.model.user.UserPatchRequestBody;
import com.dgm.board.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public List<User> getUsers(String query) {
        List<UserEntity> userEntities;

        if (query != null && !query.isBlank()) {
            userEntities = userEntityRepository.findByUsernameContaining(query);
        } else {
            userEntities = userEntityRepository.findAll();
        }
        return userEntities.stream().map(User::from).collect(Collectors.toList());
    }

    public User getUser(String username) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        return User.from(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username));

    }

    public User signUp(String username, String password) {
        userEntityRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException();
                });

        UserEntity userEntity = userEntityRepository.save(UserEntity.of(username, passwordEncoder.encode(password)));

        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(String username, String password) {
        UserEntity userEntity = userEntityRepository
                .findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            String accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundException();
        }

    }

    public User updateUser(String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {
        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!userEntity.equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        if (userPatchRequestBody.getDescription() != null) {
            userEntity.setDescription(userPatchRequestBody.getDescription());
        }

        return User.from(userEntityRepository.save(userEntity));
    }
}
