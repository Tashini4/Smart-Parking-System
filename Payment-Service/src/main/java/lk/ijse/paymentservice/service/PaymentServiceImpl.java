package lk.ijse.paymentservice.service;

import lk.ijse.paymentservice.dto.PaymentDTO;
import lk.ijse.paymentservice.dto.ResponseDTO;
import lk.ijse.paymentservice.entity.Parking;
import lk.ijse.paymentservice.entity.Payment;
import lk.ijse.paymentservice.repo.ParkingRepo;
import lk.ijse.paymentservice.repo.PaymentRepo;
import lk.ijse.paymentservice.util.VarList;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private ParkingRepo parkingRepo;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseDTO savePaymentAndTransaction(PaymentDTO paymentDTO) {
        System.out.println("Processing payment and transaction for location: " + paymentDTO.getParkingLocation());

        try {
            if (paymentDTO == null) {
                System.out.println("Error: Payment data is null");
                return new ResponseDTO(VarList.Bad_Request, "Payment data cannot be null", null);
            }

            Parking parking = parkingRepo.findByLocation(paymentDTO.getParkingLocation().toLowerCase());
            if (parking == null) {
                System.out.println("Error: Parking location not found - " + paymentDTO.getParkingLocation());
                return new ResponseDTO(VarList.Not_Found, "Parking location not found", null);
            }

            Payment payment = modelMapper.map(paymentDTO, Payment.class);
            payment.setAmount(parking.getPayAmount());

            if(parking.isAvailable()){
                Payment savedPayment = paymentRepo.save(payment);
                if (savedPayment == null) {
                    System.out.println("Error: Failed to save payment");
                    return new ResponseDTO(VarList.Conflict, "Failed to save payment", null);
                }


                parking.setAvailable(false);
                Parking updatedParking = parkingRepo.save(parking);
                if (updatedParking == null) {
                    System.out.println("Error: Failed to update parking status - rolling back payment");
                    paymentRepo.delete(savedPayment);
                    return new ResponseDTO(VarList.Conflict, "Payment processed but failed to update parking status", null);
                }

                System.out.println("Successfully processed payment ID: " + savedPayment.getPaymentId() +
                        " for parking: " + parking.getLocation());
                return new ResponseDTO(VarList.Created, "Transaction completed successfully", savedPayment);

            }
            return new ResponseDTO(VarList.Conflict, "Operation failed", null);

        } catch (DataAccessException e) {
            System.out.println("Database error while processing payment: " + e.getMessage());
            return new ResponseDTO(VarList.Conflict, "Database operation failed", null);
        } catch (Exception e) {
            System.out.println("Unexpected error while processing payment: " + e.getMessage());
            return new ResponseDTO(VarList.Internal_Server_Error, "An unexpected error occurred", null);
        }
    }


    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payment> paymentList = paymentRepo.findAll();
        return modelMapper.map(paymentList,new TypeToken<List<PaymentDTO>>(){}.getType());
    }

    @Override
    public List<PaymentDTO> getAllPaymentBYEmail(String email) {
        List<Payment> paymentList = paymentRepo.findAllByUserEmail(email);
        return modelMapper.map(paymentList,new TypeToken<List<PaymentDTO>>(){}.getType());
    }
}
