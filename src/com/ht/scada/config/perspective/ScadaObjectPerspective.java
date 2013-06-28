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
import com.ht.scada.config.view.ScadaChannelConfigView;
import com.ht.scada.config.view.ScadaDeviceConfigView;
import com.ht.scada.config.view.ScadaDeviceTableTreeView;
import com.ht.scada.config.view.ScadaDeviceTreeView;
import com.ht.scada.config.view.ScadaObjectTreeView;
import com.ht.scada.config.view.VariableGroupConfigView;
import com.ht.scada.config.view.VariableTemplateConfigView;
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
		
		IFolderLayout leftFolder = layout.createFolder("left", IPageLayout.LEFT, 0.25f, editorArea);
		leftFolder.addPlaceholder(ScadaObjectTreeView.ID);
		leftFolder.addPlaceholder(AreaTreeView.ID);
		leftFolder.addPlaceholder(EnergyTreeView.ID);
		
		leftFolder.addPlaceholder(ScadaDeviceTreeView.ID);
		leftFolder.addPlaceholder(ScadaDeviceTableTreeView.ID);					//表格树显示在左侧
		
		leftFolder.addPlaceholder(VariableGroupConfigView.ID);
		leftFolder.addPlaceholder(VariableTemplateConfigView.ID);
		
		
		
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.LEFT, 1f, editorArea);
		folder.addPlaceholder(MainIndexView.ID);
		folder.addPlaceholder(EndTagView.ID);
		folder.addPlaceholder(AreaIndexView.ID);
		folder.addPlaceholder(EnergyIndexView.ID);
		
		folder.addPlaceholder(ScadaChannelConfigView.ID);
		folder.addPlaceholder(ScadaDeviceConfigView.ID);
		
//		layout.getViewLayout(ScadaObjectTreeView.ID).setCloseable(false);
	}
}
