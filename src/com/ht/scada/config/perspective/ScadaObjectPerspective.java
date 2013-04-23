package com.ht.scada.config.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.ht.scada.config.view.AreaIndexView;
import com.ht.scada.config.view.AreaTreeView;
import com.ht.scada.config.view.EnergyIndexView;
import com.ht.scada.config.view.EnergyTreeView;
import com.ht.scada.config.view.MainIndexView;
import com.ht.scada.config.view.ScadaObjectTreeView;
/**
 * 监控对象透视图
 * @author 赵磊
 *
 */
public class ScadaObjectPerspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "com.ht.scada.config.perspective.ScadaObjectPerspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		
//		layout.addPerspectiveShortcut(ID);
//		layout.addPerspectiveShortcut(ScadaDevicePerspective.ID);
		
//		layout.addView(ScadaObjectTreeView.ID, IPageLayout.LEFT, 0.25f, editorArea);
		IFolderLayout leftFolder = layout.createFolder("left", IPageLayout.LEFT, 0.25f, editorArea);
		leftFolder.addView(ScadaObjectTreeView.ID);
		
		leftFolder.addView(AreaTreeView.ID);
		
		leftFolder.addView(EnergyTreeView.ID);
		
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.LEFT, 1f, editorArea);
		folder.addPlaceholder(MainIndexView.ID + ":*");
//		folder.addView(MainIndexView.ID);
//		layout.addView(MainIndexView.ID, IPageLayout.LEFT, 1f, editorArea);
		
//		layout.getViewLayout(ScadaObjectTreeView.ID).setCloseable(false);
	}
}
