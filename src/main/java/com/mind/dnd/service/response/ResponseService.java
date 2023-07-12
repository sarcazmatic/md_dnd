package com.mind.dnd.service.response;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ResponseService {

    SendMessage makeAnswer(long chatId, String answer);

}
