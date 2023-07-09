package com.mind.dnd;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
public class BotCommands {

    private final BotCommand botCommandDice = new BotCommand("/dice", "Будем бросать дайсы!");
    private final BotCommand botCommandChar = new BotCommand("/char", "Будем собирать персонажа!");
    private final BotCommand botCommandReg = new BotCommand("/reg", "Регистрируйся, чувачок!");

    public List<BotCommand> getAllCommands() {
        return List.of(botCommandDice, botCommandChar, botCommandReg);
    }
}
