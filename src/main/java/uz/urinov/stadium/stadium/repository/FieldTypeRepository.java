package uz.urinov.stadium.stadium.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.stadium.stadium.entity.FieldTypeEntity;

import java.util.List;
import java.util.Optional;

public interface FieldTypeRepository extends CrudRepository<FieldTypeEntity, Integer> {

    // 3. FieldType list
    @Query("FROM FieldTypeEntity WHERE visible=true ORDER BY orderNumber ")
    List<FieldTypeEntity> findAllByVisibleTrueOrderByOrderNumber();

    Optional<FieldTypeEntity> findByIdAndVisibleTrue(Integer id);
}
