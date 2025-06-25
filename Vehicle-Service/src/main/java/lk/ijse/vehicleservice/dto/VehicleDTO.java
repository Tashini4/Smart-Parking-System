package lk.ijse.vehicleservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDTO {
    private UUID id;
    private String licensePlate;
    private String model;
    private String email;
}
