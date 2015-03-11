package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.event.Action;
import de.gzockoll.prototype.templates.util.Command;

import java.util.function.BiFunction;

import static com.google.common.base.Preconditions.checkState;

public class CommandAction<TARGET> extends Action {
    private final Operation<TARGET> operation;

    public CommandAction(String name,Operation<TARGET> operation) {
        super(name);
        this.operation=operation;
    }

    public void handle(Object t, Object u) {
        checkState(operation!=null);
        operation.apply(t, (TARGET) u);
    }

    public static interface Operation<T> {
        void apply(Object source,T target);
    }
}
