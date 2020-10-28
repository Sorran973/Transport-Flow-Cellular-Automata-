package Objects; /**
 * Created by ArtemBulkhak on 27/02/2019.
 */

import TrafficFlow.Drawer;

import static org.lwjgl.opengl.GL11.*;

public class Car implements Comparable<Car>{

    private Grid grid;
    private int id;
    private double[] color;
    private Cell first;
    private Cell second;
    private int t = 0;

    private int g;
    private int speed;
    private boolean changeSpeed;
    private int oldY;


    private int y_laneChange = 0;
    private static final int d = 6;
    private static final int maxSpeed = 5;
    private boolean b = false;

    /** Парамметры вероятности */
    private static final double p = 0.05; // вероятность случайного замедления
    private static final double p_c = 0.99; // вероятность смены полосы (дефолт = 0.05 странно)
    private static final double p_sts = 0.8; // Вероятность срабатывания правила медленного старта (дефолт = 0.68 маловато)
    private static final double p_ds = 0.8; // вероятность срабатывания правила пространственного упреждения
    private static final double p_s = 0.4; // вероятность срабатывания правила превышения скорости (дефолт 0.7, многовато)



    public Car(int id, double[] color, int speed, int x1, int y1, Grid grid) {
        this.grid = grid;
        this.id = id;
        this.speed = speed;
//        this.changeSpeed = changeSpeed;
        this.color = color;

        this.first = new Cell(x1, y1);
        this.second = new Cell((x1-1), y1);
    }

    public void increaseT(){
        this.t += 1;
    }


    public boolean laneChange(){
        if (conditionLaneFront()){
            return  (!conditionLaneLeft() || !conditionLaneRight());
        } else
            return false;

    }

    public void movement(){
        ruleAcceleration();
        ruleDeceleration();
        ruleRandomDeceleration();
        ruleNegativSpeed();
    }

    private void ruleNegativSpeed() {
        if (speed < 0){
            speed = 0;
            grid.getCell(first.getX(), first.getY()).setSpeedCar(speed);
            grid.getCell(second.getX(), second.getY()).setSpeedCar(speed);
        }
    }

    private void ruleRandomDeceleration() {
        if (Math.random() < p){
            speed = speed - 1;
            grid.getCell(first.getX(), first.getY()).setSpeedCar(speed);
            grid.getCell(second.getX(), second.getY()).setSpeedCar(speed);
        }
    }

    private boolean conditionLaneFront(){
        boolean flag = false;
        for (int i  = 1; i <= d; i++) {  //  ???
            if (grid.getCell(first.getX() + i, first.getY()).isFree()) {
                g = i;
                break;
            }
        }

        for (int i  = 1; i <= g; i++) { //  ???
            if (grid.getCell(first.getX() + i, first.getY()).isFree() &&
                    grid.getCell(first.getX() + i, first.getY()).getSpeedCar() < speed){  // ???
                flag = true;
                break;
            }

        }
        return flag;
    }

    private boolean conditionLaneLeft() {
        boolean flag = false;
        int x = first.getX();
        int y = first.getY()+1;
        for (int i = -maxSpeed-1; i <= g; i++){
            int d_l = i;
            if (grid.getCell(x+i, y).isFree()) {
                if (d_l <= g) {
                    flag = true;
                    break;
                }
                speed = d_l-1;
            }
        }
        y_laneChange = 1;

        return flag;
    }

    private boolean conditionLaneRight(){
        boolean flag = false;
        int x = first.getX();
        int y = first.getY()-1;
        for (int i = -maxSpeed-1; i <= g; i++){   // ???
            int d_r = i;
            if (grid.getCell(x+i, y).isFree()){
                if (d_r <= g) {
                    flag = true;
                    break;
                }
                speed = d_r-1;
            }
        }
        y_laneChange = -1;

        return flag;
    }


    private void ruleDeceleration(){
        int otherSpeed;
        if (g == 0){
            return;
        } else {
            otherSpeed = grid.getCell(first.getX() + g, first.getY()).getSpeedCar();
        }

        if (Math.random() < p_ds
                && speed > 0
                && otherSpeed > 0
                && g <= d
                && (grid.getCell(first.getX() + g, first.getY()).getB() || speed > otherSpeed)){
            speed = otherSpeed;
//            if (speed > g){
//                speed = g;
//            }
            b = true;
            grid.getCell(first.getX(), first.getY()).setB(true);
            grid.getCell(second.getX(), second.getY()).setB(true);
            grid.getCell(first.getX(), first.getY()).setSpeedCar(speed);
            grid.getCell(second.getX(), second.getY()).setSpeedCar(speed);
        }

        if ((otherSpeed == -2 || otherSpeed == 0) && g > 1){
            speed = 1;
            b = true;
            grid.getCell(first.getX(), first.getY()).setB(true);
            grid.getCell(second.getX(), second.getY()).setB(true);
            grid.getCell(first.getX(), first.getY()).setSpeedCar(speed);
            grid.getCell(second.getX(), second.getY()).setSpeedCar(speed);
        }
        if ((otherSpeed == -2 || otherSpeed == 0) && g == 1){
            speed = 0;
            b = true;
            grid.getCell(first.getX(), first.getY()).setB(true);
            grid.getCell(second.getX(), second.getY()).setB(true);
            grid.getCell(first.getX(), first.getY()).setSpeedCar(speed);
            grid.getCell(second.getX(), second.getY()).setSpeedCar(speed);
        }
    }

    private void ruleAcceleration(){
        for (int i  = 1; i <= d; i++) {  //  ???
            if (grid.getCell(first.getX() + i, first.getY()).isFree()) {
                g = i;
                break;
            } else
                g = i;
        }
        b = false;
        /** 2 - d_sts - Пространство, которое необходимо для начала движения автомобилю при срабатывании правила медленного старта */
        if (Math.random() < p_sts && speed == 0 && g <= 1){
            speed = 0;
            grid.getCell(first.getX(), first.getY()).setSpeedCar(speed);
            grid.getCell(second.getX(), second.getY()).setSpeedCar(speed);
        } else if (speed < maxSpeed){
            speed++;
            grid.getCell(first.getX(), first.getY()).setSpeedCar(speed);
            grid.getCell(second.getX(), second.getY()).setSpeedCar(speed);
        }
    }

    public void ruleLaneChange(){
        oldY = first.getY();
        grid.getCell(first.getX(), oldY).setFree(false);
        grid.getCell(second.getX(), oldY).setFree(false);
        grid.getCell(first.getX(), oldY).setSpeedCar(-1);
        grid.getCell(second.getX(), oldY).setSpeedCar(-1);
        grid.getCell(first.getX(), oldY).setB(false);
        grid.getCell(second.getX(), oldY).setB(false);
        first.setY(oldY+y_laneChange);
        second.setY(oldY+y_laneChange);
    }

    public void ruleDriving() {

        grid.getCell(first.getX(), first.getY()).setFree(false);
        grid.getCell(first.getX(), first.getY()).setSpeedCar(-1);
        grid.getCell(first.getX(), first.getY()).setB(false);
        grid.getCell(second.getX(), second.getY()).setFree(false);
        grid.getCell(second.getX(), second.getY()).setSpeedCar(-1);
        grid.getCell(second.getX(), second.getY()).setB(false);


        first.setX(first.getX() + speed);
        second.setX(second.getX() + speed);


        grid.getCell(first.getX(), first.getY()).setFree(true);
        grid.getCell(second.getX(), second.getY()).setFree(true);
        grid.getCell(first.getX(), first.getY()).setSpeedCar(speed);
        grid.getCell(second.getX(), second.getY()).setSpeedCar(speed);
        grid.getCell(first.getX(), first.getY()).setB(b);
        grid.getCell(second.getX(), second.getY()).setB(b);

    }


    public void render() {

        grid.getCell(first.getX(), first.getY()).setFree(true);
        grid.getCell(second.getX(), second.getY()).setFree(true);

        grid.getCell(first.getX(), first.getY()).setSpeedCar(speed);
        grid.getCell(second.getX(), second.getY()).setSpeedCar(speed);


        /** Отрисовка автомобиля */
        glColor3dv(color);
        glBegin(GL_QUADS);
        {
            glVertex2i(first.getX()*Drawer.cellSize, first.getY()*Drawer.cellSize+Drawer.cellSize);
            glVertex2i(first.getX()*Drawer.cellSize, first.getY()*Drawer.cellSize);
            glVertex2i(first.getX()*Drawer.cellSize+Drawer.cellSize, first.getY()*Drawer.cellSize);
            glVertex2i(first.getX()*Drawer.cellSize+Drawer.cellSize, first.getY()*Drawer.cellSize+Drawer.cellSize);
        }
        glEnd();

        glBegin(GL_QUADS);
        {
            glVertex2i(second.getX()*Drawer.cellSize, second.getY()*Drawer.cellSize+Drawer.cellSize);
            glVertex2i(second.getX()*Drawer.cellSize, second.getY()*Drawer.cellSize);
            glVertex2i(second.getX()*Drawer.cellSize+Drawer.cellSize, second.getY()*Drawer.cellSize);
            glVertex2i(second.getX()*Drawer.cellSize+Drawer.cellSize, second.getY()*Drawer.cellSize+Drawer.cellSize);
        }
        glEnd();


        /** Отрисовка фар */
        glColor3dv(Drawer.YELLOW);
        glBegin(GL_TRIANGLES);
        {
            glVertex2i(first.getX()*Drawer.cellSize+2*Drawer.cellSize*15/16, first.getY()*Drawer.cellSize+Drawer.cellSize);
            glVertex2i(first.getX()*Drawer.cellSize+2*Drawer.cellSize*15/16, first.getY()*Drawer.cellSize+Drawer.cellSize*5/8);
            glVertex2d(first.getX()*Drawer.cellSize+Drawer.cellSize, first.getY()*Drawer.cellSize+Drawer.cellSize*6/8);
        }
        glEnd();
        glColor3dv(Drawer.YELLOW);
        glBegin(GL_TRIANGLES);
        {
            glVertex2i(first.getX()*Drawer.cellSize+2*Drawer.cellSize*15/16, first.getY()*Drawer.cellSize);
            glVertex2i(first.getX()*Drawer.cellSize+2*Drawer.cellSize*15/16, first.getY()*Drawer.cellSize+Drawer.cellSize*4/8);
            glVertex2d(first.getX()*Drawer.cellSize+Drawer.cellSize, first.getY()*Drawer.cellSize+Drawer.cellSize*2/8);
        }
        glEnd();
    }

    public Cell getFirst(){
        return first;
    }

    public int getY_laneChange() {
        return y_laneChange;
    }

    @Override
    public int compareTo(Car o) {
        return o.getFirst().getX()-this.getFirst().getX();
    }

    public double getP() {
        return p;
    }

    public double getP_c() {
        return p_c;
    }

    public double getP_ds() {
        return p_ds;
    }

    public double getP_s() {
        return p_s;
    }

    public int getT() {
        return t;
    }
}
