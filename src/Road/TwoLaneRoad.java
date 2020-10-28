package Road;

import Objects.Car;
import Objects.Grid;
import Objects.TrafficLight;
import TrafficFlow.Drawer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by ArtemBulkhak on 01/03/2019.
 */
public class TwoLaneRoad implements Road {

    private static final int[] y = {0, 3};
    private TrafficLight trafficLight1;
    private TrafficLight trafficLight2;

    public TwoLaneRoad(Grid grid) {

        for (int i = 0; i < Drawer.width/Drawer.cellSize; i++) {
            grid.getCell(i, y[0]).setFree(true);
            grid.getCell(i, y[1]).setFree(true);
        }

        trafficLight1 = new TrafficLight(40, 2, grid);
        trafficLight2 = new TrafficLight(70, 2, grid);
    }

    public void render() {

        trafficLight1.render(trafficLight1.isLight());
        trafficLight2.render(trafficLight2.isLight());

        for (int i = 0; i < Drawer.width/Drawer.cellSize; i++){
            GL11.glColor3dv(Drawer.GRAY);
            glBegin(GL_QUADS);
            {
                glVertex2i(i*Drawer.cellSize, y[0]*Drawer.cellSize+ Drawer.cellSize);
                glVertex2i(i*Drawer.cellSize, y[0]*Drawer.cellSize);
                glVertex2i(i*Drawer.cellSize+ Drawer.cellSize, y[0]*Drawer.cellSize);
                glVertex2i(i*Drawer.cellSize+ Drawer.cellSize, y[0]*Drawer.cellSize+ Drawer.cellSize);
            }
            glEnd();
        }

        for (int i = 0; i < Drawer.width/Drawer.cellSize; i++){
            GL11.glColor3dv(Drawer.GRAY);
            glBegin(GL_QUADS);
            {
                glVertex2i(i*Drawer.cellSize, y[1]*Drawer.cellSize+Drawer.cellSize);
                glVertex2i(i*Drawer.cellSize, y[1]*Drawer.cellSize);
                glVertex2i(i*Drawer.cellSize+ Drawer.cellSize, y[1]*Drawer.cellSize);
                glVertex2i(i*Drawer.cellSize+ Drawer.cellSize, y[1]*Drawer.cellSize+Drawer.cellSize);
            }
            glEnd();
        }

    }

    @Override
    public void addCar(Car car) {

    }

    @Override
    public void removeCar(Car car) {

    }

    @Override
    public void sortLine() {

    }

    @Override
    public ArrayList<ArrayList<Car>> getArrLines() {
        return null;
    }

    @Override
    public TrafficLight getTrafficLight1(){
        return trafficLight1;
    }
    @Override
    public TrafficLight getTrafficLight2(){
        return trafficLight2;
    }
}
