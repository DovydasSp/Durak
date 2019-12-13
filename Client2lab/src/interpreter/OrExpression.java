package interpreter;

import chain.AbstractLogger;
import chain.ChainLogger;

public class OrExpression implements Expression{
    private Expression expr1 = null;
    private Expression expr2 = null;
    public ChainLogger loggerChain = new ChainLogger();
    
    public OrExpression(Expression expr1, Expression expr2){
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public boolean interpret(String context) {
        loggerChain.logMessage(AbstractLogger.PATTERN, "Interpreter: intepret of OrExpression was called.");
        return expr1.interpret(context) || expr2.interpret(context);
    }
}
