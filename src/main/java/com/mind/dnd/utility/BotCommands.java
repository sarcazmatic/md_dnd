package com.mind.dnd.utility;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
public class BotCommands {

    private final BotCommand botCommandDice = new BotCommand("/dice", "Будем бросать дайсы!");
    private final BotCommand botCommandChar = new BotCommand("/char", "Будем собирать персонажа!");
    private final BotCommand botCommandReg = new BotCommand("/reg", "Регистрируйся, чувачок!");
    private final BotCommand botCommandUser = new BotCommand("/user", "Взгляни на свою учетку");

    public List<BotCommand> getAllCommands() {
        return List.of(botCommandDice, botCommandChar, botCommandReg, botCommandUser);
    }
}
