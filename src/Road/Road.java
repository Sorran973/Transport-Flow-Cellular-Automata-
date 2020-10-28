package Road;

import Objects.Car;
import Objects.TrafficLight;

import java.util.ArrayList;

/**
 * Created by ArtemBulkhak on 28/02/2019.
 */
public interface Road {

    void render();
    void addCar(Car car);
    void removeCar(Car car);
    void sortLine();

    ArrayList<ArrayList<Car>> getArrLines();
    TrafficLight getTrafficLight1();
    TrafficLight getTrafficLight2();

}
