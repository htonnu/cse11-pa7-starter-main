import java.nio.file.*;
import javax.management.QueryEval;
import java.io.IOException;

interface Query {
    public boolean matches(String s);
}

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

class StringSearch{
    static Query readQuery(String arg) {
        String[] querySplit = arg.split("=");
        if(querySplit[0].contains("not")) {
            if(querySplit[0].contains("length")) {
                String querySplit2 = querySplit[1].substring(0, querySplit[1].length()-1);
                LengthQuery query2 = new LengthQuery(querySplit2);
                NotQuery query = new NotQuery(query2);
                return query;
            }
            else if(querySplit[0].contains("greater")) {
                String querySplit2 = querySplit[1].substring(0, querySplit[1].length()-1);
                GreaterQuery query2 = new GreaterQuery(querySplit2);
                NotQuery query = new NotQuery(query2);
                return query;
            }
            else if(querySplit[0].contains("less")) {
                String querySplit2 = querySplit[1].substring(0, querySplit[1].length()-1);
                LessQuery query2 = new LessQuery(querySplit2);
                NotQuery query = new NotQuery(query2);
                return query;
            }
            else if(querySplit[0].contains("contains")) {
                String querySplit2 = querySplit[1].substring(1, querySplit[1].length()-2);
                ContainsQuery query2 = new ContainsQuery(querySplit2);
                NotQuery query = new NotQuery(query2);
                return query;
            }
            else if(querySplit[0].contains("starts")) {
                String querySplit2 = querySplit[1].substring(1, querySplit[1].length()-2);
                StartsQuery query2 = new StartsQuery(querySplit2);
                NotQuery query = new NotQuery(query2);
                return query;
            }
            else if(querySplit[0].contains("ends")) {
                String querySplit2 = querySplit[1].substring(1, querySplit[1].length()-2);
                EndsQuery query2 = new EndsQuery(querySplit2);
                NotQuery query = new NotQuery(query2);
                return query;
            }
            else {
                return null;
            }
        }
        else if (querySplit[0].contains("length")) {
            String querySplit2 = querySplit[1].substring(0, querySplit[1].length());
            LengthQuery query = new LengthQuery(querySplit2);
            return query;
        }
        else if(querySplit[0].contains("greater")) {
            String querySplit2 = querySplit[1].substring(0, querySplit[1].length());
            GreaterQuery query = new GreaterQuery(querySplit2);
            return query;
        }
        else if(querySplit[0].contains("less")) {
            String querySplit2 = querySplit[1].substring(0, querySplit[1].length());
            LessQuery query = new LessQuery(querySplit2);
            return query;
        }
        else if(querySplit[0].contains("contains")) {
            String querySplit2 = querySplit[1].substring(1, querySplit[1].length()-1);
            ContainsQuery query = new ContainsQuery(querySplit2);
            return query;
        }
        else if(querySplit[0].contains("starts")) {
            String querySplit2 = querySplit[1].substring(1, querySplit[1].length()-1);
            StartsQuery query = new StartsQuery(querySplit2);
            return query;
        }
        else if(querySplit[0].contains("ends")) {
            String querySplit2 = querySplit[1].substring(1, querySplit[1].length()-1);
            EndsQuery query = new EndsQuery(querySplit2);
            return query;
        }
        return null;
    }
    public static void main(String[] arr) {        
        if (arr.length == 1) {
            for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                System.out.println(FileHelper.getLines(arr[0])[i]);
            }
        }
        else if (arr.length == 2) {
            Query query = readQuery(arr[1]);
            if (arr[1].contains("not")) {
                if (arr[1].contains("length")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                        String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                        if (query.matches(lengthString)) {
                            System.out.println(FileHelper.getLines(arr[0])[i]);
                        }
                    }
                }
                else if (arr[1].contains("greater")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                        String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                        if (query.matches(lengthString)) {
                            System.out.println(FileHelper.getLines(arr[0])[i]);
                        }
                    }
                }
                else if (arr[1].contains("less")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                        String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                        if (query.matches(lengthString)) {
                            System.out.println(FileHelper.getLines(arr[0])[i]);
                        }
                    }
                }
                else if (arr[1].contains("contains")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                        if (query.matches(FileHelper.getLines(arr[0])[i])) { 
                            System.out.println(FileHelper.getLines(arr[0])[i]);
                        }
                    }
                }
                else if (arr[1].contains("starts")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                        String[] poemLineSplit = FileHelper.getLines(arr[0])[i].split(" ");
                        if (query.matches(poemLineSplit[0])) { 
                            System.out.println(FileHelper.getLines(arr[0])[i]);
                        }
                    }
                }
                else if (arr[1].contains("ends")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                        String[] poemLineSplit = FileHelper.getLines(arr[0])[i].split(" ");
                        if (query.matches(poemLineSplit[poemLineSplit.length-1])) { 
                            System.out.println(FileHelper.getLines(arr[0])[i]);
                        }
                    }
                }
            }
            else if (arr[1].contains("length")) {
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                    String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                    if (query.matches(lengthString)) {
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
            else if (arr[1].contains("greater")) {
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                    String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                    if (query.matches(lengthString)) {
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
            else if (arr[1].contains("less")) {
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                    String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                    if (query.matches(lengthString)) {
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
            else if (arr[1].contains("contains")) {
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                    if (query.matches(FileHelper.getLines(arr[0])[i])) { 
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
            else if (arr[1].contains("starts")) {
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                    String[] poemLineSplit = FileHelper.getLines(arr[0])[i].split(" ");
                    if (query.matches(poemLineSplit[0])) { 
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
            else if (arr[1].contains("ends")) {
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                    String[] poemLineSplit = FileHelper.getLines(arr[0])[i].split(" ");
                    if (query.matches(poemLineSplit[poemLineSplit.length-1])) { 
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
        }
    }
}

class ContainsQuery implements Query {
    String field;
    ContainsQuery(String field) {
        this.field = field;
    }
    public boolean matches(String s) { 
        if(s.contains(this.field)) {
            return true;
        }
        else {
            return false;
        }
    }
}

class LengthQuery implements Query {
    String field;
    LengthQuery(String field) {
        this.field = field;
    }
    public boolean matches(String s)    { 
        if(Integer.parseInt(s) == Integer.parseInt(this.field)) {
            return true;
        }
        else {
            return false;
        }
    }
}

class GreaterQuery implements Query {
    String field;
    GreaterQuery(String field) {
        this.field = field;
    }
    public boolean matches(String s)    { 
        if(Integer.parseInt(s) > Integer.parseInt(this.field)) {
            return true;
        }
        else {
            return false;
        }
    }
}

class LessQuery implements Query {
    String field;
    LessQuery(String field) {
        this.field = field;
    }
    public boolean matches(String s)    { 
        if(Integer.parseInt(s) < Integer.parseInt(this.field)) {
            return true;
        }
        else {
            return false;
        }
    }
}

class StartsQuery implements Query {
    String field;
    StartsQuery(String field) {
        this.field = field;
    }
    public boolean matches(String s) { 
        if(s.contains(this.field)) {
            return true;
        }
        else {
            return false;
        }
    }
}

class EndsQuery implements Query {
    String field;
    EndsQuery(String field) {
        this.field = field;
    }
    public boolean matches(String s) { 
        if(s.endsWith(this.field)) {
            return true;
        }
        else {
            return false;
        }
    }
}

class NotQuery implements Query {
    Query field;
    NotQuery(Query field) {
        this.field = field;
    }
    public boolean matches(String s) {
        if (field.matches(s) == true) {
            return false;
        } 
        else {
            return true;
        }
    }
}