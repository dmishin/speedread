package configuration;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.prefs.Preferences;


public class PropsConfiguration {
	private String prefix;
	private Preferences prefs;
	public PropsConfiguration(Preferences p, String prefix){
		this.prefix = prefix;
		this.prefs = p;
	}
	public void save(Configurable object){
		HashMap<String, Object> cfg = object.getConfiguration();
		for(Entry<String, Object> entry: cfg.entrySet()){
			String key = prefix + entry.getKey();
			Object v = entry.getValue();
			if (v instanceof String){
				prefs.put(key, (String) v);
			}else if(v instanceof Integer){
				prefs.putInt(key, (Integer)v);
			}else if(v instanceof Double){
				prefs.putDouble(key, (Double)v);				
			}else
				throw new RuntimeException("Unknown config key type: "+key+ " "+ v);
		}
	}
	public void load(Configurable object){
		HashMap<String, Object> cfg = object.getDefaultConfiguration();
		for(Entry<String, Object> entry: cfg.entrySet()){
			String key = prefix + entry.getKey();
			Object defVal = entry.getValue();
			if (defVal instanceof String){
				cfg.put(entry.getKey(), prefs.get(key, (String) defVal));
			}else if(defVal instanceof Integer){
				cfg.put(entry.getKey(), prefs.getInt(key, (Integer) defVal));
			}else if(defVal instanceof Double){
				cfg.put(entry.getKey(), prefs.getDouble(key, (Double) defVal));
			}else
				throw new RuntimeException("Unknown config key type: "+key+ " "+ defVal);
		}
		object.applyConfiguration(cfg);
	}
}
