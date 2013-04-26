package com.ht.scada.config.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.actions.ActionFactory;



public class OpenViewAction extends Action {
	
	private final IWorkbenchWindow window;
	private final String viewId;
	private final String perspectiveId;
	
	public OpenViewAction(IWorkbenchWindow window, String perspectiveId, String viewId, String actionName) {
		this.window = window;
		this.viewId = viewId;
		this.perspectiveId = perspectiveId;
        setText(actionName);
        
        // The id is used to refer to the action in a menu or toolbar
		setId(viewId);
        // Associate the action with a pre-defined command, to allow key bindings.
		setImageDescriptor(test.Activator.getImageDescriptor("/icons/sample2.gif"));
	}
	
	public void run() {
		String oldId = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective().getId();
		if(!oldId.equals(this.perspectiveId)) {//打开另一个透视图
			IWorkbench w = PlatformUI.getWorkbench();
			ActionFactory.IWorkbenchAction closePerspectiveAction = ActionFactory.CLOSE_PERSPECTIVE
					.create(w.getActiveWorkbenchWindow());
			closePerspectiveAction.run();
			
			try {
				PlatformUI.getWorkbench().showPerspective(this.perspectiveId,
						PlatformUI.getWorkbench().getActiveWorkbenchWindow());
			} catch (WorkbenchException e) {
				e.printStackTrace();
			}
		}
		
		if(window != null) {	
			try {
				window.getActivePage().showView(viewId);
			} catch (PartInitException e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
}
