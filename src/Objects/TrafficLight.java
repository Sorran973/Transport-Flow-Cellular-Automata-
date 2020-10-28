package Objects;

import TrafficFlow.Drawer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by ArtemBulkhak on 28/02/2019.
 */
public class TrafficLight {

    private boolean light = true;
    private double[][] colors = {Drawer.RED, Drawer.GREEN};
    private int x;
    private int y;
    private Grid grid;

    public TrafficLight(int x, int y, Grid grid) {

        this.x = x;
        this.y = y;
        this.grid = grid;
    }

    public void render(boolean i){
        if (i) {
            glColor3dv(colors[1]);
        } else {
            glColor3dv(colors[0]);
            for (int j = 1; j <= y; j++) {
                grid.getCell(x, j).setFree(true);
                grid.getCell(x, j).setSpeedCar(-2);
            }
        }

        glBegin(GL_QUADS);
        {
            glVertex2i(x*Drawer.cellSize, (y+2)*Drawer.cellSize+Drawer.cellSize);
            glVertex2i(x*Drawer.cellSize, (y+2)*Drawer.cellSize);
            glVertex2i(x*Drawer.cellSize+Drawer.cellSize, (y+2)*Drawer.cellSize);
            glVertex2i(x*Drawer.cellSize+Drawer.cellSize, (y+2)*Drawer.cellSize+ Drawer.cellSize);
        }
        glEnd();

        for (int j = 1; j <= y; j++){
            glColor3dv(Drawer.GRAY);
            glBegin(GL_QUADS);
            {
                glVertex2i(x*Drawer.cellSize, j*Drawer.cellSize+Drawer.cellSize);
                glVertex2i(x*Drawer.cellSize, j*Drawer.cellSize);
                glVertex2i(x*Drawer.cellSize+Drawer.cellSize, j*Drawer.cellSize);
                glVertex2i(x*Drawer.cellSize+Drawer.cellSize, j*Drawer.cellSize+ Drawer.cellSize);
            }
            glEnd();
        }

    }

    public void setLight(){
        this.light = !light;

        for (int i = 1; i <= y; i++) {
            if (light) {
                grid.getCell(x, i).setFree(false);
                grid.getCell(x, i).setSpeedCar(-1);
            } else {
                grid.getCell(x, i).setFree(true);
                grid.getCell(x, i).setSpeedCar(-2);
            }
        }

    }

    public boolean isLight() {
        return light;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
