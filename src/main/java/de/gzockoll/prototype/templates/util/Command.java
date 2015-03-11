package de.gzockoll.prototype.templates.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode @Getter
public class Command<T> implements Runnable {
    private final Runnable function;
    @Getter private final T target;
    private String name;

    public Command(String name, T target, Runnable function) {

        this.name = name;
        this.function=function;
        this.target=target;
    }

    @Override
    public void run() {
        function.run();
    }
}
