import csharp.CSharpLexer;
import csharp.CSharpParser;
import csharp.CSharpParserBaseListener;
import csharp.CSharpParserListener;
import csharp.Java.CSharpParserBase;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {
    static public void main(String args[]){
        String javaClassContent = "public class MyClass\n" +
                "{\n" +
                "    public String myField = \"exampleAttribute\";\n" +
                "\n" +
                "    public void MyMethod(int parameter1, string parameter2)\n" +
                "    {\n" +
                "\n" +
                "        int exampleInt = 5;\n" +
                "        String exampleString = \"exampleText\";\n" +
                "        float exampleFloat = 5.0;\n" +
                "        int c = 0;\n" +
                "\n" +
                "        if( exampleInt == 5 ){\n" +
                "            while(exampleFloat < 10){\n" +
                "                c += 1;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "}";


        CSharpLexer lexer = new CSharpLexer(CharStreams.fromString(javaClassContent));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CSharpParser parser = new CSharpParser(tokens);
        ParseTree tree = parser.compilation_unit();

//        String beforeVisit = tree.getText();
//        String[] splittedTree = beforeVisit.split(";");
//        for (String x : splittedTree ){
//            System.out.println(x);
//        }
        System.out.println(" --------------------------------------------------------- ");

        var result = new StringBuilder();
        var visitor = new CSharpCodeGenVisitor(result);
        visitor.visit(tree);
        String stringAfterVisit = result.toString();


        String[] splittedTree = stringAfterVisit.split(";");
        for (String x : splittedTree ){
            System.out.println(x);
        }
        System.out.println(" --------------------------------------------------------- ");


    }
}
