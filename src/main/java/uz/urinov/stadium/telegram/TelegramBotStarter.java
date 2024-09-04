package uz.urinov.stadium.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;


@Component
public class TelegramBotStarter implements CommandLineRunner {
    @Autowired
    SmsSenderTelegram smsSenderTelegram;

    public void startBot() {
        String botToken = TokenHolder.token;
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, smsSenderTelegram);
            System.out.println("SmsSenderTelegram successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(this::startBot);
        thread.start();
    }
}