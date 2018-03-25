package com.oracle.javafx.scenebuilder.kit.editor.panel.content.driver.handles;

import com.oracle.javafx.scenebuilder.kit.editor.panel.content.ContentPanelController;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import com.oracle.javafx.scenebuilder.kit.metadata.util.DesignHierarchyMask;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class SceneHandles extends AbstractGenericHandles<Scene> {
    private final DesignHierarchyMask designHierarchyMask;

    public SceneHandles(ContentPanelController contentPanelController,
                        FXOMInstance fxomInstance) {
        super(contentPanelController, fxomInstance, Scene.class);
        designHierarchyMask = new DesignHierarchyMask(getFxomObject());
    }

    @Override
    public Bounds getSceneGraphObjectBounds() {
        return getSceneRoot().getLayoutBounds();
    }

    @Override
    public Node getSceneGraphObjectProxy() {
        return getSceneRoot();
    }

    @Override
    protected void startListeningToSceneGraphObject() {
        Node sceneRoot = getSceneRoot();
        startListeningToLayoutBounds(sceneRoot);
        startListeningToLocalToSceneTransform(sceneRoot);
    }

    @Override
    protected void stopListeningToSceneGraphObject() {
        Node sceneRoot = getSceneRoot();
        stopListeningToLayoutBounds(sceneRoot);
        stopListeningToLocalToSceneTransform(sceneRoot);
    }

    private Node getSceneRoot() {
        FXOMObject root = designHierarchyMask.getAccessory(DesignHierarchyMask.Accessory.ROOT);
        assert root != null;
        assert root instanceof FXOMInstance;
        assert root.getSceneGraphObject() instanceof Node;
        return (Node) root.getSceneGraphObject();
    }
}
