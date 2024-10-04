package com.jomariabejo.motorph.controller;

import java.util.function.Consumer;

public interface _ViewLoader {
    <T> void loadView(String fxmlPath, Consumer<T> controllerInitializer);
}
