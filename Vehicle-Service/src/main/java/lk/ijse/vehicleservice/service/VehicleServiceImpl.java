package lk.ijse.vehicleservice.service;

import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.entity.Vehicle;
import lk.ijse.vehicleservice.repo.UserRepository;
import lk.ijse.vehicleservice.repo.VehicleRepo;
import lk.ijse.vehicleservice.util.VarList;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int saveVehicle(VehicleDTO vehicle) {
        try {
            if(userRepo.existsUserByEmail(vehicle.getEmail().toLowerCase())){
                if(vehicleRepo.existsVehicleByLicensePlate(vehicle.getLicensePlate())){
                    return VarList.All_Ready_Added;
                }
                Vehicle vehicleEntity = modelMapper.map(vehicle, Vehicle.class);
                String email = vehicle.getEmail().toLowerCase();
                vehicleEntity.setEmail(email);
                System.out.println("vehicleEntity : saveVehicleEntity : " + vehicleEntity);
                Vehicle vehicleEntity1 = vehicleRepo.save(vehicleEntity);
                System.out.println("Vehicle save successfully");
                return VarList.Created;
            }
            return VarList.Not_Found;
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @Override
    public int updateVehicle(VehicleDTO vehicleDTO) {
        try {
            if(userRepo.existsUserByEmail(vehicleDTO.getEmail().toLowerCase())){
                if(vehicleRepo.existsVehicleByLicensePlate(vehicleDTO.getLicensePlate())){
                    Vehicle vehicleEntity = vehicleRepo.findByLicensePlate(vehicleDTO.getLicensePlate());
                    if(vehicleEntity != null && vehicleDTO.getEmail().equals(vehicleEntity.getEmail())){
                        vehicleEntity.setLicensePlate(vehicleDTO.getLicensePlate());
                        vehicleEntity.setModel(vehicleDTO.getModel());
                        vehicleEntity.setEmail(vehicleDTO.getEmail().toLowerCase());
                        Vehicle vehicleEntity1 = vehicleRepo.save(vehicleEntity);
                        return VarList.Created;
                    }
                    return VarList.Not_Found;
                }
            }
            return VarList.Not_Found;
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @Override
    public int deleteVehicle(VehicleDTO vehicleDTO) {
       try {
           if(userRepo.existsUserByEmail(vehicleDTO.getEmail().toLowerCase())){
               if(vehicleRepo.existsVehicleByLicensePlate(vehicleDTO.getLicensePlate())){
                   Vehicle vehicleEntity = vehicleRepo.findByLicensePlate(vehicleDTO.getLicensePlate());
                   vehicleRepo.delete(vehicleEntity);
                   System.out.println("Vehicle deleted");
                   return VarList.OK;
               }
               return VarList.Not_Found;
           }
           return VarList.Not_Found;
       }catch (Exception e){
           System.out.println("exception inside VehicleServiceImpl.deleteVehicle :" + e.getMessage());
           throw new RuntimeException();
       }
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        System.out.println("Get all vehicle ");
        List<Vehicle> vehicles = vehicleRepo.findAll();
        return modelMapper.map(vehicles,new TypeToken<List<VehicleDTO>>() {}.getType());
    }

    @Override
    public VehicleDTO getVehicleByNumberPlate(String licensePlate) {
        System.out.println("get vehicle use by number plate : " +licensePlate);
        if(vehicleRepo.existsVehicleByLicensePlate(licensePlate)){
            Vehicle vehicle = vehicleRepo.findByLicensePlate(licensePlate);
            return modelMapper.map(vehicle,VehicleDTO.class);
        }
        return null;
    }
}
