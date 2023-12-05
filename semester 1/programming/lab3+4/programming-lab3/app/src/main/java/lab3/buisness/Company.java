package lab3.buisness;

import java.util.ArrayList;

import lab3.buisness.materials.Equipment;
import lab3.buisness.stuff.Worker;
import lab3.locations.base.Building;

public class Company extends Building {
    protected Worker owner;
    private ArrayList<Worker> stuff = new ArrayList<Worker>();
    private ArrayList<Equipment> equipments = new ArrayList<Equipment>();

    public Company(String name, Worker owner) {
        super(name);
        this.owner = owner;
    }

    public Worker getOwner() {
        return owner;
    }

    public void add(Worker worker) {
        stuff.add(worker);
    }

    public void remove(Worker worker) {
        stuff.remove(worker);
    }

    public void add(Equipment equipment) {
        equipments.add(equipment);
    }

    public void remove(Equipment equipment) {
        equipments.remove(equipment);
    }
}
