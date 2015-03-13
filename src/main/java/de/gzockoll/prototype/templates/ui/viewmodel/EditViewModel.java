package de.gzockoll.prototype.templates.ui.viewmodel;

import de.gzockoll.prototype.templates.ui.view.EditView;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinUIScope;

@Component
@VaadinUIScope
@Slf4j
@Getter
public class EditViewModel {
    @Autowired
    private EditView view;

}
