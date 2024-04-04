package util;

import java.net.URISyntaxException;
import java.net.URL;

public enum FileDistributer {
    input("input.xml"),
    output("output.xml"),

    // Test files
    test_input("test_input.xml"),
    test_output("test_output.xml"),

    // Views
    main("view/main.fxml"),
    start_screen("view/start.fxml");

    

    private String filePath;

    FileDistributer(String fileName) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource(fileName);
            if (resource == null) {
                throw new IllegalArgumentException("file not found! " + fileName);
            } else {
                this.filePath = resource.toURI().getPath();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public String getFilePath() {
        return filePath.toString();
    }
}
