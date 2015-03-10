package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import de.gzockoll.prototype.templates.util.Command;

import java.util.Collection;

/**
 * Created by Guido.Zockoll on 10.03.2015.
 */
public class ButtonBar  extends HorizontalLayout {
    private Collection<Command> commands;

    public ButtonBar(Collection<Command> commands) {
        setSizeUndefined();
        this.commands = commands;
        commands.forEach(c -> {
            Button button = new Button(c.getName());
            button.addClickListener(e -> c.run());
            addComponent(button);
        });
    }
}
