package com.appointmentservice.repository;
import com.appointmentservice.model.AppointmentEntryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentEntryRepository extends JpaRepository<AppointmentEntryModel,Integer> {
    //findAllByDoctorId -> for restricting the slots(5)
    //existsByDoctorId -> check whether Doctor Exists in DB (for Allocation)
    //findAllByDoctorIdAndDate -> getting the latest record for checking the Time to allocate another patient(For Allocation)
    List<AppointmentEntryModel> findAllByDoctorId(Integer doctorId);
    boolean existsByDoctorId(Integer doctorId);

    List<AppointmentEntryModel> findAllByDoctorIdAndDate(Integer doctorId, LocalDate date);

    boolean existsByDate(LocalDate date);
}
