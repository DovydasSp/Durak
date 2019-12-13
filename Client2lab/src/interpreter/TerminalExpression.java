package interpreter;

import chain.AbstractLogger;
import chain.ChainLogger;

public class TerminalExpression implements Expression{
    private String data;
    public ChainLogger loggerChain = new ChainLogger();
    
    public TerminalExpression(String data){
        this.data = data;
    }
    
    @Override
    public boolean interpret(String context) {
        loggerChain.logMessage(AbstractLogger.PATTERN, "Interpreter: intepret of TerminalExpression was called.");
        if(context.contains(data)){
            return true;
        }
        return false;
    }
}
