package com.mind.dnd.service;

import com.mind.dnd.service.registration.RegistrationService;
import com.mind.dnd.utility.BotCommands;
import com.mind.dnd.config.TelegramBotConfig;
import com.mind.dnd.service.dice.DiceService;
import com.mind.dnd.service.response.ResponseService;
import com.mind.dnd.service.start.StartService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotServiceImpl extends TelegramLongPollingBot {

    private final TelegramBotConfig botConfig;
    private final ResponseService responseService;
    private final DiceService diceService;
    private final StartService startService;
    private final BotCommands botCommands;
    private final RegistrationService registrationService;

    private static final String USER_GEN_RESPONSE = "Введите имя пользователя ответом на это сообщение.";
    private static final String CHAR_GEN_RESPONSE = "Введите имя персонажа ответом на это сообщение.";

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
            log.info(update.getMessage().getText());
            String userRequest = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            try {
                if (Optional.ofNullable(update.getMessage().getReplyToMessage()).orElseThrow(
                        () -> new RuntimeException("Сообщение с id {} не является ответом на сообщение.")
                ).getText().equals(USER_GEN_RESPONSE)) {
                    sendAnswer(registrationService.registerUser(String.valueOf(chatId),
                            update.getMessage().getFrom().getId(),
                            update.getMessage().getText()));
                }
            } catch (RuntimeException e) {
                log.info(e.getMessage(), update.getMessage().getMessageId());
            }

            if (userRequest.charAt(0) == '/') {
                switch (userRequest) {
                    case "/start" ->
                            sendAnswer(startService.startRequest(chatId, update.getMessage().getChat().getFirstName()));
                    case "/dice" ->
                            sendAnswer(diceService.diceRequest(chatId, update.getMessage().getChat().getFirstName()));
                    case "/char" ->
                            sendAnswer(responseService.makeAnswer(chatId, "Скоро будем тут персов делать, а твой ID: " + update.getMessage().getFrom().getId() + "."));
                    case "/reg" ->
                            sendAnswer(responseService.makeAnswer(chatId, USER_GEN_RESPONSE),
                                    update.getMessage().getMessageId()); //force answer to that message
                    case "/user" ->
                            sendAnswer(registrationService.getUser(String.valueOf(chatId), update.getMessage().getFrom().getId()));
                    default ->
                            sendAnswer(responseService.makeAnswer(chatId, "Вообще такой команды не знаю, соррь :("));
                }
            }

        } else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();

            switch (data) {
                case "D4", "D6", "D8", "D10", "D12", "D100", "D20", "-D20", "+D20" ->
                        sendAnswer(diceService.rollDice(chatId.toString(), data, 1));
                case "DICE" ->
                        sendAnswer(diceService.diceRequest(chatId, update.getCallbackQuery().getFrom().getFirstName()));
                case "CHAR" ->
                        sendAnswer(responseService.makeAnswer(chatId, "Скоро будем тут персов делать, а твой ID: " + update.getMessage().getFrom().getId() + "."));
                case "REG" ->
                        sendAnswer(responseService.makeAnswer(chatId, USER_GEN_RESPONSE),
                                update.getCallbackQuery().getMessage().getMessageId()); //force answer to that message
                default ->
                        sendAnswer(responseService.makeAnswer(chatId, "Вообще такой команды нет, не знаю, как так вышло :("));
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

    public void sendAnswer(SendMessage sendMessage, Integer messageIdToReplyTo) {
        try {
            sendMessage.setReplyToMessageId(messageIdToReplyTo);
            sendMessage.setReplyMarkup(new ForceReplyKeyboard());
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
