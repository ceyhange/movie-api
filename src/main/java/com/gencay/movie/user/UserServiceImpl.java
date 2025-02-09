package com.gencay.movie.user;

import com.gencay.movie.exceptions.ApiRequestException;
import com.gencay.movie.role.Role;
import com.gencay.movie.services.FileUploaderService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.gencay.movie.enums.UserRole.USER;
import static com.gencay.movie.utils.constants.AppConfig.MAX_UPLOAD_AVATAR_SIZE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements IUserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUploaderService fileUploaderService;


    @Override
    public User saveUser(User user) {
        log.info("Saving new user to the Database");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(new Role(USER.name()));
        return userRepository.save(user);
    }


    @Override
    public User getUser(String username) {
        log.info("Fetching user {} ", username);
        return userRepository.findByUsername(username);
    }


    @Override
    public User getUserById(Long id) throws NotFoundException {
        log.info("Fetching user {} ", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("User Not Found"));
    }

    @Override
    public String uploadAvatar(MultipartFile uploadFile, String uploadPath) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getPrincipal().toString());
        if (user == null) {
            throw new ApiRequestException("User Not exist!");
        }

        if (uploadFile == null) {
            throw new ApiRequestException("Please select a file!");
        }


        if (uploadFile.getSize() > MAX_UPLOAD_AVATAR_SIZE) {
            throw new ApiRequestException("Your file is too large!");
        }

        user.setAvatar(uploadFile.getOriginalFilename());
        userRepository.save(user);
        return fileUploaderService.uploadFile(uploadFile, uploadPath);
    }

    @Override
    public User geUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }


    @Override
    public List<User> getUsers() {
        log.info("Fetching all the users");

        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.error("User Not found!");
            throw new ApiRequestException("User not found");
        } else {
            log.info("User found: {}", username);

        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


}

