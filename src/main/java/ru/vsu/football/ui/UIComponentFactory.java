package ru.vsu.football.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UIComponentFactory {
    private final Map<String, UIComponent> componentMap;

    public UIComponent getUIComponent(String name) {
        return componentMap.get(name);
    }
}
