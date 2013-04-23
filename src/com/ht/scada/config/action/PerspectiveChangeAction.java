package com.ht.scada.config.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.actions.ActionFactory;

public class PerspectiveChangeAction extends Action {

	private final IWorkbenchWindow window;
	private final String perspectiveId;

	public PerspectiveChangeAction(String text, String perspectiveId, IWorkbenchWindow window) {
		super(text);
		this.window = window;
		this.perspectiveId = perspectiveId;
		setId(perspectiveId);
	}
	// 
	public void run() {
		IWorkbench w = PlatformUI.getWorkbench();
		ActionFactory.IWorkbenchAction closePerspectiveAction = ActionFactory.CLOSE_PERSPECTIVE
				.create(w.getActiveWorkbenchWindow());
		closePerspectiveAction.run();
		
		try {
			PlatformUI.getWorkbench().showPerspective(perspectiveId,
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}

	}
}