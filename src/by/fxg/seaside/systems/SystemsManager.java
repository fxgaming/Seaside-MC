package by.fxg.seaside.systems;

import by.fxg.seaside.SVars;
import by.fxg.seaside.systems.jobs.JobSystem;
import by.fxg.seaside.systems.punishment.Punishments;

public class SystemsManager {
	public SystemsManager register() {
		SVars.load();
		Database.init();
		
		GameLogging logging = new GameLogging();
		Punishments punishments = new Punishments();
		JobSystem jobSystem = new JobSystem();
		
		return this;
	}
}
