/*
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.oracle.javafx.scenebuilder.kit.fxom;

import com.oracle.javafx.scenebuilder.kit.fxom.glue.GlueAuxiliary;
import com.oracle.javafx.scenebuilder.kit.fxom.glue.GlueDocument;
import com.oracle.javafx.scenebuilder.kit.fxom.glue.GlueInstruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javafx.fxml.FXMLLoader;

/**
 *
 * 
 */
class FXOMSaver {
    
    
    public String save(FXOMDocument fxomDocument) {
        
        assert fxomDocument != null;
        assert fxomDocument.getGlue() != null;
        
        if (fxomDocument.getFxomRoot() != null) {
            updateNameSpace(fxomDocument);
            updateImportInstructions(fxomDocument);
        }

        return fxomDocument.getGlue().toString();
    }
    
    
    /*
     * Private
     */
    
    private static final String NAME_SPACE_FX = "http://javafx.com/javafx/" + FXMLLoader.JAVAFX_VERSION;
    private static final String NAME_SPACE_FXML = "http://javafx.com/fxml/1";
    
    private void updateNameSpace(FXOMDocument fxomDocument) {
        assert fxomDocument.getFxomRoot() != null;
        
        final FXOMObject fxomRoot = fxomDocument.getFxomRoot();
        final String currentNameSpaceFX = fxomRoot.getNameSpaceFX();
        final String currentNameSpaceFXML = fxomRoot.getNameSpaceFXML();
        
        if ((currentNameSpaceFX == null) 
                || (currentNameSpaceFX.equals(NAME_SPACE_FX) == false)) {
            fxomRoot.setNameSpaceFX(NAME_SPACE_FX);
        }
        
        if ((currentNameSpaceFXML == null) 
                || (currentNameSpaceFXML.equals(NAME_SPACE_FXML) == false)) {
            fxomRoot.setNameSpaceFXML(NAME_SPACE_FXML);
        }
        
        
    }
        
    private void updateImportInstructions(FXOMDocument fxomDocument) {
        assert fxomDocument.getFxomRoot() != null;

        // gets list of the imports to be added to the FXML document.
        List<GlueInstruction> importList = getHeaderIncludes(fxomDocument);

        // synchronizes the glue with the list of glue instructions
        synchronizeHeader(fxomDocument.getGlue(), importList);
    }

    private List<GlueInstruction> getHeaderIncludes(FXOMDocument fxomDocument) {
        // Collects all the classes declared in the fxomdocument,
        // constructs the set of classes to be imported. No doubles allowed.
        final Set<String> imports = new TreeSet<>(); // Sorted
        fxomDocument.getFxomRoot().collectDeclaredClasses().forEach(dc -> {
            imports.add(dc.getName());
        });

        // adds java.lang package to imports;
        // is it needed?
        imports.add("java.lang.*");

        // Creates a List of glue instruction for each class that was declared.
        List<GlueInstruction> importsList = new ArrayList<>();
        imports.forEach(className -> {
            final GlueInstruction instruction = new GlueInstruction(fxomDocument.getGlue(), "import", className);
            importsList.add(instruction);
        });

        return importsList;
    }

    private void synchronizeHeader(GlueDocument glue, List<GlueInstruction> importList) {
        List<GlueAuxiliary> temp = glue.getHeader();
        synchronized (this) {
            glue.getHeader().clear();
            glue.getHeader().addAll(importList);

            if (!glue.getHeader().equals(importList))
                glue.getHeader().addAll(temp);
        }

    }
}
