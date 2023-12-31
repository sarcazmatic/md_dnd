package com.mind.dnd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseService {

    public SendMessage makeAnswer(long chatId, String answer) {
        String chatIdString = Long.toString(chatId);
        return new SendMessage(chatIdString, answer);
    }

}
