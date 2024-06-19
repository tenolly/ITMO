package shared.commands.exceptions;

import com.google.common.util.concurrent.UncheckedExecutionException;

/**
* Exception using when user force to terminate command
*/
public class TerminateException extends UncheckedExecutionException {}
