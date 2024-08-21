package uz.urinov.stadium.attach.repository;

import org.springframework.data.repository.CrudRepository;
import uz.urinov.stadium.attach.entity.AttachEntity;

import java.util.List;

public interface AttachRepository extends CrudRepository<AttachEntity,String> {

    List<AttachEntity> findAllByIdIn(List<String> ids);
}
