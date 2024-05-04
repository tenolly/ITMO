package lab3.locations;

import lab3.buisness.Company;
import lab3.buisness.problems.NotEnoughMoneyException;
import lab3.buisness.problems.NotEnoughPapersException;
import lab3.buisness.stuff.Worker;

public class NewspaperEditoring extends Company {
    class FinanceMenagment {
        private int spent = 0;
        private int earned = 0;

        public FinanceMenagment() {
            spent = 0;
            earned = 0;
        }

        public FinanceMenagment(int spent, int earned) {
            this.spent = spent;
            this.earned = earned;
        }

        public void spendMoney(int money) {
            spent += money;
            owner.changeBudget(-1 * money);
        }

        public void earnMoney(int money) {
            earned += money;
            owner.changeBudget(money);
        }

        public String getProfit() {
            if (spent > earned) {
                return "Потратили больше, чем заработали";
            } else if (spent < earned) {
                return "Заработали больше, чем потратили";
            } else {
                return "Вышли в ноль";
            }
        }
    }

    private int countPapers = 0;
    private FinanceMenagment finance = new FinanceMenagment(100, 0);

    public NewspaperEditoring(String name, Worker owner) {
        super(name, owner);
    }

    public void makePapers(int count, int cost) throws NotEnoughMoneyException {
        if (owner.getBudget() < count * cost) {
            throw new NotEnoughMoneyException("Не хватает денег");
        } else {
            countPapers += count;
            finance.spendMoney(count * cost);
        }
    }

    public void sellPapers(int count, int cost) throws NotEnoughPapersException {
        if (countPapers < count) {
            throw new NotEnoughPapersException("Не хватает газет");
        } else {
            countPapers -= count;
            finance.earnMoney(count * cost);
        }
    }
    
    public void paidSalary(Worker worker, int money) throws NotEnoughMoneyException {
        if (getOwner().getBudget() < money) {
            throw new NotEnoughMoneyException("Не хватает денег");
        } else {
            worker.changeBudget(money);
            finance.spendMoney(money);
        }
    }

    public String getProfit() {
        return finance.getProfit();
    }
}
