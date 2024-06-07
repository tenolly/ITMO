package utils.caster;

import java.io.Serializable;
import com.google.common.base.Function;

import utils.commands.exceptions.TerminateException;

public class CasterUtil implements Serializable {
    public static final Function<String, String> castToString = (Function<String, String> & Serializable) arg -> {
        if (arg == null) throw new CasterException("can't be empty");
        if (arg.isEmpty()) throw new CasterException("can't be empty");
        if (arg.equals("!exit")) throw new TerminateException();
        return arg;
    };
}
