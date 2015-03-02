package de.gzockoll.prototype.templates.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.springframework.util.StringUtils;
import org.vaadin.spring.annotation.VaadinUI;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@VaadinUI
@Theme("valo")
public class MainUI extends UI {

    ValoMenuLayout root = new ValoMenuLayout();
    ComponentContainer viewDisplay = root.getContentContainer();
    CssLayout menu = new CssLayout();
    CssLayout menuItemsLayout = new CssLayout();
    {
        menu.setId("testMenu");
    }
    private Navigator navigator;
    private final LinkedHashMap<String, String> menuItems = new LinkedHashMap<String, String>();
    private boolean testMode;

    @Override
    protected void init(VaadinRequest request) {
        if (request.getParameter("test") != null) {
            testMode = true;

            if (browserCantRenderFontsConsistently()) {
                getPage().getStyles().add(
                        ".v-app.v-app.v-app {font-family: Sans-Serif;}");
            }
        }
        if (getPage().getWebBrowser().isIE()
                && getPage().getWebBrowser().getBrowserMajorVersion() == 9) {
            menu.setWidth("320px");
        }
        if (!testMode) {
            Responsive.makeResponsive(this);
        }

        getPage().setTitle("Valo Theme Test");
        setContent(root);
        root.setWidth("100%");

        root.addMenu(buildMenu());

        navigator = new Navigator(this, viewDisplay);
        navigator.addView("common", CommonParts.class);

        final String f = Page.getCurrent().getUriFragment();
        if (StringUtils.isEmpty(f)) {
            navigator.navigateTo("common");
        }
        navigator.setErrorView(CommonParts.class);

        navigator.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                for (final Iterator<Component> it = menuItemsLayout.iterator(); it
                        .hasNext();) {
                    it.next().removeStyleName("selected");
                }
                for (final Map.Entry<String, String> item : menuItems.entrySet()) {
                    if (event.getViewName().equals(item.getKey())) {
                        for (final Iterator<Component> it = menuItemsLayout
                                .iterator(); it.hasNext();) {
                            final Component c = it.next();
                            if (c.getCaption() != null
                                    && c.getCaption().startsWith(
                                    item.getValue())) {
                                c.addStyleName("selected");
                                break;
                            }
                        }
                        break;
                    }
                }
                menu.removeStyleName("valo-menu-visible");
            }
        });

        viewDisplay.addComponent(getContentComponents());
    }

    private Component buildMenu() {
        final CssLayout menu = new CssLayout();
        menu.addStyleName("large-icons");

        final Label logo = new Label("Va");
        logo.setSizeUndefined();
        logo.setPrimaryStyleName("valo-menu-logo");
        menu.addComponent(logo);

        Button b = new Button(
                "Reference <span class=\"valo-menu-badge\">3</span>");
        b.setIcon(FontAwesome.FILE_CODE_O);
        b.setPrimaryStyleName("valo-menu-item");
        b.addStyleName("selected");
        b.setHtmlContentAllowed(true);
        menu.addComponent(b);

        b = new Button("API");
        b.setIcon(FontAwesome.FILE_PDF_O);
        b.setPrimaryStyleName("valo-menu-item");
        menu.addComponent(b);

        return menu;
    }

    private VerticalLayout getContentComponents() {
        VerticalLayout layout=new VerticalLayout();
        layout.addComponent(new Label("Hi!"));
        return layout;
    }

    private boolean browserCantRenderFontsConsistently() {
        // PhantomJS renders font correctly about 50% of the time, so
        // disable it to have consistent screenshots
        // https://github.com/ariya/phantomjs/issues/10592

        // IE8 also has randomness in its font rendering...

        return getPage().getWebBrowser().getBrowserApplication()
                .contains("PhantomJS")
                || (getPage().getWebBrowser().isIE() && getPage()
                .getWebBrowser().getBrowserMajorVersion() <= 9);
    }

}
