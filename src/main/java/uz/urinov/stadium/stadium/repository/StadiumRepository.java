package uz.urinov.stadium.stadium.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uz.urinov.stadium.stadium.entity.FieldEntity;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.enums.Status;
import uz.urinov.stadium.stadium.mapper.StadiumAttachMapper;

import java.util.List;
import java.util.Optional;

public interface StadiumRepository extends CrudRepository<StadiumEntity, Integer> {

    Optional<StadiumEntity> findByIdAndProfileIdAndVisibleTrue(Integer id, String profileId);

    Optional<StadiumEntity> findByIdAndVisibleTrue(Integer id);



    @Query(value = "SELECT * FROM stadium s " +
            " WHERE s.visible = true AND s.visible AND s.status='ACTIVE'" +
            " ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(s.lat)) * " +
            " cos(radians(s.lon) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.lat)))) ASC",
            nativeQuery = true)
    List<StadiumEntity> findClosestStadiums(@Param("lat") double lat, @Param("lon") double lon, Pageable pageable);

    @Query("SELECT s FROM StadiumEntity s " +
            " WHERE s.visible AND s.status=:status " +
            " ORDER BY s.createDate ASC ")
    List<StadiumEntity> listStatusStadium(@Param("status") Status status, Pageable pageable);

    @Query("SELECT f FROM FieldEntity f " +
            " WHERE f.visible AND f.status=:status " +
            " ORDER BY f.createDate ASC ")
    List<FieldEntity> listStatusField(@Param("status") Status status, Pageable pageable);


    @Query(value = "SELECT * FROM stadium s " +
            " WHERE s.district_id IN (:districtIds) AND s.visible AND s.status='ACTIVE' " +
            " ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(s.lat)) * " +
            "            cos(radians(s.lon) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.lat)))) ASC",nativeQuery = true)
    List<StadiumEntity> findClosestStadiumsInDistricts(@Param("districtIds") List<Integer> districtIds,
                                                             @Param("lat") Double lat,
                                                             @Param("lon") Double lon,
                                                             Pageable pageable);




}


