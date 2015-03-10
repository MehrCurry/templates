package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.event.Action;
import de.gzockoll.prototype.templates.util.Command;

import java.util.function.BiFunction;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created by Guido.Zockoll on 10.03.2015.
 */
public class CommandAction<TARGET,RETURN> extends Action implements BiFunction<Object,TARGET,RETURN> {
    private BiFunction<Object,TARGET,RETURN> function;

    public CommandAction(String name, BiFunction<Object, TARGET, RETURN> function) {
        super(name);
        this.function = function;
    }

    public CommandAction(String name, Command command) {
        super(name);
        function= (s, t) -> {
            command.run();
            return null;
        };
    }

    @Override
    public RETURN apply(Object t, Object u) {
        checkState(function!=null);
        return function.apply(t, (TARGET) u);
    }

    public void handle(Object sender, Object target) {
        apply(sender,target);
    }
}
