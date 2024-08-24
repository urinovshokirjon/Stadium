package uz.urinov.stadium.stadium.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.mapper.StadiumAttachMapper;

import java.util.List;
import java.util.Optional;

public interface StadiumRepository extends CrudRepository<StadiumEntity, Integer> {

    Optional<StadiumEntity> findByIdAndProfileId(Integer id, String profileId);

    @Query(value = "SELECT * FROM stadium s " +
            " WHERE s.visible = true " +
            " ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(s.lat)) * " +
            " cos(radians(s.lon) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.lat)))) ASC",
            nativeQuery = true)
    List<StadiumEntity> findClosestStadiums(@Param("lat") double lat, @Param("lon") double lon, Pageable pageable);

    @Query("SELECT s FROM StadiumEntity s " +
            "WHERE s.visible = true " +
            "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(s.lat)) * " +
            "cos(radians(s.lon) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.lat)))) ASC")
    List<StadiumEntity> findClosestStadiumPage(@Param("lat") double lat, @Param("lon") double lon, Pageable pageable);


    @Query(value = "SELECT * FROM stadium s " +
            " WHERE s.district_id IN (:districtIds) " +
            " ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(s.lat)) * " +
            "            cos(radians(s.lon) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.lat)))) ASC",nativeQuery = true)
    List<StadiumEntity> findClosestStadiumsInDistricts(@Param("districtIds") List<Integer> districtIds,
                                                             @Param("lat") Double lat,
                                                             @Param("lon") Double lon,
                                                             Pageable pageable);


}


