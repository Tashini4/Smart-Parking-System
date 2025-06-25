package lk.ijse.paymentservice.service;

import lk.ijse.paymentservice.dto.PaymentDTO;
import lk.ijse.paymentservice.dto.ResponseDTO;

import java.util.List;

public interface PaymentService {
    ResponseDTO savePaymentAndTransaction(PaymentDTO paymentDTO);

    List<PaymentDTO> getAllPayments();

    List<PaymentDTO> getAllPaymentBYEmail(String email);
}
