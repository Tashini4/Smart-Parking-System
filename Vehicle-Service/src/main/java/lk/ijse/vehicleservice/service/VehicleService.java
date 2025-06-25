package lk.ijse.vehicleservice.service;

import lk.ijse.vehicleservice.dto.VehicleDTO;

import java.util.List;

public interface VehicleService {
    int saveVehicle(VehicleDTO vehicle);
    int updateVehicle(VehicleDTO vehicleDTO);
    int deleteVehicle(VehicleDTO vehicleDTO);
    List<VehicleDTO> getAllVehicles();
    VehicleDTO getVehicleByNumberPlate(String licensePlate);

}
