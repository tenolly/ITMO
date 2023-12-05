import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Test {
    public interface Human {
        public String getName();
    }

    public static class Pirate implements Human {
        private String name;

        public Pirate(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    public static class PirateHandler implements InvocationHandler {
        private Human human;

        public PirateHandler(Human human) {
            this.human = human;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            var result = method.invoke(this.human, args);
            if (result == "Джек Воробей") {
                return "Капитан Джек Воробей"; 
            }
            return result;
        }
    }

    public static void main(String[] args) {
        var pirate = new Pirate("Джек Воробей");

        ClassLoader classLoader = pirate.getClass().getClassLoader();
        Class[] interfaces = pirate.getClass().getInterfaces();

        var proxy = (Human) Proxy.newProxyInstance(classLoader, interfaces, new PirateHandler(pirate));
        System.err.println("Как тебя зовут?");
        System.out.println(proxy.getName());
    }
}