import csharp.CSharpParser;
import csharp.CSharpParserBaseVisitor;

public class CSharpTypeInferenceVisitor extends CSharpParserBaseVisitor {

    public boolean checkType(String text) {
        return isString(text) || isInteger(text) || isDouble(text) || isBoolean(text) ;
    }

    public String inferType(String text) {
        if (isString(text)) {
            return String.format("[%s x i8]", text.length() - 2);
        }
        if (isInteger(text)) {
            return "i32";
        }
        if (isDouble(text)) {
            return "double";
        }
        if (isBoolean(text)) {
            return "i1";
        }
        return "";
    }

    public String litteralType(String text) {
        switch(text){
            case "int":
                return "i32";

            case "string":
                return "i8";

            case "double":
                return "dobule";

            case "float":
                return "dobule";

            case "Boolean":
                return "i1";

            default:
                return "";
        }
    }

    public String litteralOperator(String text){
        if(text.contains("=="))
            return "eq";
        else if( text.contains("<") )
            return "nsw";
        else
            return "nieznazny operator";
    }

    private boolean isString(String text) {
        if( text.getClass().equals(String.class)  ){
            return true;
        }
        return false;
    }

    private boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isBoolean(String text) {
        return "True".equals(text) || "False".equals(text);
    }


}
