package uz.urinov.stadium.stadium.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.stadium.stadium.entity.FieldEntity;

public interface FieldRepository extends CrudRepository<FieldEntity, Integer> {
    // UPDATE District SET nameEn = :newName WHERE id = :districtId";

    @Query("UPDATE FieldEntity f SET f.visible=false WHERE f.ownerId=?1 ")
    Integer updateFirstByVisible(String ownerId);
}
