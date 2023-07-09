package com.mind.dnd.service.dice;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface DiceService {

    SendMessage diceRequest(long chatId, String name);

    SendMessage rollDice(String chatId, String diceType, Integer count);

    Integer rollResult(Integer count, int diceSides);

}
