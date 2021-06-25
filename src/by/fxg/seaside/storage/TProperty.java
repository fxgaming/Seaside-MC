package by.fxg.seaside.storage;

public class TProperty<T> {
	public T value;
	public boolean enabled;
	
	public TProperty(T value, boolean enabled) {
		this.value = value;
		this.enabled = enabled;
	}
	
	public TProperty setValue(T value) {
		this.value = value;
		return this;
	}
	
	public TProperty setEnabled(boolean value) {
		this.enabled = value;
		return this;
	}
}
