package com.ht.scada.config.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.ht.scada.config.view.MainIndexView;
import com.ht.scada.config.view.ScadaObjectTreeView;

/**
 * 监控设备透视图
 * @author 赵磊
 *
 */
public class RTURemotePerspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "com.ht.scada.config.perspective.RTURemotePerspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
	}
}
