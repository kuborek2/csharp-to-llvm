import csharp.CSharpParser;
import csharp.CSharpParserBaseVisitor;

import java.util.ArrayList;

public class CSharpCodeGenVisitor extends CSharpParserBaseVisitor<String> {
    private final StringBuilder stringBuilder;
    private final CSharpTypeInferenceVisitor typeVisitor = new CSharpTypeInferenceVisitor();

    CSharpCodeGenVisitor(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public String visitMethod_declaration(CSharpParser.Method_declarationContext ctx) {
        ArrayList<String> parameterListInLLVM = new ArrayList<>();
        String parameterListInLLVMString = "";
        var functionName = ctx.getChild(0).getText();

        var parameterElement = ctx.formal_parameter_list().fixed_parameters().fixed_parameter();
        String[] parameterList = ctx.formal_parameter_list().fixed_parameters().getText().split(",");
        int parameterCount = parameterList.length;
        for ( int i = 0; i < parameterCount; i++ ){
            var parameter = parameterElement.get(i);
            var parameterType = parameter.arg_declaration().type_().getText();
            var parameterName = parameter.arg_declaration().identifier(). getText();
            String parameterInLLVM = typeVisitor.litteralType(parameterType)+" "+parameterName;
            parameterListInLLVM.add(parameterInLLVM);
        }

        for (int i = 0; i < parameterListInLLVM.size(); i++){
            parameterListInLLVMString = parameterListInLLVMString + parameterListInLLVM.get(i);
            if ( i + 1 < parameterListInLLVM.size() ) parameterListInLLVMString = parameterListInLLVMString + ", ";
        }
        // -----------------------------------------------------
//        System.out.println("3 Child: "+ctx.getChild(3).getText());
//        System.out.println("4 Child: "+ctx.getChild(4).getText());
        var returnType = "void";
//        stringBuilder.append(String.format("define void @%s%s{\n", functionName, params));
        stringBuilder.append(String.format("define void @%s(%s){\n", functionName, parameterListInLLVMString));
        super.visitMethod_declaration(ctx);
        stringBuilder.append(String.format("ret %s\n", returnType));
        stringBuilder.append("}\n");
        return null;
    }



}
