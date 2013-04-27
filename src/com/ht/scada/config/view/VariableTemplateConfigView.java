package com.ht.scada.config.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Button;

/**
 * 变量模板配置
 * @author 赵磊
 *
 */
public class VariableTemplateConfigView extends ViewPart implements IPropertyChangeListener {

	public VariableTemplateConfigView() {

	}
	
	private MenuManager menuMng;

	public static final String ID = "com.ht.scada.config.view.VariableTemplateConfigView";

	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm = new SashForm(parent, SWT.BORDER | SWT.SMOOTH);
		sashForm.setSashWidth(1);
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ListViewer listViewer = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		
		menuMng = new MenuManager();
		menuMng.setRemoveAllWhenShown(true);
		menuMng.addMenuListener(new MenuListener(listViewer));

		list.setMenu(menuMng.createContextMenu(list)); // 添加菜单
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		
		Button button = new Button(composite_1, SWT.NONE);
		button.setBounds(265, 428, 80, 27);
		button.setText(" 保  存 ");
		
		Button button_1 = new Button(composite_1, SWT.NONE);
		button_1.setText(" 取  消 ");
		button_1.setBounds(365, 428, 80, 27);
		sashForm.setWeights(new int[] {80, 325});

	}
	
	private class MenuListener implements IMenuListener {
		private ListViewer listViewer;

		public MenuListener(ListViewer listViewer) {
			this.listViewer = listViewer;
		}

		@Override
		public void menuAboutToShow(IMenuManager manager) {
			IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
			if (!selection.isEmpty()) {
				createContextMenu(selection.getFirstElement());
			} else {//新建
				Action action = new Action() {
					public void run() {
					}
				};
				action.setText("新建变量模板");
				menuMng.add(action);
			}
		}

		/**
		 * 右键菜单内容
		 * 
		 * @param
		 */
		private void createContextMenu(final Object selectedObject) {
			if (selectedObject instanceof String) {
				
				

			}
		}

	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
