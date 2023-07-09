package com.mind.dnd.service;

import com.mind.dnd.BotCommands;
import com.mind.dnd.config.TelegramBotConfig;
import com.mind.dnd.service.dice.DiceService;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotServiceImpl extends TelegramLongPollingBot {

    private final TelegramBotConfig botConfig;
    private final ResponseService responseService;
    private final DiceService diceService;
    private final BotCommands botCommands;

    @PostConstruct
    private void startUp() {
        try {
            execute(new SetMyCommands(botCommands.getAllCommands(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String userRequest = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (userRequest) {
                case "/start":
                    sendAnswer(new SendMessage(String.valueOf(chatId), "Привет! В этом боте можно кидать кубы и вести учет персонажей."));
                    break;
                case "/dice":
                    sendAnswer(diceService.diceRequest(chatId, update.getMessage().getChat().getFirstName()));
                    break;
                case "/char":
                    sendAnswer(responseService.makeAnswer(chatId, "Скоро будем тут персов делать"));
                    break;
                case "/reg":
                    sendAnswer(responseService.makeAnswer(chatId, "Скоро тут появится регистрация"));
                    break;
                default:
                    sendAnswer(responseService.makeAnswer(chatId, "Command unrecognized"));
            }
        } else if (update.hasCallbackQuery()) {
            String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
            String data = update.getCallbackQuery().getData();
            if (data.equals("D4") || data.equals("D6") || data.equals("D8") || data.equals("D10")
                    || data.equals("D12") || data.equals("D100")
                    || data.equals("D20") || data.equals("-D20") || data.equals("+D20")) {
                sendAnswer(diceService.rollDice(chatId, data, 1));
            } else if (data.equals("DICE")) {
                sendAnswer(responseService.makeAnswer(Long.parseLong(chatId), "Тут уже можно кидать, осталось связку сделать."));
            } else if (data.equals("CHAR")) {
                sendAnswer(responseService.makeAnswer(Long.parseLong(chatId), "Скоро будем тут персов делать"));
            }
        }

    }

    public void sendAnswer(SendMessage sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
