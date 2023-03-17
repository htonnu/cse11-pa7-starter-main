import java.nio.file.*;
import javax.management.QueryEval;
import java.io.IOException;

interface Query {
    public boolean matches(String s);
}

interface Transform {
    public String transform(String s);
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

class StringSearch {
    static boolean matchesAll(Query[] qs, String s) {
        boolean result = true;
        for(int i = 0; i < qs.length; i++) {
            if (qs[i].toString().contains("Length") ||
                qs[i].toString().contains("Greater") ||
                qs[i].toString().contains("Less")) {
                if (qs[i].matches(Integer.toString(s.length()))) {
                    result = true;
                }
                else {
                    result = false;
                    break;
                }
            }
            else {
                if (qs[i].matches(s)) {
                    result = true;
                }
                else {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
    static String applyAll(Transform[] ts, String s) {
        String result = s;
        for(int i = 0; i < ts.length; i++) {
            result = ts[i].transform(result); 
        }
        return result;
    }
    static Transform readTransform(String t) {
        String[] querySplit = t.split("=");
        if(querySplit[0].contains("upper")) {
            upperTransform transform = new upperTransform();
            return transform;
        }
        else if (querySplit[0].contains("lower")) {
            lowerTransform transform = new lowerTransform();
            return transform;
        }
        else if (querySplit[0].contains("first")) {
            int querySplit2 = Integer.parseInt(querySplit[1].substring(0, querySplit[1].length()));
            firstTransform transform = new firstTransform(querySplit2);
            return transform;
        }
        else if (querySplit[0].contains("last")) {
            int querySplit2 = Integer.parseInt(querySplit[1].substring(0, querySplit[1].length()));
            lastTransform transform = new lastTransform(querySplit2);
            return transform;
        }
        else if (querySplit[0].contains("replace")) {
            String[] querySplitAgain = querySplit[1].split(";");
            String querySplit2 = querySplitAgain[0].substring(1, querySplitAgain[0].length()-1);
            String querySplit3 = querySplitAgain[1].substring(1, querySplitAgain[1].length()-1);
            replaceTransform transform = new replaceTransform(querySplit2, querySplit3);
            return transform;
        }
        else {
            return null;
        }
    }
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
            if (arr[1].contains("&")) {
                String[] queriesStringList = arr[1].split("&");
                Query[] queriesList = new Query[queriesStringList.length];
                for(int i = 0; i < queriesList.length; i++) {
                    Query query = readQuery(queriesStringList[i]); 
                    queriesList[i] = query;
                }
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                    if(matchesAll(queriesList, FileHelper.getLines(arr[0])[i])) {
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
            else if (arr[1].contains("not")) {
                Query query = readQuery(arr[1]);
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
                Query query = readQuery(arr[1]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                    String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                    if (query.matches(lengthString)) {
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
            else if (arr[1].contains("greater")) {
                Query query = readQuery(arr[1]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                    String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                    if (query.matches(lengthString)) {
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
            else if (arr[1].contains("less")) {
                Query query = readQuery(arr[1]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                    String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                    if (query.matches(lengthString)) {
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
            else if (arr[1].contains("contains")) {
                Query query = readQuery(arr[1]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                    if (query.matches(FileHelper.getLines(arr[0])[i])) { 
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
            else if (arr[1].contains("starts")) {
                Query query = readQuery(arr[1]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                    String[] poemLineSplit = FileHelper.getLines(arr[0])[i].split(" ");
                    if (query.matches(poemLineSplit[0])) { 
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
            else if (arr[1].contains("ends")) {
                Query query = readQuery(arr[1]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                    String[] poemLineSplit = FileHelper.getLines(arr[0])[i].split(" ");
                    if (query.matches(poemLineSplit[poemLineSplit.length-1])) { 
                        System.out.println(FileHelper.getLines(arr[0])[i]);
                    }
                }
            }
        }
        else if (arr.length == 3) {
            if (arr[1].contains("&")) {
                String[] queriesStringList = arr[1].split("&");
                Query[] queriesList = new Query[queriesStringList.length];
                for(int i = 0; i < queriesList.length; i++) {
                    Query query = readQuery(queriesStringList[i]);
                    queriesList[i] = query;
                }
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                    if(matchesAll(queriesList, FileHelper.getLines(arr[0])[i])) {
                        Transform transform = readTransform(arr[2]);                             
                        if (arr[2].contains("&")) { 
                            String[] transformStringList = arr[2].split("&"); 
                            Transform[] transformList = new Transform[transformStringList.length]; 
                            for(int j = 0; j < transformList.length; j++) {      
                                transform = readTransform(transformStringList[j]); 
                                transformList[j] = transform; 
                            }
                            System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                        }
                        else {
                            System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                        }
                    }
                }
            }
            else if (arr[1].contains("not")) {
                Query query = readQuery(arr[1]);
                Transform transform = readTransform(arr[2]);
                if (arr[1].contains("length")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                        String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                        if (query.matches(lengthString)) {
                            if (arr[2].contains("&")) { 
                                String[] transformStringList = arr[2].split("&"); 
                                Transform[] transformList = new Transform[transformStringList.length]; 
                                for(int j = 0; j < transformList.length; j++) {      
                                    transform = readTransform(transformStringList[j]); 
                                    transformList[j] = transform; 
                                }
                                System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                            }
                            else {
                                System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                            }
                        }
                    }
                }
                else if (arr[1].contains("greater")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                        String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                        if (query.matches(lengthString)) {
                            if (arr[2].contains("&")) { 
                                String[] transformStringList = arr[2].split("&"); 
                                Transform[] transformList = new Transform[transformStringList.length]; 
                                for(int j = 0; j < transformList.length; j++) {      
                                    transform = readTransform(transformStringList[j]); 
                                    transformList[j] = transform; 
                                }
                                System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                            }
                            else {
                                System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                            }                     
                        }
                    }
                }
                else if (arr[1].contains("less")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                        String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                        if (query.matches(lengthString)) {
                            if (arr[2].contains("&")) { 
                                String[] transformStringList = arr[2].split("&"); 
                                Transform[] transformList = new Transform[transformStringList.length]; 
                                for(int j = 0; j < transformList.length; j++) {      
                                    transform = readTransform(transformStringList[j]); 
                                    transformList[j] = transform; 
                                }
                                System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                            }
                            else {
                                System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                            }                     
                        }
                    }
                }
                else if (arr[1].contains("contains")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {        
                        if (query.matches(FileHelper.getLines(arr[0])[i])) {          
                            if (arr[2].contains("&")) { 
                                String[] transformStringList = arr[2].split("&"); 
                                Transform[] transformList = new Transform[transformStringList.length]; 
                                for(int j = 0; j < transformList.length; j++) {      
                                    transform = readTransform(transformStringList[j]); 
                                    transformList[j] = transform; 
                                }
                                System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                            }
                            else {
                                System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                            }
                        }
                    }
                }
                else if (arr[1].contains("starts")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                        String[] poemLineSplit = FileHelper.getLines(arr[0])[i].split(" ");
                        if (query.matches(poemLineSplit[0])) { 
                            if (arr[2].contains("&")) { 
                                String[] transformStringList = arr[2].split("&"); 
                                Transform[] transformList = new Transform[transformStringList.length]; 
                                for(int j = 0; j < transformList.length; j++) {      
                                    transform = readTransform(transformStringList[j]); 
                                    transformList[j] = transform; 
                                }
                                System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                            }
                            else {
                                System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                            }
                        }
                    }
                }
                else if (arr[1].contains("ends")) {
                    for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                        String[] poemLineSplit = FileHelper.getLines(arr[0])[i].split(" ");
                        if (query.matches(poemLineSplit[poemLineSplit.length-1])) { 
                            if (arr[2].contains("&")) { 
                                String[] transformStringList = arr[2].split("&"); 
                                Transform[] transformList = new Transform[transformStringList.length]; 
                                for(int j = 0; j < transformList.length; j++) {      
                                    transform = readTransform(transformStringList[j]); 
                                    transformList[j] = transform; 
                                }
                                System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                            }
                            else {
                                System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                            }
                        }
                    }
                }
            }
            else if (arr[1].contains("length")) {
                Query query = readQuery(arr[1]);
                Transform transform = readTransform(arr[2]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                    String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                    if (query.matches(lengthString)) {
                        if (arr[2].contains("&")) { 
                            String[] transformStringList = arr[2].split("&"); 
                            Transform[] transformList = new Transform[transformStringList.length]; 
                            for(int j = 0; j < transformList.length; j++) {      
                                transform = readTransform(transformStringList[j]); 
                                transformList[j] = transform; 
                            }
                            System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                        }
                        else {
                            System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                        }
                    }
                }
            }
            else if (arr[1].contains("greater")) {
                Query query = readQuery(arr[1]);
                Transform transform = readTransform(arr[2]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                    String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                    if (query.matches(lengthString)) {
                        if (arr[2].contains("&")) { 
                            String[] transformStringList = arr[2].split("&"); 
                            Transform[] transformList = new Transform[transformStringList.length]; 
                            for(int j = 0; j < transformList.length; j++) {      
                                transform = readTransform(transformStringList[j]); 
                                transformList[j] = transform; 
                            }
                            System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                        }
                        else {
                            System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                        }
                    }
                }
            }
            else if (arr[1].contains("less")) {
                Query query = readQuery(arr[1]);
                Transform transform = readTransform(arr[2]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) {
                    String lengthString = Integer.toString(FileHelper.getLines(arr[0])[i].length());
                    if (query.matches(lengthString)) {
                        if (arr[2].contains("&")) { 
                            String[] transformStringList = arr[2].split("&"); 
                            Transform[] transformList = new Transform[transformStringList.length]; 
                            for(int j = 0; j < transformList.length; j++) {      
                                transform = readTransform(transformStringList[j]); 
                                transformList[j] = transform; 
                            }
                            System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                        }
                        else {
                            System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                        }
                    }
                }
            }
            else if (arr[1].contains("contains")) {
                Query query = readQuery(arr[1]);
                Transform transform = readTransform(arr[2]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                    if (query.matches(FileHelper.getLines(arr[0])[i])) { 
                        if (arr[2].contains("&")) { 
                            String[] transformStringList = arr[2].split("&"); 
                            Transform[] transformList = new Transform[transformStringList.length]; 
                            for(int j = 0; j < transformList.length; j++) {      
                                transform = readTransform(transformStringList[j]); 
                                transformList[j] = transform; 
                            }
                            System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                        }
                        else {
                            System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                        }
                    }
                }
            }
            else if (arr[1].contains("starts")) {
                Query query = readQuery(arr[1]);
                Transform transform = readTransform(arr[2]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                    String[] poemLineSplit = FileHelper.getLines(arr[0])[i].split(" ");
                    if (query.matches(poemLineSplit[0])) { 
                        if (arr[2].contains("&")) { 
                            String[] transformStringList = arr[2].split("&"); 
                            Transform[] transformList = new Transform[transformStringList.length]; 
                            for(int j = 0; j < transformList.length; j++) {      
                                transform = readTransform(transformStringList[j]); 
                                transformList[j] = transform; 
                            }
                            System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                        }
                        else {
                            System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                        }
                    }
                }
            }
            else if (arr[1].contains("ends")) {
                Query query = readQuery(arr[1]);
                Transform transform = readTransform(arr[2]);
                for(int i = 0; i < FileHelper.getLines(arr[0]).length; i++) { 
                    String[] poemLineSplit = FileHelper.getLines(arr[0])[i].split(" ");
                    if (query.matches(poemLineSplit[poemLineSplit.length-1])) { 
                        if (arr[2].contains("&")) { 
                            String[] transformStringList = arr[2].split("&"); 
                            Transform[] transformList = new Transform[transformStringList.length]; 
                            for(int j = 0; j < transformList.length; j++) {      
                                transform = readTransform(transformStringList[j]); 
                                transformList[j] = transform; 
                            }
                            System.out.println(applyAll(transformList, FileHelper.getLines(arr[0])[i]));
                        }
                        else {
                            System.out.println(transform.transform(FileHelper.getLines(arr[0])[i]));
                        }
                    }
                }
            }
        }
    }
}

class upperTransform implements Transform {
    public String transform(String s) {
        return s.toUpperCase();
    }
}

class lowerTransform implements Transform {
    public String transform(String s) {
        return s.toLowerCase();
    }
}

class firstTransform implements Transform {
    int field;
    firstTransform(int field) {
        this.field = field;
    }
    public String transform(String s) {
        if (s.length() < field) {
            return s;
        }
        else {
            return s.substring(0, field);
        }
    }
}

class lastTransform implements Transform {
    int field;
    lastTransform(int field) {
        this.field = field;
    }
    public String transform(String s) {
        if (s.length() < field) {
            return s;
        }
        else {
            return s.substring(s.length()-field, s.length());
        }
    }
}

class replaceTransform implements Transform {
    String field1;
    String field2;
    replaceTransform(String field1, String field2) {    
        this.field1 = field1;
        this.field2 = field2;
    }
    public String transform(String s) {
        return s.replace(this.field1, this.field2);
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
        if(s.contains(this.field)) {
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