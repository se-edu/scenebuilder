package com.oracle.javafx.scenebuilder.kit.editor.panel.content.driver.handles;

import com.oracle.javafx.scenebuilder.kit.editor.panel.content.ContentPanelController;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMObject;
import com.oracle.javafx.scenebuilder.kit.metadata.util.DesignHierarchyMask;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

public class WindowHandles extends AbstractGenericHandles<Window> {
    private final DesignHierarchyMask designHierarchyMask;
    private final Pane dummyPane = new Pane();

    public WindowHandles(ContentPanelController contentPanelController,
                         FXOMInstance fxomInstance) {
        super(contentPanelController, fxomInstance, Window.class);
        designHierarchyMask = new DesignHierarchyMask(fxomInstance);
    }

    @Override
    public Bounds getSceneGraphObjectBounds() {
        Node sceneRoot = getSceneRoot();
        return sceneRoot != null ? sceneRoot.getLayoutBounds() : new BoundingBox(0, 0, 0, 0);
    }

    @Override
    public Node getSceneGraphObjectProxy() {
        Node sceneRoot = getSceneRoot();
        return sceneRoot != null ? sceneRoot : dummyPane;
    }

    @Override
    protected void startListeningToSceneGraphObject() {
        Node sceneRoot = getSceneRoot();
        if (sceneRoot == null) {
            return;
        }
        startListeningToLayoutBounds(sceneRoot);
        startListeningToLocalToSceneTransform(sceneRoot);
    }

    @Override
    protected void stopListeningToSceneGraphObject() {
        Node sceneRoot = getSceneRoot();
        if (sceneRoot == null) {
            return;
        }
        stopListeningToLayoutBounds(sceneRoot);
        stopListeningToLocalToSceneTransform(sceneRoot);
    }

    private Node getSceneRoot() {
        FXOMObject scene = designHierarchyMask.getAccessory(DesignHierarchyMask.Accessory.SCENE);
        if (scene == null) {
            return null;
        }
        DesignHierarchyMask sceneDesignHierarchyMask = new DesignHierarchyMask(scene);
        FXOMObject root = sceneDesignHierarchyMask.getAccessory(DesignHierarchyMask.Accessory.ROOT);
        assert root != null;
        assert root instanceof FXOMInstance;
        assert root.getSceneGraphObject() instanceof Node;
        return (Node) root.getSceneGraphObject();
    }
}
