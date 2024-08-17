package uz.urinov.stadium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "Telegram_chat")
public class TelegramChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "phone_id")
    private String phone;

    @Column(name = "create_date")
    private LocalDate createDate=LocalDate.now();
}
