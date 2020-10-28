package Objects;

import TrafficFlow.Drawer;

/**
 * Created by ArtemBulkhak on 27/02/2019.
 */
public class Cell{

    private int x;
    private int y;
    private int speedCar;
    private boolean b;
    private boolean free;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.speedCar = -1;
        this.free = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeedCar() {
        return speedCar;
    }

    public void setSpeedCar(int speedCar) {
        this.speedCar = speedCar;
    }

    public boolean getB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
