package uz.urinov.stadium.stadium.repository;

import org.springframework.data.repository.CrudRepository;
import uz.urinov.stadium.stadium.entity.FieldAttachEntity;
import uz.urinov.stadium.stadium.entity.StadiumAttachEntity;

public interface StadiumAttachRepository extends CrudRepository<StadiumAttachEntity,String> {
}
