package org.goravski;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.goravski.controller.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@Log4j
public class TelegramBotApplication {
    private final ApplicationContext context;

    public TelegramBotApplication(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
    }

    @PostConstruct
    void getBeans() {
        try {
            log.info("getBeans");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(context.getBean(TelegramBot.class));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
