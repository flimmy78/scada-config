package com.ht.scada.config.scadaconfig;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(600, 400));
		configurer.setShowCoolBar(true); // 是否显示工具栏
		configurer.setShowStatusLine(false);

		configurer.setShowPerspectiveBar(true);// 是否显示透视图栏

//		PlatformUI.getPreferenceStore().setValue(
//				IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
//				IWorkbenchPreferenceConstants.TOP_RIGHT);
//		PlatformUI.getPreferenceStore().setDefault(
//				IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS,
//				false);
//		PlatformUI.getPreferenceStore().setValue(
//				IWorkbenchPreferenceConstants.SHOW_PROGRESS_ON_STARTUP, true);

		// MessageDialog.openWarning(Display.getDefault().getActiveShell().getShell(),
		// "", "");
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		// 使得窗口最大化
		this.getWindowConfigurer().getWindow().getShell().setMaximized(true);
	}

}
