package configuration;

import java.util.HashMap;

public interface Configurable {
	public HashMap<String, Object> getConfiguration();
	public HashMap<String, Object> getDefaultConfiguration();
	public void applyConfiguration(HashMap<String, Object> config);
}
