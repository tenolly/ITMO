package lab3;

import lab3.things.Car;
import lab3.things.Phone;
import lab3.things.Telegram;
import lab3.things.problems.PhoneIsBrokenException;
import lab3.buisness.stuff.Job;
import lab3.buisness.stuff.Worker;
import lab3.creature.Human;
import lab3.locations.NewspaperEditoring;
import lab3.locations.base.Building;

public class Main {
    public static void main(String[] args) {
        var home = new Building(" ");

        var mainChar = new Human("", home);
        var firstMinorChar = new Human("");
        var secondMinorChar = new Human("");
        var thirdMinorChar = new Worker("", Job.Owner, 0);

        var telegram = new Telegram("", "", home);
        var phone = new Phone("", mainChar);

        mainChar.read(telegram);
        try {
            mainChar.call(phone, firstMinorChar, home);
        } catch (PhoneIsBrokenException e) {
            e.printStackTrace();
        }
        try {
            mainChar.call(phone, secondMinorChar, home);
        } catch (PhoneIsBrokenException e) {
            e.printStackTrace();
        }
        home.remove(firstMinorChar);
        home.remove(secondMinorChar);

        var car = new Car("Машина Крабса");
        mainChar.move(car);
        car.move(home);

        var newspaperEditoring = new NewspaperEditoring("Давилонские юморески", thirdMinorChar);
        car.move(newspaperEditoring);

        System.out.println(newspaperEditoring.getProfit());
        thirdMinorChar.mind("Я не гонюсь за большими барышами");
        thirdMinorChar.mind("Газета нужна для рекламы товаров");
    }
}
