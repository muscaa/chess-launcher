package muscaa.chess.launcher.config;

import fluff.functions.gen.obj.obj.Func2;
import fluff.json.JSONObject;

public class Value<V> {
	
	private final String key;
	private final Func2<V, JSONObject, String> func;
	private final V defaultValue;
	
	private V value;
	
	public Value(String key, Func2<V, JSONObject, String> func, V defaultValue) {
        this.key = key;
        this.func = func;
        this.defaultValue = defaultValue;
        
        this.value = defaultValue;
	}
	
	public V getDefaultValue() {
		return defaultValue;
	}
	
	public V get() {
		return value;
	}
	
	public void set(V value) {
		this.value = value;
	}
	
	public boolean isSet() {
		return value != null;
	}
	
	protected void load(JSONObject json) {
		if (json.contains(key)) {
			value = func.invoke(json, key);
		} else {
			value = defaultValue;
		}
	}
	
	protected void save(JSONObject json) {
		if (isSet()) {
			json.put(key, value);
		}
	}
}
