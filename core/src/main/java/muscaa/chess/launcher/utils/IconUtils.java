package muscaa.chess.launcher.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class IconUtils {
	
	public static BufferedImage getIcon(String path) {
		try {
			return ImageIO.read(IconUtils.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
