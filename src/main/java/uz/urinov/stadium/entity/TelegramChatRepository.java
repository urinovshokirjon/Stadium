package uz.urinov.stadium.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TelegramChatRepository extends CrudRepository<TelegramChatEntity,Integer> {

    Optional<TelegramChatEntity> findByPhone(String phone);
}
