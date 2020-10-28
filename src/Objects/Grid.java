package Objects;

import TrafficFlow.Drawer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by ArtemBulkhak on 27/02/2019.
 */

public class Grid {

    private int width = Drawer.width/Drawer.cellSize;
    private int height = Drawer.height/Drawer.cellSize;

    private Cell[][] cells;

    public Grid() {
        cells = new Cell[width][height];

        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public void render() {
        for (int x = 0; x < width; x++){
            glBegin(GL_LINES);
            {
                glColor3dv(Drawer.GRAY);
                glVertex2i(x*Drawer.cellSize, -Drawer.height);
                glVertex2i(x*Drawer.cellSize, Drawer.height);
            }
            glEnd();
        }
        for (int y = 0; y < height; y++){
            glBegin(GL_LINES);
            {
                glColor3dv(Drawer.GRAY);
                glVertex2i(-Drawer.width, y*Drawer.cellSize);
                glVertex2i(Drawer.width, y*Drawer.cellSize);
            }
            glEnd();
        }
    }


    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

}
