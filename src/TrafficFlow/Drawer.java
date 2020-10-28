package TrafficFlow;

import Objects.*;
import Road.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

import java.util.*;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Drawer {
    public static final double[] GRAY  = {0.4, 0.4, 0.4};
    public static final double[] YELLOW = {1.0, 1.0, 0.0};
    public static final double[] GREEN  = {0.0, 1.0, 0.0};
    public static final double[] RED  = {1.0, 0.0, 0.0};
    public static final double[] BLUE  = {0.0, 0.0, 1.0};
    public static final double[] AQUA = {0.0, 1.0, 1.0};
    public static final double[] PURPLE = {1.0, 0.0, 1.0};


    public static final int width = 1440;
    public static final int height = 200;
    public static final int cellSize = 16;

    private int t = 1;
    private int id = 1;


    private long window;
    private Grid grid;
    private Road road;
    private ArrayList<Car> cars;
    private ArrayList<Car> cars_res;
    private static ArrayList<double[]> colors;


    public Drawer() {

        GLFWErrorCallback.createPrint(System.err).set();



        grid = new Grid();
        road = new ThreeLaneRoad(grid);
//        road = new TwoLaneRoad(grid);
//        road = new OneLaneRoad(grid);
        cars = new ArrayList<>();
        cars_res = new ArrayList<>();


        colors = new ArrayList<>();
        colors.add(RED);
        colors.add(YELLOW);
        colors.add(GREEN);
        colors.add(BLUE);
        colors.add(AQUA);
        colors.add(PURPLE);



        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        this.window = GLFW.glfwCreateWindow(width, height, "App", 0, 0);

        if (window == 0) {
            throw new RuntimeException("Failed to create window");
        }

        glfwSetKeyCallback(window, GLFWKeyCallback.create(((window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                fluxDensity();
                glfwSetWindowShouldClose(window, true);
            }
            if (key == GLFW_KEY_Z && action == GLFW_PRESS) {
                test();
            }
            if (key == GLFW_KEY_B && action == GLFW_PRESS) {
                createCars();
            }
            if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                space();
            }
            if (key == GLFW_KEY_N && action == GLFW_PRESS) {
                road.getTrafficLight1().setLight();
            }
            if (key == GLFW_KEY_M && action == GLFW_PRESS) {
                road.getTrafficLight2().setLight();
            }
        })));

        GLFW.glfwMakeContextCurrent(window);
        glfwShowWindow(window);

        GL.createCapabilities();
        glClearColor(0, 0, 0, 0);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, 0, height, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        while (!glfwWindowShouldClose(window)){

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);


            grid.render();
            road.render();

            if (t == 1){
                road.getTrafficLight1().setLight();
            }

            t++;
            if (t % 2 == 0){
                createCars();
            }
            space();

            for (Car car: cars){
                car.render();
            }

            if (t >= 1000){
                fluxDensity();
                return;
            }


            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        glfwTerminate();
    }

    private int random(int min, int max){
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    private void createCars(){

        if (grid.getCell(6, 1).isFree()
                || grid.getCell(6, 2).isFree()
                || grid.getCell(6, 3).isFree()){
            return;
        } else {
            int numberOfCars = random(1, 3);
            Integer[] arr = new Integer[(numberOfCars)];
            for (int i = 0, j = 1; i < arr.length; i++) {
                arr[i] = j;
                j++;
            }
            Collections.shuffle(Arrays.asList(arr));

            for (int i = 0; i < numberOfCars; i++) {
                Car car = new Car(id++, colors.get(i), random(2, 5), 6, arr[i], grid);
                cars.add(car);
                road.addCar(car);
                cars_res.add(car);
            }
        }
    }

    private void fluxDensity(){  // k
        float q = trafficVolume();
        float v = averageSpeed();
        float p = density();
        System.out.println("numberOfCars = " + cars_res.size());
        System.out.println("t = " + t);
        System.out.println("q = " + q);
        System.out.println("p = " + p);
        System.out.println("v = " + v);
        System.out.println("k = " + q/v);
    }

    private float density(){  // p
        return (float) cars_res.size()/80;
    }

    private float trafficVolume(){  // q
        return (float) cars_res.size()/t;
    }

    private float averageSpeed(){  // v

        float sumSpeed = 0;
        for (Car car: cars_res){
            sumSpeed += (float) 80/car.getT();
        }
        return (float) sumSpeed/cars_res.size();
    }

    private void test(){
        Car car = new Car(id++, colors.get(0), 1, 15, 1, grid);
        cars.add(car);
        road.addCar(car);
        cars_res.add(car);

        Car car2 = new Car(id++, colors.get(1), 5, 11, 1, grid);
        cars.add(car2);
        road.addCar(car2);
        cars_res.add(car2);

        Car car3 = new Car(id++, colors.get(2), 5, 11, 3, grid);
        cars.add(car3);
        road.addCar(car3);
        cars_res.add(car3);

        Car car4 = new Car(id++, colors.get(3), 1, 15, 3, grid);
        cars.add(car4);
        road.addCar(car4);
        cars_res.add(car4);

        /**
         * Итог:

         Светофоры в разнобой, 10, 10, % = 2
         numberOfCars = 988
         t = 1000
         q = 0.988
         p = 12.35
         v = 3.6709182
         k = 0.26914245

         Светофоры в разнобой, 10, 10, % = 4
         numberOfCars = 509
         t = 1000
         q = 0.509
         p = 6.3625
         v = 3.995776
         k = 0.12738451

         Светофоры в разнобой, 10, 10, % = 8
         numberOfCars = 244
         t = 1000
         q = 0.244
         p = 3.05
         v = 4.6419578
         k = 0.05256403




         Светофоры в разнобой, 10, 10, % = 2
         numberOfCars = 988
         t = 1000
         q = 0.988
         p = 12.35
         v = 3.6709182
         k = 0.26914245

         Светофоры в разнобой, 10, 20, % = 2
         numberOfCars = 1000
         t = 1000
         q = 1.0
         p = 12.5
         v = 2.4500363
         k = 0.40815723

         Светофоры в разнобой, 10, 30, % = 2
         numberOfCars = 980
         t = 1000
         q = 0.98
         p = 12.25
         v = 2.9239256
         k = 0.33516586

         Светофоры в разнобой, 10, 40, % = 2
         numberOfCars = 944
         t = 1000
         q = 0.944
         p = 11.8
         v = 1.9291472
         k = 0.4893354

         Светофоры в разнобой, 10, 50, % = 2
         numberOfCars = 944
         t = 1000
         q = 0.944
         p = 11.8
         v = 2.1714513
         k = 0.4347323
         */
    }

    private void space(){
        Iterator iterator = cars.iterator();
        while (iterator.hasNext()) {
            Car car = (Car) iterator.next();
            try {
                if (car.laneChange()) {
                    double random = Math.random();
                    if (car.getY_laneChange() == 1 && random < car.getP_c() && t % 2 == 0) {
                        road.removeCar(car);
                        car.ruleLaneChange();
                        road.addCar(car);
                    } else if (car.getY_laneChange() == -1 && random < car.getP_c() && t % 2 > 0) {
                        road.removeCar(car);
                        car.ruleLaneChange();
                        road.addCar(car);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e){
                road.removeCar(car);
                iterator.remove();
            }
        }
        road.sortLine();
        for (ArrayList<Car> arr: road.getArrLines()){
            Iterator iterator3 = arr.iterator();
            while (iterator3.hasNext()) {
                Car car = (Car) iterator3.next();
                try {
                    car.movement();
                } catch (ArrayIndexOutOfBoundsException e){
                    iterator3.remove();
                }
            }
        }

        Iterator iterator2 = cars.iterator();
        while (iterator2.hasNext()) {
            Car car = (Car) iterator2.next();
            try {
                car.increaseT(); // ???
                car.ruleDriving();
            } catch (ArrayIndexOutOfBoundsException e){
                iterator2.remove();
            }
        }

//        t++;

        if (t % 10 == 0){
            road.getTrafficLight1().setLight();
//            road.getTrafficLight2().setLight();
        }
        if (t % 40 == 0){
            road.getTrafficLight2().setLight();
        }
    }

}