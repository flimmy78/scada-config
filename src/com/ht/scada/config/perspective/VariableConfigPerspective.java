package com.ht.scada.config.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.ht.scada.config.view.VariableGroupConfigView;
import com.ht.scada.config.view.VariableTemplateConfigView;
/**
 * 变量配置透视图
 * @author 赵磊
 *
 */
public class VariableConfigPerspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "com.ht.scada.config.perspective.VariableConfigPerspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
//		layout.addPerspectiveShortcut(ID);
//		layout.addPerspectiveShortcut(ScadaDevicePerspective.ID);
		
		IFolderLayout leftFolder = layout.createFolder("left", IPageLayout.LEFT, 1f, editorArea);
		leftFolder.addPlaceholder(VariableGroupConfigView.ID);
		leftFolder.addPlaceholder(VariableTemplateConfigView.ID);
		
	}
}
