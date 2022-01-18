import csharp.CSharpParser;
import csharp.CSharpParserBaseVisitor;

public class CSharpLeafsVisitor extends CSharpParserBaseVisitor<String> {

    @Override
    public String visitExpression(CSharpParser.ExpressionContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitAssignment_operator(CSharpParser.Assignment_operatorContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitConditional_expression(CSharpParser.Conditional_expressionContext ctx) {
        var comp = ctx.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0);
        var left = comp.getChild(0).accept(this);
        var operator = comp.getChild(1).accept(this);
        var right = comp.getChild(comp.getChildCount() - 1).accept(this);

        return left + "|" + operator + "|" + right;
    }
}
