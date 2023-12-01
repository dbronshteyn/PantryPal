package backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HexFormat;

public class HexUtils {
    public static String fileToHex(File file) {
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            return HexFormat.of().formatHex(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void hexToFile(String hex, File file) throws IOException {
        Files.write(Paths.get(file.getAbsolutePath()), HexFormat.of().parseHex(hex));
    }
}
