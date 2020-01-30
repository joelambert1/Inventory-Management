/* Joe Lambert -- exceptions for the package
 */
package inventorysystem;

class MinMaxException extends Exception{
    private String message;
    
    MinMaxException(String passedMsg) {
        message = passedMsg;
    }
    public String sendMsg () {
        return ("min-max exception:\n" + message);
    }
}

class InValidProduct extends Exception {
    private String message;
    
    InValidProduct(String passedMsg) {
        message = passedMsg;
    }
    public String sendMsg () {
        return ("Invalid product exception:\n" + message);
    }
}
