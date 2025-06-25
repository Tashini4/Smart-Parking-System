package lk.ijse.paymentservice.controller;

import lk.ijse.parkingservice.dto.ParkingDTO;
import lk.ijse.paymentservice.dto.PaymentDTO;
import lk.ijse.paymentservice.dto.ResponseDTO;
import lk.ijse.paymentservice.service.PaymentService;
import lk.ijse.paymentservice.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/savePayment")
    public ResponseEntity<ResponseDTO> savePayment(@RequestBody PaymentDTO paymentDTO) {
        System.out.println("controller savePayment...........");
        try {
            Date date = Date.valueOf(LocalDate.now());
            LocalTime time = LocalTime.now();
            System.out.println("date        "+date);
            System.out.println("time        "+time);

            paymentDTO.setPaymentDate(date);
            paymentDTO.setPaymentTime(Time.valueOf(time));
            paymentDTO.setBookingDate(date);
            ResponseDTO res = paymentService.savePaymentAndTransaction(paymentDTO);

            switch(res.getCode()) {
                case VarList.Created -> {
                    System.out.println("Payment Transaction is Successfully created");
                    return ResponseEntity.ok(new ResponseDTO(VarList.Created, res.getMessage(), res.getData()));
                }
                case VarList.Not_Found -> {
                    System.out.println("Resource not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, res.getMessage(), null));
                }
                case VarList.Conflict -> {
                    System.out.println("Conflict occurred");
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ResponseDTO(VarList.Conflict, res.getMessage(), null));
                }
                default -> {
                    System.out.println("Unexpected error occurred");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(VarList.Internal_Server_Error, "Unexpected error occurred", null));
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Internal server error", null));
        }
    }

    @GetMapping(value = "/getPaymentUseByEmail")
    public List<PaymentDTO> AllPaymentByEmail(ParkingDTO parkingDTO) {
        System.out.println("getAllByEmail");
        List<PaymentDTO> paymentDTOS = paymentService.getAllPaymentBYEmail(parkingDTO.getEmail());
        return paymentDTOS;
    }
    @GetMapping(value = "/getAll")
    public List<PaymentDTO> getAll() {
        System.out.println("getAllPayment");
        List<PaymentDTO> paymentDTOS = paymentService.getAllPayments();
        return paymentDTOS;
    }
}
