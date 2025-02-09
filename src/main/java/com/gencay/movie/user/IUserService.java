package com.gencay.movie.user;

import javassist.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    User saveUser(User user);

    User getUser(String username);

    List<User> getUsers();

    User getUserById(Long id) throws NotFoundException;

    String uploadAvatar(MultipartFile uploadFile, String uploadPath);

    User geUserByEmail(String email);

}
