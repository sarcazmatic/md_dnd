package com.mind.dnd.service.start;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface StartService {

    SendMessage startRequest(long chatId, String name);


}
