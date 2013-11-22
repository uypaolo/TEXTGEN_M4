package controller.listener.grammardev;

import java.awt.event.ActionListener;

import view.grammardevelopment.editsemantics.NewSemanticsInfoDialog;

public abstract class SemanticsInfoListener implements ActionListener {
	protected NewSemanticsInfoDialog dialog;
	
	public abstract void proceed(boolean isCanceled);
}
