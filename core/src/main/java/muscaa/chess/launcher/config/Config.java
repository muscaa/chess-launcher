package muscaa.chess.launcher.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import fluff.functions.gen.obj.obj.Func2;
import fluff.json.JSON;
import fluff.json.JSONObject;

public class Config {
	
	private final List<Value<?>> values = new LinkedList<>();
	private final File file;
	
	public Config(File file) {
		this.file = file;
	}
	
	public <V> Value<V> value(String key, Func2<V, JSONObject, String> func, V defaultValue) {
		Value<V> value = new Value<>(key, func, defaultValue);
		values.add(value);
		return value;
	}
	
	public <V> Value<V> value(String key, Func2<V, JSONObject, String> func) {
		return value(key, func, null);
	}
	
	public void load() {
		if (!file.exists()) return;
		
		try (FileInputStream fis = new FileInputStream(file)) {
			String text = new String(fis.readAllBytes());
			JSONObject json = JSON.object(text);
			
			for (Value<?> value : values) {
				value.load(json);
			}
		} catch (IOException e) {}
	}
	
	public void save() {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			JSONObject json = JSON.object();
			
			for (Value<?> value : values) {
				value.save(json);
			}
			
			fos.write(json.toPrettyString().getBytes());
		} catch (IOException e) {}
	}
}
