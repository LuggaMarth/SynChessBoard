package at.synchess.boardsoftware.front.model;

import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class ControllerUtils {
    public static FontIcon getFontIcon(String name, int size, Color fill) {
        FontIcon ret = new FontIcon(name);
        ret.setIconSize(size);
        ret.setFill(fill);

        return ret;
    }
}
