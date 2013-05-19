package com.ht.scada.config.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.actions.ActionFactory;



public class OpenWindowAction extends Action {
	
	private final IWorkbenchWindow window;
	private ApplicationWindow myWindow;
	
	public OpenWindowAction(IWorkbenchWindow window, String actionName, ApplicationWindow myWindow) {
		this.window = window;
		this.myWindow = myWindow;
        setText(actionName);
        
        // The id is used to refer to the action in a menu or toolbar
		setId("window" + actionName);
        // Associate the action with a pre-defined command, to allow key bindings.
		//setImageDescriptor(test.Activator.getImageDescriptor("/icons/sample2.gif"));
	}
	
	public void run() {
		myWindow.open();
	}
}
