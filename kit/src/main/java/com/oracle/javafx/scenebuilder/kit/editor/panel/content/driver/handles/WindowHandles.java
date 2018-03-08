package com.oracle.javafx.scenebuilder.kit.editor.panel.content.driver.handles;

import com.oracle.javafx.scenebuilder.kit.editor.panel.content.ContentPanelController;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

public class WindowHandles extends AbstractGenericHandles<Window> {
    private final Pane placeholderPane = new Pane();

    public WindowHandles(ContentPanelController contentPanelController,
                         FXOMInstance fxomInstance) {
        super(contentPanelController, fxomInstance, Window.class);
    }

    @Override
    public Bounds getSceneGraphObjectBounds() {
        Window window = getSceneGraphObject();
        if (window == null) {
            return placeholderPane.getLayoutBounds();
        }
        return new BoundingBox(0, 0, window.getWidth(), window.getHeight());
    }

    @Override
    public Node getSceneGraphObjectProxy() {
        Node displayRoot = getFxomObject().getFxomDocument().getDisplayRoot();
        if (displayRoot == null) {
            displayRoot = placeholderPane;
        }
        return displayRoot;
    }

    @Override
    protected void startListeningToSceneGraphObject() {
    }

    @Override
    protected void stopListeningToSceneGraphObject() {
    }
}
