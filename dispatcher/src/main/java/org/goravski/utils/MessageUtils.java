package org.goravski.utils;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Log4j
public class MessageUtils {
    public SendMessage generateSendMessageWithText (Update update, String text){
        var message = update.getMessage();
        log.info("SendMessage generating" + message.getText());
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(text)
                .build();
    }
}
