package lk.ijse.parkingservice.controller;

import lk.ijse.parkingservice.dto.ParkingDTO;
import lk.ijse.parkingservice.dto.ResponseDTO;
import lk.ijse.parkingservice.service.ParkingService;
import lk.ijse.parkingservice.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/parking")
public class ParkingController {
    @Autowired
    private ParkingService parkingService;

    @PostMapping(value = "/saveParking")
    public ResponseEntity<ResponseDTO> saveParkingPlace(@RequestBody ParkingDTO parkingDTO){
        System.out.println("saveParkingPlace");
        try {
            int res = parkingService.saveParkingPlace(parkingDTO);
            System.out.println("response code: " + res);
            switch(res){
                case VarList.Created ->{
                    System.out.println("Create Parking Success");
                    return  ResponseEntity.ok((new ResponseDTO(VarList.Created, "Parking created successfully", parkingDTO)));

                }
                case VarList.All_Ready_Added -> {
                    System.out.println("All ready Added Parking : ...");
                    ResponseDTO response = new ResponseDTO(VarList.All_Ready_Added, "User Already Exists", null);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }
                default -> {
                    System.out.println("Create Parking Failed");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(VarList.Internal_Server_Error, "Error saving Parking", null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(value = "/updateParking")
    public ResponseEntity<ResponseDTO> updateParkingPlace(@RequestBody ParkingDTO parkingDTO){
        System.out.println("updateParkingPlace........." + parkingDTO);
        try {
            int res = parkingService.updateParkingPlace(parkingDTO);
            switch(res){
                case VarList.Created ->{
                    System.out.println("update Parking Success");
                    return  ResponseEntity.ok((new ResponseDTO(VarList.Created, "Parking created successfully", parkingDTO)));

                }
                case VarList.Not_Found -> {
                    System.out.println("Parking Not Found");
                    ResponseDTO response = new ResponseDTO(VarList.Not_Found, "Parking Not Found", null);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }
                default -> {
                    System.out.println("update Parking Failed");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(VarList.Internal_Server_Error, "Error update Parking", null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping(value = "/deleteParking")
    public ResponseEntity<ResponseDTO> deleteParkingPlace(@RequestBody ParkingDTO parkingDTO){
        System.out.println("deleteParkingPlace");
        try{
            int res = parkingService.deleteParkingPlace(parkingDTO.getId(),parkingDTO.getEmail(),parkingDTO.getLocationCode());

            switch (res){
                case VarList.OK -> {
                    System.out.println("Delete ParkingSpace Success");
                    return  ResponseEntity.ok((new ResponseDTO(VarList.Created, "ParkingSpace Delete successfully", parkingDTO)));
                }

                case VarList.Not_Found -> {
                    System.out.println("Not found ParkingSpace : ...");
                    ResponseDTO response = new ResponseDTO(VarList.Not_Found, "Not found ParkingSpace", null);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }

                default -> {
                    System.out.println("Internal server error");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(VarList.Internal_Server_Error, "Error delete ParkingSpace", null));

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @GetMapping(value = "/getAllByCity")
    public List<ParkingDTO> getAllUseByCity(@RequestBody ParkingDTO parkingDTO){
        System.out.println("getAllByCity");
        List<ParkingDTO> parkingDTOS = parkingService.getParkingPlaceCity(parkingDTO.getCity());
        return parkingDTOS;
    }

    @GetMapping(value = "/getAll")
    public List<ParkingDTO> AllGetPlaces(){
        System.out.println("getAllByCity");
        List<ParkingDTO> parkingDTOS = parkingService.getAllParkingPlace();
        return parkingDTOS;
    }
}
