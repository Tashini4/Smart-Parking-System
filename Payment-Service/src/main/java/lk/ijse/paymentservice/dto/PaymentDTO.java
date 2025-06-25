package lk.ijse.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {
    private UUID paymentId;
    private String licensePlate;
    private String userEmail;
    private String parkingLocation;
    private int amount;
    private Date paymentDate;
    private Date bookingDate;
    private Time paymentTime;
}
