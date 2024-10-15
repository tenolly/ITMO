package org.server.beans;

import java.sql.SQLException;

import org.server.database.PostgreSQLJDBC;
import org.server.database.models.Point;
import org.server.utils.Area;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("formBean")
@ApplicationScoped
public class FormBean {
    private double x;
    private double y;
    private double r;

    public void check() {
        try (var connection = PostgreSQLJDBC.connect()) {
            PostgreSQLJDBC.createPointsTable(connection);
            PostgreSQLJDBC.insertPoint(connection, new Point(x, y, r, Area.Checker.hit(x, y, r)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }
}
