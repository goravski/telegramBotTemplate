package org.goravski.controller;

import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.goravski.service.impl.UpdateProducerImpl;
import org.goravski.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.goravski.model.RabbitQueue.*;

@Log4j
@Component
public class UpdateController {
    private MessageUtils messageUtils;
    private UpdateProducerImpl producer;
    private TelegramBot telegramBot;

    public UpdateController(MessageUtils messageUtils, UpdateProducerImpl producer) {
        this.messageUtils = messageUtils;
        this.producer = producer;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(@NonNull Update update) {
        String contentType = getContentType(update);
        switch (contentType) {
            case "Message" -> processMessageWithText(update);
            case "Entities" -> processMessageWithEntities(update);
            case "Document" -> processDocMessage(update);
            case "Photo" -> processPhotoMessage(update);
            case "Audio" -> processAudioMessage(update);
            case "" -> setUnsupportedMessageTypeView(update);
        }
    }

    private void processAudioMessage(Update update) {
        producer.produce(AUDIO_MESSAGE_UPDATE, update);
        sendObjectReceivedView(update);
    }



    private void processDocMessage(Update update) {
        producer.produce(DOC_MESSAGE_UPDATE, update);
        sendObjectReceivedView(update);
    }

    private void processPhotoMessage(Update update) {
        producer.produce(PHOTO_MESSAGE_UPDATE, update);
        sendObjectReceivedView(update);
    }

    private void processMessageWithEntities(Update update) {
        producer.produce(ENTITY_MESSAGE_UPDATE, update);
        sendObjectReceivedView(update);
    }

    private void processMessageWithText(Update update) {
        producer.produce(TEXT_MESSAGE_UPDATE, update);
        sendObjectReceivedView(update);
    }

    public String getContentType(@NonNull Update update) {
        Message message = update.getMessage();
        if (message.hasText()) {
            return "Message";
        } else if (message.hasEntities()) {
            return "Entities";
        } else if (message.hasDocument()) {
            return "Document";
        } else if (message.hasPhoto()) {
            return "Photo";
        } else if (message.hasAudio()) {
            return "Audio";
        } else {
            log.error("Unsupported type update =" + update);
            return "";
        }
    }
    private void sendObjectReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "File received! Processed...");
        setView(sendMessage);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Unsupported Message type");
        setView(sendMessage);
    }

    /**
     * Method for all answers
     *
     * @param sendMessage
     */
    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }
}