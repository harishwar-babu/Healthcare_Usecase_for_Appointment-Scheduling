package com.patientservice.repository;
import com.patientservice.model.SignUpModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SignUpRepository extends JpaRepository<SignUpModel,Integer> {
     Boolean existsByUsernameAndPassword(String userName,String password);
}
