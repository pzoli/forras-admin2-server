package hu.infokristaly.keycloakauthenticatoin.controller;

import hu.infokristaly.keycloakauthenticatoin.entity.Doctor;
import hu.infokristaly.keycloakauthenticatoin.services.DoctorService;
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
    DoctorService doctorService;

    @GetMapping
    @PreAuthorize("hasRole('user') or hasRole('manager')")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping(path="/{doctorId}")
    @PreAuthorize("hasRole('user') or hasRole('manager')")
    public ResponseEntity<Doctor> getDoctor(@PathVariable(value = "doctorId") Long doctorId) {
        Doctor doctor = doctorService.getDoctor(doctorId);
        return doctor == null ? new ResponseEntity<Doctor>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(doctor);
    }

    @PostMapping
    @PreAuthorize("hasRole('manager')")
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.createDoctor(doctor);
    }

    @PutMapping
    @PreAuthorize("hasRole('manager')")
    public Doctor updateDoctors(@RequestBody Doctor doctor) {
        Doctor origin = doctorService.getDoctor(doctor.getId().longValue());
        if (origin == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor with id " + doctor.getId() + " does not exist");
        }
        return doctorService.updateDoctor(doctor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<?> deleteDoctors(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctor(id);
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            doctorService.deleteById(doctor.getId());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
