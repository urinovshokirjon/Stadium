package uz.urinov.stadium.Prifile;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, String> {

    // Resent Phone code
    Optional<ProfileEntity> findByPhoneAndVisibleTrue(String phone);

    // Phone number exist
    Boolean existsByPhone(String phone);
    // Profile login
    @Query("SELECT p FROM ProfileEntity AS p WHERE p.phone=?1 AND p.password=?2 AND p.visible=true AND p.status='ACTIVE'")
    Optional<ProfileEntity> findByPhoneAndPasswordAndVisibleTrueAndStatusActive(String phone, String password);

}
