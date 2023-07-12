package com.mind.dnd.model;

import lombok.Getter;

@Getter
public enum CharClass {

    WARRIOR("Воин"),
    BARBARIAN("Варвар"),
    PALADIN("Паладин"),
    RANGER("Следопыт"),
    ROGUE("Плут"),
    DRUID("Друид"),
    WIZARD("Волшебник"),
    SORCERER("Чародей"),
    WARLOCK("Колдун"),
    MONK("Монах"),
    BARD("Бард"),
    CLERIC("Жрец");

    private String title;

    CharClass(String title) {
        this.title = title;
    }

}
