package Road;

import Objects.Car;
import Objects.Grid;
import Objects.TrafficLight;
import TrafficFlow.Drawer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by ArtemBulkhak on 09/03/2019.
 */
public class ThreeLaneRoad implements Road{

    private static final int[] y = {0, 4};
    private TrafficLight trafficLight1;
    private TrafficLight trafficLight2;

    private ArrayList<Car> first;
    private ArrayList<Car> second;
    private ArrayList<Car> third;
    private ArrayList<ArrayList<Car>> arrLines;


    public ThreeLaneRoad(Grid grid) {

        for (int i = 0; i < Drawer.width/Drawer.cellSize; i++) {
            grid.getCell(i, y[0]).setFree(true);
            grid.getCell(i, y[1]).setFree(true);
        }

        trafficLight1 = new TrafficLight(30, 3, grid);
        trafficLight2 = new TrafficLight(65, 3, grid);

        first = new ArrayList<>();
        second = new ArrayList<>();
        third = new ArrayList<>();
        arrLines = new ArrayList<>();
        arrLines.add(first);
        arrLines.add(second);
        arrLines.add(third);

    }

    @Override
    public void addCar(Car car){
        switch (car.getFirst().getY()){
            case 1:
                first.add(car);
                break;
            case 2:
                second.add(car);
                break;
            case 3:
                third.add(car);
                break;
        }
    }

    @Override
    public void removeCar(Car car) {
        switch (car.getFirst().getY()){
            case 1:
                first.remove(car);
                break;
            case 2:
                second.remove(car);
                break;
            case 3:
                third.remove(car);
                break;
        }
    }

    @Override
    public void sortLine() {
        first.sort(Car::compareTo);
        second.sort(Car::compareTo);
        third.sort(Car::compareTo);
    }

    @Override
    public ArrayList<ArrayList<Car>> getArrLines() {
        return arrLines;
    }

    @Override
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
    public TrafficLight getTrafficLight1(){
        return trafficLight1;
    }
    @Override
    public TrafficLight getTrafficLight2(){
        return trafficLight2;
    }



}
