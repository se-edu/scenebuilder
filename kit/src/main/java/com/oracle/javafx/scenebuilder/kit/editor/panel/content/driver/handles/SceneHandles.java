package com.oracle.javafx.scenebuilder.kit.editor.panel.content.driver.handles;

import com.oracle.javafx.scenebuilder.kit.editor.panel.content.ContentPanelController;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class SceneHandles extends AbstractGenericHandles<Scene> {
    private final Pane placeholderPane = new Pane();

    public SceneHandles(ContentPanelController contentPanelController,
                        FXOMInstance fxomInstance) {
        super(contentPanelController, fxomInstance, Scene.class);
    }

    @Override
    public Bounds getSceneGraphObjectBounds() {
        Scene scene = getSceneGraphObject();
        return new BoundingBox(0, 0, scene.getWidth(), scene.getHeight());
    }

    @Override
    public Node getSceneGraphObjectProxy() {
        Node displayRoot = getFxomObject().getFxomDocument().getDisplayRoot();
        return displayRoot != null ? displayRoot : placeholderPane;
    }

    @Override
    protected void startListeningToSceneGraphObject() {
    }

    @Override
    protected void stopListeningToSceneGraphObject() {
    }
}
