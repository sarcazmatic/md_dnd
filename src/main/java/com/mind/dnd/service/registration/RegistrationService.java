package com.mind.dnd.service.registration;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface RegistrationService {

    SendMessage getUser(String chatId, long id);

    SendMessage registerUser(String chatId, long id, String name);

}
