package uz.urinov.stadium.Profile.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.stadium.Profile.entity.ProfileEntity;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, String> {

    // Resent Phone code
    Optional<ProfileEntity> findByPhoneAndVisibleTrue(String phone);

    // Phone number exist
    Optional<ProfileEntity> findByPhone(String phone);
    // Profile login
    @Query("SELECT p FROM ProfileEntity AS p WHERE p.phone=?1 AND p.password=?2 AND p.visible=true AND p.status='ACTIVE'")
    Optional<ProfileEntity> findByPhoneAndPasswordAndVisibleTrueAndStatusActive(String phone, String password);

}
