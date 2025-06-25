package lk.ijse.userservice.controller;

import lk.ijse.userservice.dto.ResponseDTO;
import lk.ijse.userservice.dto.UserDTO;
import lk.ijse.userservice.dto.UserDTO2;
import lk.ijse.userservice.service.UserService;
import lk.ijse.userservice.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/saveUser")
    public ResponseEntity<ResponseDTO> SaveUser(@RequestBody UserDTO user){
        try {
            Date date = Date.valueOf(LocalDate.now());
            user.setCreatedAt(date);
            System.out.println("User Data Come to Controller :" + user);

            int result = userService.saveUser(user);
            switch (result) {
                case VarList.Created -> {
                    System.out.println("User Data Saved Successfully");
                    return ResponseEntity.ok(new ResponseDTO(VarList.Created,"User saved successfully",user));
                }
                case VarList.All_Ready_Added -> {
                    System.out.println("User Already Exists");
                    ResponseDTO responseDTO = new ResponseDTO(VarList.All_Ready_Added,"User Already Exists",null);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDTO);
                }
                default -> {
                    System.out.println("User Already Exists");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(VarList.Internal_Server_Error,"Error saving User",null));
                }
            }
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @PutMapping(value = "/updateUser")
    public ResponseEntity<ResponseDTO> UpdateUser(@RequestBody UserDTO user){
        try {
            Date date = Date.valueOf(LocalDate.now());
            user.setCreatedAt(date);
            System.out.println("User Data Come to Controller :" + user);

            int result = userService.updateUser(user);
            switch (result) {
                case VarList.OK -> {
                    System.out.println("User Data Updated Successfully");
                    return ResponseEntity.ok(new ResponseDTO(VarList.OK,"User updated successfully",user));
                }
                case VarList.Not_Found -> {
                    System.out.println("User Not Found");
                    ResponseDTO responseDTO = new ResponseDTO(VarList.Not_Found,"User Not Found",null);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDTO);
                }
                default -> {
                    System.out.println("User Not exists");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(VarList.Internal_Server_Error,"Error saving User",null));
                }
            }
        }catch (Exception e){
            throw new RuntimeException();
        }
    }
    @GetMapping(value = "/getUser")
    public ResponseEntity<ResponseDTO> getMemberByUserID(@RequestBody UserDTO2 user){
        UserDTO userDTO = userService.getUserByEmail(user.getEmail());

        if(userDTO == null){
            return ResponseEntity.ok(
                    new ResponseDTO(VarList.Not_Found,"not found a member",null));
        }else{
            return ResponseEntity.ok(new ResponseDTO(VarList.OK,"Member detail",userDTO));

        }
    }

    @DeleteMapping(value = "/deleteUser")
    public ResponseEntity<ResponseDTO> deleteUser(@RequestBody UserDTO2 user){
        try {
            int res = userService.deleteUser(user.getEmail(),user.getPassword());
            switch (res) {
                case VarList.OK -> {
                    System.out.println("User Deleted Successfully");
                    return ResponseEntity.ok(new ResponseDTO(VarList.OK,"User deleted successfully",user));
                }
                case VarList.Not_Found -> {
                    System.out.println("User Not Found");
                    ResponseDTO responseDTO = new ResponseDTO(VarList.Not_Found,"User Not Found",null);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDTO);
                }
                default -> {
                    System.out.println("User Not exists");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(VarList.Internal_Server_Error,"Error saving User",null));
                }
            }
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @GetMapping(value = "/getAllUser")
    public List<UserDTO> getAllUsers(){
        List<UserDTO> userDTOS = userService.getAllUsers();
        return userDTOS;
    }
}
