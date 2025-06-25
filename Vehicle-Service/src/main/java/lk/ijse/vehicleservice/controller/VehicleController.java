package lk.ijse.vehicleservice.controller;

import lk.ijse.vehicleservice.dto.ResponseDTO;
import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.entity.Vehicle;
import lk.ijse.vehicleservice.service.VehicleService;
import lk.ijse.vehicleservice.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping(value = "/saveVehicle")
    public ResponseEntity<ResponseDTO> saveVehicle(@RequestBody VehicleDTO vehicleDTO) {
        try {
            int res = vehicleService.saveVehicle(vehicleDTO);
            switch(res) {
                case VarList.All_Ready_Added -> {
                    System.out.println("All ready Added vehicle :");
                    ResponseDTO response = new ResponseDTO(VarList.All_Ready_Added, "Vehicle Already Exists", null);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }
                case VarList.Created -> {
                    System.out.println("Create Vehicle Success");
                    return  ResponseEntity.ok((new ResponseDTO(VarList.Created, "Vehicle created successfully", vehicleDTO)));
                }
                case VarList.Not_Found -> {
                    System.out.println("Not found User : ...");
                    ResponseDTO response = new ResponseDTO(VarList.Not_Found, "Not found User", null);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }
                default -> {
                    System.out.println("Internal server error");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(VarList.Internal_Server_Error, "Error saving Vehicle", null));

                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    @PutMapping(value = "/updateVehicle")
    public ResponseEntity<ResponseDTO> updateVehicle(@RequestBody VehicleDTO vehicleDTO) {
        System.out.println("Update vehicle : ..." + vehicleDTO);
        try{
            int res = vehicleService.updateVehicle(vehicleDTO);
            switch (res){
                case VarList.Not_Found -> {
                    System.out.println("Not found vehicle : ...");
                    ResponseDTO response = new ResponseDTO(VarList.All_Ready_Added, "Not found Details", null);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

                }
                case VarList.Created -> {
                    System.out.println("Update Vehicle Success");
                    return  ResponseEntity.ok((new ResponseDTO(VarList.Created, "Vehicle Update successfully", vehicleDTO)));
                }
                default -> {
                    System.out.println("Internal server error");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(VarList.Internal_Server_Error, "Error update Vehicle", null));

                }

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    @DeleteMapping(value = "/deleteVehicle")
    public ResponseEntity<ResponseDTO> DeleteVehicle(@RequestBody VehicleDTO vehicleDTO) {
        System.out.println("Delete vehicle : ..." + vehicleDTO);
        try{
            int res = vehicleService.deleteVehicle(vehicleDTO);
            switch (res){
                case VarList.OK -> {
                    System.out.println("Delete Vehicle Success");
                    return  ResponseEntity.ok((new ResponseDTO(VarList.Created, "Vehicle Delete successfully", vehicleDTO)));
                }

                case VarList.Not_Found -> {
                    System.out.println("Not found vehicle : ...");
                    ResponseDTO response = new ResponseDTO(VarList.Not_Found, "Not found Vehicle", null);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }

                default -> {
                    System.out.println("Internal server error");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(VarList.Internal_Server_Error, "Error delete Vehicle", null));

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/getVehicleByPlateNumber")
    public ResponseEntity<ResponseDTO> getVehicleById(@RequestBody VehicleDTO vehicleDTO) {
        System.out.println("Get vehicle by plate number : ..." + vehicleDTO);
        try{
            VehicleDTO vehicleDTO1 = vehicleService.getVehicleByNumberPlate(vehicleDTO.getLicensePlate());

            if(vehicleDTO1 == null){
                return ResponseEntity.ofNullable(new ResponseDTO(VarList.Not_Found,"Not found vehicle",null));
            }else{
                return ResponseEntity.ok(new ResponseDTO(VarList.OK,"Vehicle details", vehicleDTO1));
            }}catch (Exception e){
            throw new RuntimeException();
        }
    }

    @GetMapping(value = "/getAllVehicle")
    public List<VehicleDTO> getAllVehicles() {
        System.out.println("Get all vehicles");
        List<VehicleDTO> vehicleDTOS = vehicleService.getAllVehicles();
        return vehicleDTOS;
    }
}
