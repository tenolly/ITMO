package org.server;

import jakarta.faces.bean.ManagedBean;

@SuppressWarnings("deprecation")
@ManagedBean
public class FormBean {
    private double x;
    private double y;
    private double r;

    public void check() {
        // Логика для проверки значений
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }
}
