package lab3.buisness.stuff;

import lab3.creature.Human;

public class Worker extends Human {
    private Job job;
    private int budget = 0;

    public Worker(String name, Job job, int budget) {
        super(name);
        this.job = job;
    }

    public Job getJob() {
        return job;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void changeBudget(int budget) {
        this.budget += budget;
    }
}
