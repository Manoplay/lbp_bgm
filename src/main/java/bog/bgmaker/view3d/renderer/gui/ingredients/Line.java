package bog.bgmaker.view3d.renderer.gui.ingredients;

import bog.bgmaker.view3d.ObjectLoader;
import bog.bgmaker.view3d.core.Model;
import bog.bgmaker.view3d.managers.WindowMan;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.awt.*;

/**
 * @author Bog
 */
public class Line extends Drawable{

    public Model model;
    public Vector4f color;
    public boolean smooth;

    public Line(Vector2i pos1, Vector2i pos2, ObjectLoader loader, WindowMan window, boolean smooth) {
        this.color = new Vector4f(1, 1, 1, 1);
        this.smooth = smooth;
        this.model = getLine(window, loader, pos1, pos2);
        this.staticVAO = false;
        this.staticVBO = false;
    }

    public Line(Vector2i pos1, Vector2i pos2, Color color, ObjectLoader loader, WindowMan window, boolean smooth) {
        this.color = new Vector4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        this.model = getLine(window, loader, pos1, pos2);
        this.smooth = smooth;
        this.staticVAO = false;
        this.staticVBO = false;
    }

    public Line(Model line, WindowMan window, boolean smooth) {
        this.color = new Vector4f(1, 1, 1, 1);
        this.smooth = smooth;
        this.model = line;
    }

    public Line(Model line, Color color, WindowMan window, boolean smooth) {
        this.color = new Vector4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        this.model = line;
        this.smooth = smooth;
    }

    public static Model getLine(WindowMan window, ObjectLoader loader, Vector2i pos1, Vector2i pos2)
    {
        if(window != null && !window.isMinimized) {
            float x1 = pos1.x / (window.width / 2f) - 1 + 1 / window.width;
            float y1 = -pos1.y / (window.height / 2f) + 1 - 1 / window.height;

            float x2 = pos2.x / (window.width / 2f) - 1 + 1 / window.width;
            float y2 = -pos2.y / (window.height / 2f) + 1 - 1 / window.height;

            return loader.loadModel(new float[]{x1, y1, x2, y2});
        }
        return null;
    }

    @Override
    public int getType() {
        return 2;
    }
}
