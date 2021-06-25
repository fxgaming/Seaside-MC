package by.fxg.seaside.systems.jobs;

import java.util.ArrayList;
import java.util.List;

public class Job {
	public EnumJob jobType;
	/** 0 - money  */
	public List<Object> vars;
	public int playerID;
	
	public Job(EnumJob job, int playerID) {
		this.jobType = job;
		this.vars = new ArrayList<Object>();
		this.playerID = playerID;
	}
}
