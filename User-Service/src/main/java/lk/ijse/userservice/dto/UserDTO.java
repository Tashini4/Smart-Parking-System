package lk.ijse.userservice.dto;

import lk.ijse.userservice.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private Date createdAt;
}
