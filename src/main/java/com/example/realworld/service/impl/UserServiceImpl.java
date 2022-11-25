package com.example.realworld.service.impl;

import com.example.realworld.entity.User;
import com.example.realworld.exception.custom.CustomBadRequestException;
import com.example.realworld.exception.custom.CustomNotFoundException;
import com.example.realworld.model.CustomError;
import com.example.realworld.model.profile.dto.ProfileDTOResponse;
import com.example.realworld.model.user.dto.UserDTOCreate;
import com.example.realworld.model.user.dto.UserDTOLoginRequest;
import com.example.realworld.model.user.dto.UserDTOResponse;
import com.example.realworld.model.user.dto.UserDTOUpdate;
import com.example.realworld.model.user.mapper.UserMapper;
import com.example.realworld.repository.UserRepository;
import com.example.realworld.service.UserService;
import com.example.realworld.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Map<String, UserDTOResponse> authenticate(Map<String, UserDTOLoginRequest> userLoginRequestMap)
            throws CustomBadRequestException {
        UserDTOLoginRequest userDTOLoginRequest = userLoginRequestMap.get("user");

        Optional<User> userOptional = userRepository.findByEmail(userDTOLoginRequest.getEmail());

        boolean isAuthen = false;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(userDTOLoginRequest.getPassword(), user.getPassword())) {
                isAuthen = true;
            }
        }
        if (!isAuthen) {
            throw new CustomBadRequestException(
                    CustomError.builder().code("400").message("User name and password incorrect").build());
        }
        return buildDTOResponse(userOptional.get());
    }

    @Override
    public Map<String, UserDTOResponse> registerUser(Map<String, UserDTOCreate> userDTOCreateMap) {
        UserDTOCreate userDTOCreate = userDTOCreateMap.get("user");
        User user = UserMapper.toUser(userDTOCreate);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return buildDTOResponse(user);
    }

    @Override
    public Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();
        if(userLoggedIn != null){
            return buildDTOResponse(userLoggedIn);
        }
        throw new  CustomNotFoundException(CustomError.builder().code("404").message("User not exits").build());
    }

    @Override
    public Map<String, ProfileDTOResponse> getProfile(String username) throws CustomNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()){
            throw new CustomNotFoundException(CustomError.builder().code("404").message("User not found").build());
        }
        boolean isFollowing = checkFollowing(userOptional);
        return buildProfileResponse(userOptional.get(), isFollowing);
    }

    @Override
    public Map<String, ProfileDTOResponse> followUser(String username) throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()){
            throw new CustomNotFoundException(CustomError.builder().code("404").message("User not found").build());
        }
        User user = userOptional.get();
        boolean isFollowing = checkFollowing(userOptional);
        if(!isFollowing){
            user.getFollowers().add(userLoggedIn);
            user = userRepository.save(user);
            isFollowing = true;
        }
        return buildProfileResponse(user, isFollowing);
    }

    @Override
    public Map<String, ProfileDTOResponse> unfollowUser(String username) throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()){
            throw new CustomNotFoundException(CustomError.builder().code("404").message("User not found").build());
        }
        User user = userOptional.get();
        boolean isFollowing = checkFollowing(userOptional);
        if(isFollowing){
            user.getFollowers().remove(userLoggedIn);
            user = userRepository.save(user);
            isFollowing = false;
        }
        return buildProfileResponse(user, isFollowing);
    }

    @Override
    public User getUserLoggedIn(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            String email = ((UserDetails) principal).getUsername();
            User user = userRepository.findByEmail(email).get();
            return user;
        }
        return null;
    }

    @Override
    public Map<String, UserDTOResponse> updateCurrentUser(Map<String, UserDTOUpdate> userDTOUpdateMap) throws CustomNotFoundException {
        User user = getUserLoggedIn();
        UserDTOUpdate userDTOUpdate = userDTOUpdateMap.get("user");
        if(user != null){
            UserMapper.toUser(user, userDTOUpdate);
            user = userRepository.save(user);
            return buildDTOResponse(user);
        }
        throw new CustomNotFoundException(CustomError.builder().code("404").message("User not found").build());
    }

    private Map<String, ProfileDTOResponse> buildProfileResponse(User user, boolean isFollowing) {
        Map<String, ProfileDTOResponse> wrapper = new HashMap<>();
        //Con thieu following>>>>>>>>>>>>>>>
        ProfileDTOResponse profileDTOResponse =  ProfileDTOResponse.builder().username(user.getUsername())
                .bio(user.getBio()).image(user.getImage()).following(isFollowing).build();
        wrapper.put("profile", profileDTOResponse);
        return wrapper;
    }

    //Lap code nen tao ham nay
    private Map<String, UserDTOResponse> buildDTOResponse(User user) {
        Map<String, UserDTOResponse> wrapper = new HashMap<>();
        UserDTOResponse userDTOResponse = UserMapper.toUserDTOResponse(user);
        userDTOResponse.setToken(jwtTokenUtil.generateToken(user, 24 * 60 * 60));
        wrapper.put("user", userDTOResponse);
        return wrapper;
    }

    private boolean checkFollowing(Optional<User> userOptional){
        User userLoggedIn = getUserLoggedIn();
        User user = userOptional.get();
        Set<User> followers = user.getFollowers();
        for(User u: followers){
            if(u.getId() == userLoggedIn.getId()){
                return true;
            }
        }
        return false;
    }

}
