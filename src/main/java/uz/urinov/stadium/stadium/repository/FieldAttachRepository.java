package uz.urinov.stadium.stadium.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.stadium.stadium.entity.FieldAttachEntity;
import uz.urinov.stadium.stadium.entity.StadiumAttachEntity;

import java.util.List;

public interface FieldAttachRepository extends CrudRepository<FieldAttachEntity,String> {

    @Query(value = " SELECT fa.attachId FROM FieldAttachEntity AS fa WHERE fa.fieldId=?1")
    List<String> findAttachIds(Integer id);
}
