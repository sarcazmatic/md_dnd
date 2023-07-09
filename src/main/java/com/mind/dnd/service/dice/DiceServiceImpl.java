package com.mind.dnd.service.dice;

import com.mind.dnd.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiceServiceImpl implements DiceService {

    private final ResponseService responseService;

    private final Random random = new Random();

    public SendMessage diceRequest(long chatId, String name) {
        String answer = "Бросай куб, " + name + "!";
        SendMessage sendMessage = responseService.makeAnswer(chatId, answer);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardFormer());
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public SendMessage rollDice(String chatId, String diceType, Integer count) {
        int diceTypeAtIndex = diceType.lastIndexOf("D");
        int diceSides = Integer.parseInt(diceType.substring(diceTypeAtIndex + 1));
        int result = rollResult(count, diceSides);
        log.info("Бросок куба " + diceType + " " + count + " раз: " + result);
        StringBuilder reply = new StringBuilder("Бросок куба d" + diceSides);

        if (diceType.charAt(0) == '-' || diceType.charAt(0) == '+') {
            List<Integer> advThrowResult = new ArrayList<>();
            int resultAdv = rollResult(count, diceSides);
            advThrowResult.add(result);
            advThrowResult.add(resultAdv);
            advThrowResult.sort(Comparator.naturalOrder());
            log.info("Второй бросок " + diceType + " " + count + " раз: " + resultAdv);
            if (diceType.charAt(0) == '+') {
                advThrowResult.remove(0);
                reply.append(".\nADV: " + result + ", " + resultAdv);
            } else {
                advThrowResult.remove(1);
                reply.append(".\nDIS: " + result + ", " + resultAdv);
            }
            result = advThrowResult.get(0);
        }

        log.info("Итоговый результат " + result);
        reply.append(".\nРезультат: " + result);
        SendMessage diceRollReply = new SendMessage(chatId, reply.toString());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardFormer());
        diceRollReply.setReplyMarkup(inlineKeyboardMarkup);
        return diceRollReply;
    }

    public Integer rollResult(Integer count, int diceSides) {
        int result = 0;
        for (int i = 0; i < count; i++) {
            result += random.nextInt(diceSides) + 1;
        }
        return result;
    }

    private List<List<InlineKeyboardButton>> inlineKeyboardFormer() {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        InlineKeyboardButton buttonD4 = InlineKeyboardButton.builder().text("d4").callbackData("D4").build();
        InlineKeyboardButton buttonD6 = InlineKeyboardButton.builder().text("d6").callbackData("D6").build();
        InlineKeyboardButton buttonD8 = InlineKeyboardButton.builder().text("d8").callbackData("D8").build();
        InlineKeyboardButton buttonD10 = InlineKeyboardButton.builder().text("d10").callbackData("D10").build();
        InlineKeyboardButton buttonD12 = InlineKeyboardButton.builder().text("d12").callbackData("D12").build();
        InlineKeyboardButton buttonDisD20 = InlineKeyboardButton.builder().text("-").callbackData("-D20").build();
        InlineKeyboardButton buttonD20 = InlineKeyboardButton.builder().text("d20").callbackData("D20").build();
        InlineKeyboardButton buttonAdvD20 = InlineKeyboardButton.builder().text("+").callbackData("+D20").build();
        InlineKeyboardButton buttonD100 = InlineKeyboardButton.builder().text("d100").callbackData("D100").build();
        rowsInline.add(List.of(buttonDisD20, buttonD20, buttonAdvD20));
        rowsInline.add(List.of(buttonD4, buttonD6, buttonD8));
        rowsInline.add(List.of(buttonD10, buttonD12, buttonD100));
        return rowsInline;
    }

}
