package backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
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

    public static String hexToBase64(String hexString) {
        // Convert hexadecimal to bytes
        byte[] byteData = hexStringToByteArray(hexString);

        // Encode bytes in base64
        byte[] base64Data = Base64.getEncoder().encode(byteData);

        // Convert bytes to string
        String base64String = new String(base64Data);

        return base64String;
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
