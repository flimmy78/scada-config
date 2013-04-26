package com.ht.scada.config.view;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

public class ScadaDeviceTreeView extends ViewPart {
	
	private static final Logger log = LoggerFactory.getLogger(ScadaDeviceTreeView.class);
	
	
	public ScadaDeviceTreeView() {
	}

	public static final String ID = "com.ht.scada.config.view.ScadaDeviceTreeView";

	private MenuManager menuMng;
	private TreeViewer treeViewer;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite filterComposite = new Composite(parent, SWT.NONE);
		filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(filterComposite, SWT.NONE);
		label.setBounds(334, 5, 94, 17);
		label.setText("第1页/共4页");
		
		Button btnNewButton = new Button(filterComposite, SWT.NONE);
		btnNewButton.setBounds(145, 0, 80, 27);
		btnNewButton.setText("上一页");
		
		Button btnNewButton_1 = new Button(filterComposite, SWT.NONE);
		btnNewButton_1.setBounds(248, 0, 80, 27);
		btnNewButton_1.setText("下一页");
		
		Label label_1 = new Label(filterComposite, SWT.NONE);
		label_1.setBounds(10, 5, 61, 17);
		label_1.setText("每页显示：");
		
		Combo combo = new Combo(filterComposite, SWT.NONE);
		combo.setBounds(77, 0, 51, 25);
		combo.setText("100");
		
		Composite treeComposite = new Composite(parent, SWT.NONE);
		treeComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_treeComposite = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_treeComposite.heightHint = 119;
		treeComposite.setLayoutData(gd_treeComposite);
		
		treeViewer = new TreeViewer(treeComposite, SWT.BORDER);
		Tree tree = treeViewer.getTree();

		menuMng = new MenuManager();
		menuMng.setRemoveAllWhenShown(true);
	}

	private class MenuListener implements IMenuListener {
		private TreeViewer treeViewer;

		public MenuListener(TreeViewer treeViewer) {
			this.treeViewer = treeViewer;
		}

		@Override
		public void menuAboutToShow(IMenuManager manager) {
			IStructuredSelection selection = (IStructuredSelection) treeViewer
					.getSelection();
			if (!selection.isEmpty()) {
				createContextMenu(selection.getFirstElement());
			}
		}

		/**
		 * 右键菜单内容
		 * 
		 * @param selectedObject
		 */
		private void createContextMenu(final Object selectedObject) {

		}

	}
	
	private void edit(Object object) {
		
		
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
}