package hu.infokristaly.keycloakauthenticatoin.controller;

import hu.infokristaly.keycloakauthenticatoin.entity.Client;
import hu.infokristaly.keycloakauthenticatoin.entity.Doctor;
import hu.infokristaly.keycloakauthenticatoin.repository.DoctorRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@SecurityRequirement(name = "Keycloak")
public class DoctorController  {
    @Autowired
    DoctorRepository doctorRepository;

    @GetMapping
    @PreAuthorize("hasRole('user') or hasRole('manager')")
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('manager')")
    public Doctor createDoctors(@RequestBody Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @PutMapping
    @PreAuthorize("hasRole('manager')")
    public Doctor updateDoctors(@RequestBody Doctor doctor) {
        Doctor origin = doctorRepository.findById(doctor.getId().longValue()).orElse(null);
        if (origin == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor with id " + origin.getId() + " does not exist");
        }
        return doctorRepository.save(doctor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<?> deleteDoctors(@PathVariable Long id) {

        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        try {
            doctorRepository.deleteById(id);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.FAILED_DEPENDENCY);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
