package uz.urinov.stadium.stadium.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uz.urinov.stadium.stadium.entity.FieldEntity;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.enums.Status;

import java.util.List;
import java.util.Optional;

public interface FieldRepository extends CrudRepository<FieldEntity, Integer> {

    Optional<FieldEntity> findByIdAndOwnerId(Integer id, String ownerId);

    List<FieldEntity> findAllByStadiumIdAndVisibleTrueAndStatus(Integer stadiumId, Status status);

    Optional<FieldEntity> findByIdAndVisibleTrueAndStatus(Integer id,Status status);

    @Query("UPDATE FieldEntity f SET f.visible=false WHERE f.stadiumId=?1 ")
    Integer updateFirstByVisible(Integer stadiumId);


    @Query("SELECT CASE " +
            "WHEN AVG(f.averageRating) IS NULL THEN 0 " +
            "WHEN AVG(f.averageRating) >= 4.75 THEN 5 " +
            "WHEN AVG(f.averageRating) >= 4.25 THEN 4.5 " +
            "WHEN AVG(f.averageRating) >= 3.75 THEN 4 " +
            "WHEN AVG(f.averageRating) >= 3.25 THEN 3.5 " +
            "WHEN AVG(f.averageRating) >= 2.75 THEN 3 " +
            "ELSE 0.5 END " +
            "FROM FieldEntity f WHERE f.id = :fieldId")
    Double findRoundedAverageRatingByFieldId(@Param("fieldId") Integer fieldId);
}
