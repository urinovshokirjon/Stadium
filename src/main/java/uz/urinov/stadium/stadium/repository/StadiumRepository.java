package uz.urinov.stadium.stadium.repository;

import org.springframework.data.repository.CrudRepository;
import uz.urinov.stadium.stadium.entity.StadiumEntity;

import java.util.Optional;

public interface StadiumRepository extends CrudRepository<StadiumEntity, Integer> {

    Optional<StadiumEntity> findByIdAndProfileId(Integer id, String projectId);
}
