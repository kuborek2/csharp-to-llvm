import csharp.CSharpParser;
import csharp.CSharpParserBaseVisitor;

import java.util.ArrayList;

public class CSharpCodeGenVisitor extends CSharpParserBaseVisitor<String> {
    private final StringBuilder stringBuilder;
    private final CSharpTypeInferenceVisitor typeVisitor = new CSharpTypeInferenceVisitor();
    private int ifCount = 0;
    private int loopCount = 0;
    private int tmpCount = 0;
    private ArrayList<String[]> varsList = new ArrayList<String[]>();

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
        stringBuilder.append(String.format("\ndefine void @%s(%s){\n", functionName, parameterListInLLVMString));
        super.visitMethod_declaration(ctx);
        stringBuilder.append(String.format("ret %s\n", returnType));
        stringBuilder.append("}\n\n");
        return null;
    }

    @Override
    public String visitDeclarationStatement(CSharpParser.DeclarationStatementContext ctx) {

        var type = ctx.local_variable_declaration().local_variable_type().type_().getText();
        var identifier = ctx.local_variable_declaration().local_variable_declarator().get(0).identifier().getText();
        var value = ctx.local_variable_declaration().local_variable_declarator().get(0).local_variable_initializer().expression().getText();

        if( type.contains("String") ){
            var llvmType = typeVisitor.inferType(value);
            stringBuilder.append(String.format("%%%s = alloca %s\n", identifier, llvmType));
            stringBuilder.append(String.format("store %s %s, %s* %%%s\n", llvmType, value, llvmType, identifier));
            String[] varsListElement = new String[3];
            varsListElement[0] = identifier.trim();
            varsListElement[1] = llvmType.trim();
            varsListElement[2] = value.trim();
            varsList.add(varsListElement);
        } else {
            var llvmType = typeVisitor.litteralType(type);
            stringBuilder.append(String.format("%%%s = alloca %s\n", identifier, llvmType));
            stringBuilder.append(String.format("store %s %s, %s* %%%s\n", llvmType, value, llvmType, identifier));
            String[] varsListElement = new String[3];
            varsListElement[0] = identifier.trim();
            varsListElement[1] = llvmType.trim();
            varsListElement[2] = value.trim();
            varsList.add(varsListElement);
        }
        return super.visitDeclarationStatement(ctx);
    }

    @Override
    public String visitTyped_member_declaration(CSharpParser.Typed_member_declarationContext ctx) {

        var type = ctx.type_().getText();
        var identifier = ctx.field_declaration().variable_declarators()
                .variable_declarator().get(0).identifier().getText();
        var value = ctx.field_declaration().variable_declarators()
                .variable_declarator().get(0).variable_initializer().expression().getText();

        if( type.contains("String") ){
            var llvmType = typeVisitor.inferType(value);
            stringBuilder.append(String.format("%%%s = alloca %s\n", identifier, llvmType));
            stringBuilder.append(String.format("store %s %s, %s* %%%s\n", llvmType, value, llvmType, identifier));
            String[] varsListElement = new String[3];
            varsListElement[0] = identifier.trim();
            varsListElement[1] = llvmType.trim();
            varsListElement[2] = value.trim();
            varsList.add(varsListElement);
        } else {
            var llvmType = typeVisitor.litteralType(type);
            stringBuilder.append(String.format("%%%s = alloca %s\n", identifier, llvmType));
            stringBuilder.append(String.format("store %s %s, %s* %%%s\n", llvmType, value, llvmType, identifier));
            String[] varsListElement = new String[3];
            varsListElement[0] = identifier.trim();
            varsListElement[1] = llvmType.trim();
            varsListElement[2] = value.trim();
            varsList.add(varsListElement);
        }

        return super.visitTyped_member_declaration(ctx);
    }

    @Override
    public String visitEmbedded_statement(CSharpParser.Embedded_statementContext ctx) {
        if( ctx.simple_embedded_statement() != null) {
            if (ctx.simple_embedded_statement().getChild(0).getText().contains("if")) {
                int thisTmpCount = tmpCount;
                int thisIfCount = ifCount;
                this.ifCount = ifCount + 1;
                this.tmpCount = tmpCount + 1;
                var start = ctx.simple_embedded_statement().getChild(2);
                var firstVariable = start.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0)
                        .getChild(0).getChild(0).getChild(0).getText();
                var operator = start.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0)
                        .getChild(0).getChild(0).getChild(1).getText();
                var secondVariable = start.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0)
                        .getChild(0).getChild(0).getChild(2).getText();
                String llvmFirstVariableType = "blad";
                String llvmOperator = typeVisitor.litteralOperator(operator);

                for (String[] stringTable : varsList) {
                    if(stringTable[0].equals(firstVariable)){
                        llvmFirstVariableType = stringTable[1];
                    }
                }

                stringBuilder.append(String.format("\n%%tmp%x = load %s, %s* %%%s\n",thisTmpCount,llvmFirstVariableType, llvmFirstVariableType, firstVariable));
                stringBuilder.append(String.format("%%cond%x = icmp %s %s %%tmp%x, %s\n",thisTmpCount, llvmOperator,llvmFirstVariableType, thisTmpCount, secondVariable));
                stringBuilder.append(String.format("bi il %%cond%x, label %%IfEqual%x, label %%IfUnequal%x\n\n",thisTmpCount, thisIfCount, thisIfCount));
                stringBuilder.append(String.format("IfEqual%x:\n",thisIfCount));

                super.visitEmbedded_statement(ctx);

                stringBuilder.append(String.format("IfAfter%x:\n",thisIfCount));
            }
            if (ctx.simple_embedded_statement().getChild(0).getText().contains("while")) {
                int thisTmpCount = tmpCount;
                int thisLoopCount = loopCount;
                this.loopCount = loopCount + 1;
                this.tmpCount = tmpCount + 1;

                var start = ctx.simple_embedded_statement().getChild(2);
                var firstVariable = start.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0)
                        .getChild(0).getChild(0).getChild(0).getChild(0).getText();
                var operator = start.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0)
                        .getChild(0).getChild(0).getChild(0).getChild(1).getText();
                var secondVariable = start.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0)
                        .getChild(0).getChild(0).getChild(0).getChild(2).getText();
                String llvmFirstVariableType = "blad";
                String llvmOperator = typeVisitor.litteralOperator(operator);


                for (String[] stringTable : varsList) {
                    if(stringTable[0].equals(firstVariable)){
                        llvmFirstVariableType = stringTable[1];
                    }
                }

                stringBuilder.append(String.format("br label %%loop%x\n\n",thisLoopCount));
                stringBuilder.append(String.format("loop%x:\n",thisLoopCount));
                stringBuilder.append(String.format("%%tmp%x = load %s, %s* %%%s\n",thisTmpCount,llvmFirstVariableType, llvmFirstVariableType, firstVariable));
                stringBuilder.append(String.format("%%cond%x = icmp %s %s %%tmp%x, %s\n",thisTmpCount, llvmOperator,llvmFirstVariableType, thisTmpCount, secondVariable));
                stringBuilder.append(String.format("bi il %%cond%x, label %%insideLoop%x, label %%afterLoop%x\n\n",thisTmpCount, thisLoopCount, thisLoopCount));
                stringBuilder.append(String.format("insideLoop%x:\n",thisLoopCount));

                stringBuilder.append(String.format("%%tmp2 = load i32, i32*%%c \n"));
                stringBuilder.append(String.format("%%tmp3 = add nsw i32 %%tmp2, 1 \n"));

                super.visitEmbedded_statement(ctx);

                stringBuilder.append(String.format("afterLoop%x:\n",thisLoopCount));
            }
        }
        return null;
    }

}

