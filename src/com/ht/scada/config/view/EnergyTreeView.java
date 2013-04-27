package com.ht.scada.config.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.EnergyMinorTag;
import com.ht.scada.common.tag.service.EnergyMinorTagService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ViewPropertyChange;
import com.ht.scada.config.view.tree.EnergyTreeContentProvider;
import com.ht.scada.config.view.tree.EnergyTreeLabelProvider;
import com.ht.scada.config.view.tree.RootTreeModel;

public class EnergyTreeView extends ViewPart {

	private static final Logger log = LoggerFactory
			.getLogger(EnergyTreeView.class);

	private EnergyMinorTagService energyMinorTagService = (EnergyMinorTagService) Activator.getDefault()
			.getApplicationContext().getBean("energyMinorTagService");

	public EnergyTreeView() {
	}

	public static final String ID = "com.ht.scada.config.view.EnergyTreeView";
	public static TreeViewer treeViewer;

	private MenuManager menuMng;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		treeViewer.setAutoExpandLevel(3);
		treeViewer.setContentProvider(new EnergyTreeContentProvider());
		treeViewer.setLabelProvider(new EnergyTreeLabelProvider());
		treeViewer.setInput("能耗分类分项");
		Tree tree = treeViewer.getTree();

		menuMng = new MenuManager();
		menuMng.setRemoveAllWhenShown(true);
		menuMng.addMenuListener(new MenuListener(treeViewer));
		tree.setMenu(menuMng.createContextMenu(tree)); // 添加菜单

		// 点击打开编辑页面
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) { // 右键

					IStructuredSelection sel = ((IStructuredSelection) treeViewer
							.getSelection());
					if (!sel.isEmpty()) {
						final Object obj = ((IStructuredSelection) treeViewer
								.getSelection()).getFirstElement();
						Display.getDefault().timerExec(
								Display.getDefault().getDoubleClickTime(),
								new Runnable() {
									public void run() {
										edit(obj);
									}
								});
					}

				}
			}

		});
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
			if (selectedObject instanceof String) {
				final String str = (String) selectedObject;

				if (str.equals(RootTreeModel.instanse.normalIndex)) {// 常规分类索引
					Action objectIndex = new Action() {
						public void run() {
							try {
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow()
										.getActivePage()
										.showView(EnergyTreeView.ID);
							} catch (PartInitException e) {
								e.printStackTrace();
							}
							ViewPropertyChange
									.getInstance()
									.firePropertyChangeListener(
											FirePropertyConstants.ENERGYMINOR_ADD,
											selectedObject);

						}
					};
					objectIndex.setText("添加索引(&A)");
					menuMng.add(objectIndex);
				}
			} else if (selectedObject instanceof EnergyMinorTag) {
				final EnergyMinorTag energyMinorTag = (EnergyMinorTag) selectedObject;

				// ===============添加索引=======================
				Action objectIndex = new Action() {
					public void run() {
						try {
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage()
									.showView(EnergyIndexView.ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						ViewPropertyChange.getInstance()
								.firePropertyChangeListener(
										FirePropertyConstants.ENERGYMINOR_ADD,
										selectedObject);

					}
				};
				objectIndex.setText("添加索引(&A)");
				menuMng.add(objectIndex);

				// ===============修改索引=======================
				objectIndex = new Action() {
					public void run() {
						try {
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage()
									.showView(EnergyIndexView.ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						ViewPropertyChange.getInstance()
								.firePropertyChangeListener(
										FirePropertyConstants.ENERGYMINOR_EDIT,
										selectedObject);

					}
				};
				objectIndex.setText("修改索引(&E)");
				menuMng.add(objectIndex);

				// ===============删除索引=======================
				objectIndex = new Action() {
					public void run() {
						if (MessageDialog.openConfirm(treeViewer.getTree()
								.getShell(), "删除", "确认要删除吗？")) {
							energyMinorTagService.deleteById(energyMinorTag
									.getId().intValue());

							treeViewer.remove(energyMinorTag);
						}
					}
				};
				objectIndex.setText("删除索引(&D)");
				menuMng.add(objectIndex);
			}
		}

	}

	private void edit(Object object) {
		if (object instanceof EnergyMinorTag) {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().showView(EnergyIndexView.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			ViewPropertyChange.getInstance().firePropertyChangeListener(
					FirePropertyConstants.ENERGYMINOR_EDIT, object);
		} else if (object instanceof EndTag) {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().showView(EndTagView.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			ViewPropertyChange.getInstance().firePropertyChangeListener(
					FirePropertyConstants.ENERGYMINOR_ADD, object);
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
}