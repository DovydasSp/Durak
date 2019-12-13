/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chain;

/**
 *
 * @author Admin
 */
public class ChainLogger {

    private AbstractLogger loggerChain;

    public void logMessage(int level, String message) {
        loggerChain.logMessage(level, message);
    }

    public ChainLogger() {
        AbstractLogger patternLogger = new PatternLogger(AbstractLogger.PATTERN);
        AbstractLogger errorLogger = new ErrorLogger(AbstractLogger.ERROR);
        AbstractLogger fileLogger = new FileLogger(AbstractLogger.DEBUG);
        AbstractLogger consoleLogger = new ConsoleLogger(AbstractLogger.INFO);

        errorLogger.setNextLogger(fileLogger);
        fileLogger.setNextLogger(patternLogger);
        patternLogger.setNextLogger(consoleLogger);
        this.loggerChain = errorLogger;
    }
}
