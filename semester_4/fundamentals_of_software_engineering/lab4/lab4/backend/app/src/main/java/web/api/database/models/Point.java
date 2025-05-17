package web.api.database.models;

public class Point {
    private int ownerId;
    private double x;
    private double y;
    private double r;
    private boolean result;

    public Point(double x, double y, double r, boolean result) {
        this.x = x;
        this.y = y;
        this.r = r;
		this.result = result;
    }
    
    public Point(double x, double y, double r, boolean result, int ownerId) {
        this.x = x;
        this.y = y;
        this.r = r;
		this.result = result;
        this.ownerId = ownerId;
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

	public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}