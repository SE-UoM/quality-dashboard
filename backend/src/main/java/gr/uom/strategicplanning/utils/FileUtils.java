package gr.uom.strategicplanning.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void deleteDirectoryContents(Path path) throws IOException {
        try {
            Files.walk(path)
                    .sorted((p1, p2) -> -p1.compareTo(p2))
                    .map(Path::toFile)
                    .forEach(file -> {
                        file.setWritable(true);
                        file.delete();
                    });
        } catch (IOException e) {
            throw new IOException("Failed to delete directory: " + path, e);
        }
    }

    public static void deleteDirectory(Path path) throws IOException {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new IOException("Failed to delete directory: " + path, e);
        }
    }
}
