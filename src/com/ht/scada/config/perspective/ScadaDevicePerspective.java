package com.ht.scada.config.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.ht.scada.config.view.AreaIndexView;
import com.ht.scada.config.view.AreaTreeView;
import com.ht.scada.config.view.EndTagView;
import com.ht.scada.config.view.EnergyIndexView;
import com.ht.scada.config.view.EnergyTreeView;
import com.ht.scada.config.view.MainIndexView;
import com.ht.scada.config.view.ScadaDeviceTreeView;
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
		
//		IFolderLayout leftFolder = layout.createFolder("device_left", IPageLayout.LEFT, 0.25f, editorArea);
//		leftFolder.addPlaceholder(ScadaDeviceTreeView.ID);
		layout.addStandaloneView(ScadaDeviceTreeView.ID, true, IPageLayout.LEFT, 0.25f, editorArea);
//		leftFolder.addView(ScadaObjectTreeView.ID);
		
		
//		IFolderLayout folder = layout.createFolder("device_messages", IPageLayout.LEFT, 1f, editorArea);
//		folder.addPlaceholder(MainIndexView.ID);
//		folder.addPlaceholder(EndTagView.ID);
//		folder.addPlaceholder(AreaIndexView.ID);
//		folder.addPlaceholder(EnergyIndexView.ID);
	}
}
