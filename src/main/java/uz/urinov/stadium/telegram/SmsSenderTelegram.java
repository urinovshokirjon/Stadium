package uz.urinov.stadium.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import uz.urinov.stadium.entity.TelegramChatEntity;
import uz.urinov.stadium.entity.TelegramChatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SmsSenderTelegram implements LongPollingSingleThreadUpdateConsumer {
    private TelegramClient telegramClient = new OkHttpTelegramClient("7362391012:AAFq2_IxOaffPRH_URdkJH2LbiGoj3-19Mk");

    @Autowired
    private TelegramChatRepository telegramChatRepository;

    @Override
    public void consume(Update update) {
        SendMessage sendMessage = null;

        if (update.getMessage().hasContact()) {
            Optional<TelegramChatEntity> byPhone = telegramChatRepository.findByPhone(update.getMessage().getContact().getPhoneNumber());

            TelegramChatEntity telegramChatEntity;
            if (byPhone.isPresent()) {
                telegramChatEntity = byPhone.get();
            } else {
                telegramChatEntity = new TelegramChatEntity();
            }
            telegramChatEntity.setPhone(update.getMessage().getContact().getPhoneNumber());
            telegramChatEntity.setChatId(update.getMessage().getChatId());
            telegramChatRepository.save(telegramChatEntity);
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            if (message_text.equals("/start")) {
                long chat_id = update.getMessage().getChatId();

                KeyboardButton contactButton = new KeyboardButton("Send contact");
                contactButton.setRequestContact(true);

                KeyboardRow row = new KeyboardRow();
                row.add(contactButton);

                List<KeyboardRow> keyboard = new ArrayList<>();
                keyboard.add(row);

                ReplyKeyboardMarkup markup = ReplyKeyboardMarkup.builder()
                        .keyboard(keyboard)
                        .resizeKeyboard(true)
                        .build();

                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text("Click 'Send contact' button!")
                        .replyMarkup(markup)
                        .build();

            }
            try {
                telegramClient.execute(sendMessage); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    public void sendConfirmSmsCode(String phone, String smsMessage) {
        Optional<TelegramChatEntity> byPhone = telegramChatRepository.findByPhone(phone);
        if (byPhone.isPresent()) {
            TelegramChatEntity telegramChatEntity = byPhone.get();
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(telegramChatEntity.getChatId())
                    .text(smsMessage)
                    .build();
            try {
                telegramClient.execute(sendMessage); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }
}
