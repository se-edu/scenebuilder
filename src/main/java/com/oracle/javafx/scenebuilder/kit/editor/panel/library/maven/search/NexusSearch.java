package com.oracle.javafx.scenebuilder.kit.editor.panel.library.maven.search;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import static org.apache.commons.codec.binary.Base64.encodeBase64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class NexusSearch implements Search {

    // nexus
    private static final String URL_PREFIX = "http://nexus.gluonhq.com/nexus/service/local/data_index?q=";
    private static final String URL_PREFIX_CLASS = "http://nexus.gluonhq.com/nexus/service/local/data_index?cn=";
    private static final String URL_SUFFIX = "";
    
    private final HttpClient client;
    private final String username;
    private final String password;
            
    public NexusSearch() {
        client = HttpClients.createDefault();
        // TODO: Retrieve user/password from Preferences, and change the nexus repository URL
        username = "";
        password = "";
    }
    
    @Override
    public List<String> getCoordinates(String query) {
        try {
            HttpGet request = new HttpGet(URL_PREFIX + query + URL_SUFFIX);
            if (!username.isEmpty() && !password.isEmpty()) {
                String authStringEnc = new String(encodeBase64((username + ":" + password).getBytes()));
                request.addHeader("Authorization", "Basic " + authStringEnc);
            }
            request.setHeader("Accept", "application/json");
            HttpResponse response = client.execute(request);
            try (JsonReader rdr = Json.createReader(response.getEntity().getContent())) {
                JsonObject obj = rdr.readObject();
                if (obj != null && !obj.isEmpty() && obj.containsKey("data")) {
                    JsonArray docResults = obj.getJsonArray("data");
                    return docResults.getValuesAs(JsonObject.class)
                            .stream()
                            .map(doc -> doc.getString("groupId", "") + ":" + doc.getString("artifactId", "")) // "version"
                            .distinct()
                            .collect(Collectors.toList());
                }
                    
            }
        } catch (IOException ex) {
            Logger.getLogger(NexusSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
