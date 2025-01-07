package muscaa.chess.launcher.config.configs;

import java.io.File;

import fluff.json.JSONObject;
import muscaa.chess.launcher.config.Config;
import muscaa.chess.launcher.config.Value;
import muscaa.chess.launcher.utils.FileUtils;

public class Settings {
	
	private static final Config CFG = new Config(new File(FileUtils.launcherConfig, "settings.json"));
	
	public static Value<Integer> WINDOW_X = CFG.value("window_x", JSONObject::getInt);
	public static Value<Integer> WINDOW_Y = CFG.value("window_y", JSONObject::getInt);
	public static Value<Integer> WINDOW_WIDTH = CFG.value("window_width", JSONObject::getInt, 640);
	public static Value<Integer> WINDOW_HEIGHT = CFG.value("window_height", JSONObject::getInt, 480);
	public static Value<Integer> WINDOW_STATE = CFG.value("window_state", JSONObject::getInt);
	
	public static void load() {
		CFG.load();
	}
	
	public static void save() {
		CFG.save();
	}
}
