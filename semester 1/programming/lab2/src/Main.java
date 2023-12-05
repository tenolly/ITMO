import pokemons.*;
import ru.ifmo.se.pokemon.Battle;

public class Main {

    public static void main(String[] args) {
        Battle b = new Battle();
    b.addAlly(new Bastiodon("Поляков В.И., доцент, кандидат технических наук ", 50));
    b.addAlly(new Burmy("Правдин К.В., кандидат технических наук", 11));
    b.addAlly(new Ledian("Попов И.Ю., профессор, доктор физико-математических наук", 45));
    b.addFoe(new Meowth("Балакшин П.В., кандидат технических наук ", 16));
    b.addFoe(new Pichu("Клименков С.В.", 28));
    b.addFoe(new Wormadam("Письмак А.Е.", 7));
    b.go();
  }
}
