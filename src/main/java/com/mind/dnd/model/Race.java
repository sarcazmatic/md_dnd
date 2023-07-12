package com.mind.dnd.model;

import lombok.Getter;

@Getter
public enum Race {

    HUMAN("Человек"),
    HIGH_ELF("Высший эльф"),
    WOOD_ELF("Лесной эльф"),
    DROW("Дроу"),
    TIEFLING("Тифлинг"),
    MOUNT_DWARF("Горный дварф"),
    HILL_DWARF("Холмовой дварф"),
    STURDY_HALFLING("Коренастый полурослик"),
    LIGHTFOOT_HALFLING("Легконогий полурослик"),
    HALFORC("Полуорк"),
    HALFELF("Полуэльф"),
    GOLIATH("Голиаф"),
    GNOME("Гном"),
    FOREST_GNOME("Лесной гном"),
    DRAGONBORN("Драконорожденный");

    private String title;

    Race(String title) {
        this.title = title;
    }

}
