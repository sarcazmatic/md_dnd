package com.mind.dnd.service.start;

import com.mind.dnd.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StartServiceImpl implements StartService {

    private final ResponseService responseService;

    @Override
    public SendMessage startRequest(long chatId, String name) {
        String answer = "Чем займемся, " + name + "?";
        SendMessage sendMessage = responseService.makeAnswer(chatId, answer);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        InlineKeyboardButton buttonDice = InlineKeyboardButton.builder().text("Dice").callbackData("DICE").build();
        InlineKeyboardButton buttonChar = InlineKeyboardButton.builder().text("Char").callbackData("CHAR").build();
        InlineKeyboardButton buttonReg = InlineKeyboardButton.builder().text("Register").callbackData("REG").build();
        rowsInline.add(List.of(buttonDice, buttonChar, buttonReg));
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

}
