package bog.lbpas.view3d.renderer.gui.elements;

import bog.lbpas.view3d.managers.assetLoading.ObjectLoader;
import bog.lbpas.view3d.core.Model;
import bog.lbpas.view3d.managers.MouseInput;
import bog.lbpas.view3d.managers.RenderMan;
import bog.lbpas.view3d.managers.WindowMan;
import bog.lbpas.view3d.renderer.gui.cursor.ECursor;
import bog.lbpas.view3d.renderer.gui.ingredients.LineStrip;
import bog.lbpas.view3d.utils.Config;
import bog.lbpas.view3d.utils.Cursors;
import org.joml.Math;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

/**
 * @author Bog
 */
public class Slider extends Element{

    float sliderPosition = 0;
    public boolean isSliding = false;
    float min = 0;
    float max = 100;

    Vector2f prevSize;
    Model outlineRect;

    public Slider(String id, Vector2f pos, Vector2f size, RenderMan renderer, ObjectLoader loader, WindowMan window)
    {
        this.id = id;
        this.pos = pos;
        this.size = size;
        this.prevSize = size;
        this.outlineRect = LineStrip.processVerts(LineStrip.getRectangle(new Vector2f(size.x, size.y * 0.2f)), loader, window);
        this.renderer = renderer;
        this.loader = loader;
        this.window = window;
    }

    public Slider(String id, Vector2f pos, Vector2f size, RenderMan renderer, ObjectLoader loader, WindowMan window, float sliderPosition, float min, float max)
    {
        this.id = id;
        this.pos = pos;
        this.size = size;
        this.prevSize = size;
        this.outlineRect = LineStrip.processVerts(LineStrip.getRectangle(new Vector2f(size.x, size.y * 0.2f)), loader, window);
        this.renderer = renderer;
        this.loader = loader;
        this.window = window;
        this.sliderPosition = ((sliderPosition - min)/(max - min)) * 100f;
        this.min = min;
        this.max = max;
    }

    @Override
    public void draw(MouseInput mouseInput, boolean overElement) {
        super.draw(mouseInput, overElement);

        if(size.x != prevSize.x || size.y != prevSize.y)
        {
            refreshOutline();
            prevSize = size;
        }

        if(isSliding)
        {
            sliderPosition = ((((float)mouseInput.currentPos.x - (size.y * 0.2f) / 2f) - pos.x)/(size.x - size.y * 0.2f)) * 100f;
            sliderPosition = Math.clamp(0, 100, sliderPosition);
        }

        Color c = Config.INTERFACE_PRIMARY_COLOR;
        Color c2 = Config.INTERFACE_PRIMARY_COLOR2;

        if(hovering || isSliding)
        {
            c = Config.INTERFACE_SECONDARY_COLOR;
            c2 = Config.INTERFACE_SECONDARY_COLOR2;
        }

        renderer.drawRect((int) pos.x, (int) (pos.y + size.y/2f - size.y * 0.1f), (int) size.x, (int) (size.y * 0.2f), c);
        renderer.drawRectOutline(new Vector2f((int) pos.x, (int) (pos.y + size.y/2f - size.y * 0.1f)), outlineRect, c2, false);
        if(!Float.isNaN(sliderPosition))
            renderer.drawRect((int) (pos.x + (sliderPosition * ((size.x - size.y * 0.1f)/100))), (int) (pos.y), (int) (size.y * 0.2f), (int) size.y, Config.FONT_COLOR);

    }
    @Override
    public void resize() {
        super.resize();
        refreshOutline();
    }
    public void refreshOutline()
    {
        if(this.outlineRect != null)
            this.outlineRect.cleanup(loader);
        this.outlineRect = LineStrip.processVerts(LineStrip.getRectangle(new Vector2f((int) size.x, (int) (size.y * 0.2f))), loader, window);
    }
    @Override
    public void onClick(MouseInput mouseInput, Vector2d pos, int button, int action, int mods, boolean overElement) {
        super.onClick(mouseInput, pos, button, action, mods, overElement);

        if(button == GLFW.GLFW_MOUSE_BUTTON_1)
        {
            if(action == GLFW.GLFW_PRESS && isMouseOverElement(pos) && !overElement)
                isSliding = true;
            else if(action == GLFW.GLFW_RELEASE)
                isSliding = false;
        }
    }

    public float getCurrentValue()
    {
        return ((max - min) * sliderPosition / 100f) + min;
    }

    public void setCurrentValue(float sliderPosition)
    {
        this.sliderPosition = ((sliderPosition - min)/(max - min)) * 100f;
    }

    public Vector2f setSliderValue(float value)
    {
        if(!isSliding)
            setCurrentValue(value);
        else
            return new Vector2f(getCurrentValue(), 1);

        return new Vector2f(0, 0);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if(!focused)
            isSliding = false;
    }

    @Override
    public void hoverCursor() {
        Cursors.setCursor(ECursor.hand2);
    }
}
