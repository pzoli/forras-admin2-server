package hu.infokristaly.keycloakauthenticatoin.repository;

import hu.infokristaly.keycloakauthenticatoin.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
