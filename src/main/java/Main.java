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
        ParseTree tree = parser.compilation_unit();

        String beforeVisit = tree.getText();
        String[] splittedTree = beforeVisit.split(";");
        for (String x : splittedTree ){
            System.out.println(x);
        }
        System.out.println(" --------------------------------------------------------- ");

        var result = new StringBuilder();
        var visitor = new CSharpCodeGenVisitor(result);
        visitor.visit(tree);
        String stringAfterVisit = result.toString();


        splittedTree = stringAfterVisit.split(";");
        for (String x : splittedTree ){
            System.out.println(x);
        }
        System.out.println(" --------------------------------------------------------- ");
//        int x = 15;
//        System.out.println(x);
//        ParseTreeWalker walker = new ParseTreeWalker();
//        CSharpParserBaseListener listener = new CSharpParserBaseListener();
//        walker.walk(listener, tree);


//        assertThat(listener.getErrors().size(), is(1));
//        assertThat(listener.getErrors().get(0),
//                is("Method DoSomething is uppercased!"));

//        var pythonLexer = new PythonLexer(CharStreams.fromString(source));
//        var commonTokenStream = new CommonTokenStream(pythonLexer);
//        var pythonParser = new PythonParser(commonTokenStream);
//
//        var fileContext = pythonParser.file_input();
//        var result = new StringBuilder();
//        var visitor = new PythonCodeGeneratingVisitor(result);
//        visitor.visit(fileContext);


    }
}
