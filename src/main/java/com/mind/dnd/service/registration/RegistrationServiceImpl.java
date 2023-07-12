package com.mind.dnd.service.registration;

import com.mind.dnd.model.User;
import com.mind.dnd.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;

    @Override
    public SendMessage registerUser(String chatId, long id, String name) {
        User user = User.builder().id(id).name(name).build();
        if (userRepository.findById(id).isPresent()) {
            return new SendMessage(chatId,"Пользователь с id " + id + " уже есть");
        } else {
            userRepository.save(user);
            return new SendMessage(chatId, "Пользователь " + name + " сохранен. ID: " + id);
        }
    }

    @Override
    public SendMessage getUser(String chatId, long id) {
        if (userRepository.findById(id).isPresent()) {
            User user = (userRepository.findById(id).get());
            return new SendMessage(chatId, "Привет! Ты зарегестрирован под id " + id + " под именем " + user.getName());
        } else {
            return new SendMessage(chatId, "Ты еще не зарегался, дружок");
        }
    }

}
