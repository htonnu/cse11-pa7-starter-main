class ContainsQuery {
    String field;
    ContainsQuery(String field){
        this.field = field;
    }
    boolean matches(String s) { 
        if(this.field.contains(s)) {
            return true;
        }
        else {
            return false;
        }
    }
    ContainsQuery query = new ContainsQuery("hello");
    boolean result1 = query.matches("hello world"); // true
    boolean result2 = query.matches("Hello world"); 
}