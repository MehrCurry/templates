package de.gzockoll.prototype.templates.ui;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.gzockoll.prototype.templates.entity.AssetRepository;
import de.gzockoll.prototype.templates.ui.viewmodel.AssetViewModel;
import de.gzockoll.prototype.templates.ui.viewmodel.TemplateViewModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.vaadin.spring.annotation.VaadinUI;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@VaadinUI
@Theme("valo")
public class MainUI extends UI {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetViewModel assetViewModel;

    @Autowired
    private TemplateViewModel templateViewModel;

    ValoMenuLayout root = new ValoMenuLayout();
    ComponentContainer viewDisplay = root.getContentContainer();
    CssLayout menu = new CssLayout();
    CssLayout menuItemsLayout = new CssLayout();
    {
        menu.setId("testMenu");
    }
    private Navigator navigator;
    private final Map<String, MenuEntry> menuItems = new LinkedHashMap<>();
    private boolean testMode;

    @Override
    protected void init(VaadinRequest request) {
        Preconditions.checkState(assetRepository!=null);
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

        getPage().setTitle("Template Manager");
        setContent(root);
        root.setWidth("100%");

        root.addMenu(buildMenu());

        navigator = new Navigator(this, viewDisplay);
        navigator.addView("assets", assetViewModel.getAssetView());
        navigator.addView("templates", templateViewModel.getView());

        final String f = Page.getCurrent().getUriFragment();
        if (StringUtils.isEmpty(f)) {
            navigator.navigateTo("assets");
        }
        navigator.setErrorView(assetViewModel.getAssetView());

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
                for (final Map.Entry<String, MenuEntry> item : menuItems.entrySet()) {
                    if (event.getViewName().equals(item.getKey())) {
                        for (final Iterator<Component> it = menuItemsLayout
                                .iterator(); it.hasNext();) {
                            final Component c = it.next();
                            if (c.getCaption() != null
                                    && c.getCaption().startsWith(
                                    item.getValue().getText())) {
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
        // Add items
        menuItems.put("assets", MenuEntry.of("Assets", FontAwesome.UPLOAD));
        menuItems.put("templates", MenuEntry.of("Templates", FontAwesome.FILE_CODE_O));

        final HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName("valo-menu-title");
        menu.addComponent(top);

        final Button showMenu = new Button("Menu", event -> {
            if (menu.getStyleName().contains("valo-menu-visible")) {
                menu.removeStyleName("valo-menu-visible");
            } else {
                menu.addStyleName("valo-menu-visible");
            }
        });
        showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
        showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
        showMenu.addStyleName("valo-menu-toggle");
        showMenu.setIcon(FontAwesome.LIST);
        menu.addComponent(showMenu);

        final Label title = new Label(
                "<h3>PAYONE <strong>Template Manager</strong></h3>", ContentMode.HTML);
        title.setSizeUndefined();
        top.addComponent(title);
        top.setExpandRatio(title, 1);

        menuItemsLayout.setPrimaryStyleName("valo-menuitems");
        menu.addComponent(menuItemsLayout);

        for (final Map.Entry<String, MenuEntry> item : menuItems.entrySet()) {
            final Button b = new Button(item.getValue().getText(), event -> navigator.navigateTo(item.getKey()));
            b.setHtmlContentAllowed(true);
            b.setPrimaryStyleName("valo-menu-item");
            b.setIcon(item.getValue().getIcon());
            menuItemsLayout.addComponent(b);
        }
        menu.addStyleName("large-icons");
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

    @Data(staticConstructor="of")
    private static class MenuEntry {
        private final String text;
        private final FontIcon icon;
    }
}
