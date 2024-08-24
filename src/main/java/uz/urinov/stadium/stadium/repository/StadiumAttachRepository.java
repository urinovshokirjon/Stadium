package uz.urinov.stadium.stadium.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.stadium.stadium.entity.FieldAttachEntity;
import uz.urinov.stadium.stadium.entity.StadiumAttachEntity;

import java.util.List;

public interface StadiumAttachRepository extends CrudRepository<StadiumAttachEntity,String> {

    @Query(value = " SELECT sa.attachId FROM StadiumAttachEntity AS sa WHERE sa.stadiumId=?1")
    List<String> findAttachIds(Integer id);
}
