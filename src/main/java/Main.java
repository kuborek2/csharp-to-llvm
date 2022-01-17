import csharp.CSharpLexer;
import csharp.CSharpParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
    static public void main(String args[]){
        String javaClassContent = "public class MyClass\n" +
                "{\n" +
                "    public string  myField = string.Empty;\n" +
                "\n" +
                "    public MyClass()\n" +
                "    {\n" +
                "    }\n" +
                "\n" +
                "    public void MyMethod(int parameter1, string parameter2)\n" +
                "    {\n" +
                "        Console.WriteLine(\"First Parameter {0}, second parameter {1}\", \n" +
                "                                                    parameter1, parameter2);\n" +
                "    }\n" +
                "\n" +
                "    public int MyAutoImplementedProperty { get; set; }\n" +
                "\n" +
                "    private int myPropertyVar;\n" +
                "    \n" +
                "    public int MyProperty\n" +
                "    {\n" +
                "        get { return myPropertyVar; }\n" +
                "        set { myPropertyVar = value; }\n" +
                "    } \n" +
                "}";


        CSharpLexer lexer = new CSharpLexer(CharStreams.fromString(javaClassContent));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CSharpParser parser = new CSharpParser(tokens);
        ParseTree tree = parser.compilationUnit();


    }
}
