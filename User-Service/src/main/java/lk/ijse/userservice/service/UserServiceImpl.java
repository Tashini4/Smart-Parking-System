package lk.ijse.userservice.service;

import lk.ijse.userservice.dto.UserDTO;
import lk.ijse.userservice.entity.User;
import lk.ijse.userservice.repo.UserRepository;
import lk.ijse.userservice.util.VarList;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public  class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public int saveUser(UserDTO user) {
       try{
           if(userRepository.existsUserByEmail(user.getEmail())){
               return VarList.All_Ready_Added;
           }
           String email = user.getEmail().toLowerCase();
           System.out.println(email);
           user.setEmail(email);
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           System.out.println("Password: " + user.getPassword());
           User user2 = modelMapper.map(user, User.class);
           System.out.println("User2: " + user2);

           User user1 = userRepository.save(user2);
           System.out.println("new save user : " + user1);
           return VarList.Created;
       }catch(Exception e){
           System.out.println(e.getMessage());
       }
       return 0;
    }

    @Override
    public int updateUser(UserDTO user) {
        try{
            String email = user.getEmail().toLowerCase();

            if(userRepository.existsUserByEmail(email)){
                User user1 = userRepository.findAllByEmail(email);
                user1.setName(user.getName());
                user1.setEmail(email);
                user1.setRole(user.getRole());
                user1.setCreatedAt(user.getCreatedAt());
                user1.setPassword(passwordEncoder.encode(user.getPassword()));

                User user2 = userRepository.save(user1);
                System.out.println("new update user : " + user2);

                return VarList.OK;
            }
            return VarList.Not_Found;
        }catch(Exception e){
            throw new RuntimeException();
        }
    }

    @Override
    public int deleteUser(String email1, String password) {
        try {
            String email = email1.toLowerCase();
            String pw = password;

            if(userRepository.existsUserByEmail(email)){
                User user = userRepository.findAllByEmail(email);
                if(passwordEncoder.matches(pw, user.getPassword())){
                    userRepository.delete(user);
                    return VarList.OK;
                }
            }
            return VarList.Not_Found;
        }catch(Exception e){
            throw new RuntimeException();
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
            return modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());
    }

    @Override
    public UserDTO getUserByEmail(String email1) {
        String email = email1.toLowerCase();
        if(userRepository.existsUserByEmail(email)){
            User user = userRepository.findAllByEmail(email);
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }
}
