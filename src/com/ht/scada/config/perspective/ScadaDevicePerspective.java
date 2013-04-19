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
public class ScadaDevicePerspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "com.ht.scada.config.perspective.ScadaDevicePerspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		
//		layout.addPerspectiveShortcut(ID);
//		layout.addPerspectiveShortcut(ScadaDevicePerspective.ID);
		
//		layout.addStandaloneView(NavigationView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
//		IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP, 0.5f, editorArea);
//		folder.addPlaceholder(View.ID + ":*");
//		folder.addView(View.ID);
//		
//		layout.getViewLayout(NavigationView.ID).setCloseable(false);
	}
}
