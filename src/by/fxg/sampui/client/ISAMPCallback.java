package by.fxg.sampui.client;

public interface ISAMPCallback {
	/**
	 * Calls when any of buttons of dialog has been pressed
	 * @param dialog - Dialog object
	 * @param dialogID - Dialog id
	 * @param buttonID - Button in dialog pressed
	 * @param mouseID - Mouse key pressed
	 * @param data - Ext. parameters like for SampDialogInput text or SampDialogList index
	 */
	void onButtonUse(ISAMPDialog dialog, int dialogID, int buttonID, int mouseID, Object... data);
}
