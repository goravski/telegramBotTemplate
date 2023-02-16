package org.goravski.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProducer {
    public void produce(String rabbitQueue, Update update);
}
