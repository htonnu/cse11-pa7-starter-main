import java.nio.file.*;
import java.io.IOException;
class FileHelper {
    static String[] getLines(String path) {
        try {
            return Files.readAllLines(Paths.get(path)).toArray(String[]::new);
        }
        catch(IOException e) {
            System.err.println("Error reading file " + path + ": " + e);
            return new String[]{"Error reading file " + path + ": " + e};
        }
    }
}
class StringSearch {
    public static void main(String[] arr) {        
        for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
            System.out.println(FileHelper.getLines(arr[0])[i]);
        }
    }
}
