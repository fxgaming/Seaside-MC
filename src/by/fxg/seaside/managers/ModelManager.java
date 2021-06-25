package by.fxg.seaside.managers;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class ModelManager {
	public static ModelManager instance;
	public WavefrontObject[] model_pickup = new WavefrontObject[12];
	
	public ModelManager() {
		instance = this;
		for (int i = 0; i != 12; i++) model_pickup[i] = (WavefrontObject)AdvancedModelLoader.loadModel("/mods/seaside/models/pickup_" + i + ".obj");
		
		//model_pizzaboy = (WavefrontObject)AdvancedModelLoader.loadModel("/mods/seaside/pizzaboy.obj");
	}
}
