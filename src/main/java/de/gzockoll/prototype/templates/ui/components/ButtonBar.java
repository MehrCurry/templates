package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import de.gzockoll.prototype.templates.util.Command;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Guido.Zockoll on 10.03.2015.
 */
public class ButtonBar  extends HorizontalLayout {
    public ButtonBar(Collection<Command> commands) {
        setSizeUndefined();
        replaceButtons(commands);
    }

    private void replaceButtons(Collection<Command> commands) {
        removeAllComponents();
        commands.forEach(c -> {
            Button button = new Button(c.getName());
            button.addClickListener(e -> c.run());
            addComponent(button);
        });
    }

    public void replaceCommands(Collection<Command> commands) {
        replaceButtons(commands);
    }
}
