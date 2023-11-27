package backend;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageConvertor {
    public static Image convertToImage(String imageURL) throws MalformedURLException, IOException {
        try (InputStream stream = new URL(imageURL).openStream()) {
            Image image = new Image(stream);
            return image;
        }
    }

}
