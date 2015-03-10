package de.gzockoll.prototype.templates.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode @Getter
public class Command implements Runnable {
    private final Runnable function;
    private String name;

    public Command(String name, Runnable function) {

        this.name = name;
        this.function=function;
    }

    @Override
    public void run() {
        function.run();
    }
}
