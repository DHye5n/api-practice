package com.dgm.board.service;


import com.dgm.board.exception.user.UserAlreadyExistsException;
import com.dgm.board.exception.user.UserNotFoundException;
import com.dgm.board.model.entity.UserEntity;
import com.dgm.board.model.user.User;
import com.dgm.board.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    private final BCryptPasswordEncoder passwordEncoder;

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
}
