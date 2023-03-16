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
        if (arr.length == 1) {
            for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                System.out.println(FileHelper.getLines(arr[0])[i]);
            }
        }
        else if (arr.length == 2) {
            String[] querySplit = arr[1].split("=");
            String querySplit2 = querySplit[1].substring(1, querySplit[1].length()-1);
            ContainsQuery query = new ContainsQuery(querySplit2);
            for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                if (query.matches(FileHelper.getLines(arr[0])[i])) { 
                    System.out.println(FileHelper.getLines(arr[0])[i]);
                }
            }
        }
    }
}
class ContainsQuery {
    String field;
    ContainsQuery(String field){
        this.field = field;
    }
    boolean matches(String s) { 
        if(s.contains(this.field)) {
            return true;
        }
        else {
            return false;
        }
    }
}
