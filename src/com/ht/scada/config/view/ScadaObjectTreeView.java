package com.ht.scada.config.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
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

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.service.EndTagService;
import com.ht.scada.common.tag.service.MajorTagService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ViewPropertyChange;
import com.ht.scada.config.view.tree.MainTreeContentProvider;
import com.ht.scada.config.view.tree.MainTreeLabelProvider;
import com.ht.scada.config.view.tree.RootTreeModel;
import com.ht.scada.config.window.EndTagDeviceConfigWindow;
import com.ht.scada.config.window.EndTagIOConfigWindow;

public class ScadaObjectTreeView extends ViewPart {
	
	//private static final Logger log = LoggerFactory.getLogger(ScadaObjectTreeView.class);
	
	private MajorTagService majorTagService = (MajorTagService) Activator.getDefault()
			.getApplicationContext().getBean("majorTagService");
	private EndTagService endTagService = (EndTagService) Activator.getDefault()
			.getApplicationContext().getBean("endTagService");
	
	public ScadaObjectTreeView() {
	}

	public static final String ID = "com.ht.scada.config.view.ScadaObjectTreeView";
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
		treeViewer.setContentProvider(new MainTreeContentProvider());
		treeViewer.setLabelProvider(new MainTreeLabelProvider());
		treeViewer.setInput(RootTreeModel.instanse);
		

		Tree tree = treeViewer.getTree();

		menuMng = new MenuManager();
		menuMng.setRemoveAllWhenShown(true);
		menuMng.addMenuListener(new MenuListener(treeViewer));
		tree.setMenu(menuMng.createContextMenu(tree)); // 添加菜单

		// 点击打开编辑页面
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
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

				if (str.equals(RootTreeModel.instanse.labelIndex)) {		// 监控对象变量
					Action objectIndex = new Action() {
						public void run() {
							try {
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow()
										.getActivePage()
										.showView(MainIndexView.ID);
							} catch (PartInitException e) {
								e.printStackTrace();
							}
							ViewPropertyChange.getInstance().firePropertyChangeListener(FirePropertyConstants.MAJOR_ADD, selectedObject);
							
						}
					};
					objectIndex.setText("添加索引(&A)");
					menuMng.add(objectIndex);
				} else if (str.equals(RootTreeModel.instanse.normalIndex)) {// 常规分类索引
					Action objectIndex = new Action() {
						public void run() {

						}
					};
					objectIndex.setText("添加索引(&A)");
					menuMng.add(objectIndex);
				} else if (str.equals(RootTreeModel.instanse.energyIndex)) {// 能耗分类分项
					Action objectIndex = new Action() {
						public void run() {

						}
					};
					objectIndex.setText("添加索引(&A)");
					menuMng.add(objectIndex);
				} else if (str.equals(RootTreeModel.instanse.otherConfig)) {// 其他
					Action objectIndex = new Action() {
						public void run() {

						}
					};
					objectIndex.setText("其他");
					menuMng.add(objectIndex);
				}
			} else if(selectedObject instanceof MajorTag) {
				final MajorTag majorTag = (MajorTag)selectedObject;
				
				Action objectIndex = new Action() {
					public void run() {
						try {
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow()
									.getActivePage()
									.showView(MainIndexView.ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						ViewPropertyChange.getInstance().firePropertyChangeListener(FirePropertyConstants.MAJOR_ADD, selectedObject);
//						System.out.println(ViewPropertyChange.getInstance().toString());
//						System.out.println(selectedObject);
					}
				};
				objectIndex.setText("添加索引1(&A)");
				menuMng.add(objectIndex);
				
				
				
				objectIndex = new Action() {
					public void run() {
						edit(selectedObject);
					}
				};
				objectIndex.setText("修改索引(&E)");
				menuMng.add(objectIndex);
				
				objectIndex = new Action() {
					public void run() {
						if (MessageDialog.openConfirm(treeViewer.getTree()
								.getShell(), "删除", "确认要删除吗？")) {
							majorTagService.deleteById(majorTag.getId().intValue());

							treeViewer.remove(majorTag);
						}
					}
				};
				objectIndex.setText("删除索引(&D)");
				menuMng.add(objectIndex);
				
				menuMng.add(new Separator());
				
				objectIndex = new Action() {
					public void run() {
						try {
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow()
									.getActivePage()
									.showView(EndTagView.ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						ViewPropertyChange.getInstance().firePropertyChangeListener(FirePropertyConstants.ENDTAG_ADD, selectedObject);
						
					}
				};
				objectIndex.setText("添加监控对象(&M)");
				menuMng.add(objectIndex);
				
				menuMng.add(new Separator());
				
				objectIndex = new Action() {
					public void run() {
						new EndTagDeviceConfigWindow(majorTag).open();
					}
				};
				objectIndex.setText("关联变量模板与设备(&G)");
				menuMng.add(objectIndex);
				
				objectIndex = new Action() {
					public void run() {
						MessageDialog.openInformation(treeViewer.getTree().getShell(), "提示", "暂未开发");
						return;
					}
				};
				objectIndex.setText("关联所有变量模板与设备(&S)");
				menuMng.add(objectIndex);
				
				menuMng.add(new Separator());
				
				objectIndex = new Action() {
					public void run() {
						new EndTagIOConfigWindow(majorTag).open();
					}
				};
				objectIndex.setText("监控对象IO设置(&J)");
				menuMng.add(objectIndex);
				
				objectIndex = new Action() {
					public void run() {
						MessageDialog.openInformation(treeViewer.getTree().getShell(), "提示", "暂未开发");
						return;
					}
				};
				objectIndex.setText("所有监控对象IO设置(&K)");
				menuMng.add(objectIndex);
				
			}  else if(selectedObject instanceof EndTag) {
				final EndTag endTag = (EndTag)selectedObject;

				Action objectIndex = new Action() {
					public void run() {
						try {
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow()
									.getActivePage()
									.showView(EndTagView.ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						ViewPropertyChange.getInstance().firePropertyChangeListener(FirePropertyConstants.ENDTAG_ADD, selectedObject);
					}
				};
				objectIndex.setText("添加监控对象(&M)");
				menuMng.add(objectIndex);

				objectIndex = new Action() {
					public void run() {
						edit(selectedObject);
					}
				};
				objectIndex.setText("修改监控对象(&E)");
				menuMng.add(objectIndex);
				
				objectIndex = new Action() {
					public void run() {
						if (MessageDialog.openConfirm(treeViewer.getTree()
								.getShell(), "删除", "确认要删除吗？")) {
							endTagService.deleteById(endTag.getId());

							treeViewer.remove(endTag);
						}
					}
				};
				objectIndex.setText("删除监控对象(&D)");
				menuMng.add(objectIndex);
				
				
				menuMng.add(new Separator());	// 分割线
				// 一下标签用于设置监控对象的组态图设计
				objectIndex = new Action() {
					public void run() {
						try {
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow()
									.getActivePage()
									.showView(EndTagConfigDesignView.ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						ViewPropertyChange.getInstance().firePropertyChangeListener(FirePropertyConstants.CONFIG_DESIGN, selectedObject);
					}
				};
				objectIndex.setText("组态模板设计(&C)");
				menuMng.add(objectIndex);
			}
		}

	}
	
	private void edit(Object object) {
		if(object instanceof MajorTag) {
			try {
				PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.showView(MainIndexView.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			ViewPropertyChange.getInstance().firePropertyChangeListener(FirePropertyConstants.MAJOR_EDIT, object);
		} else if(object instanceof EndTag) {
			try {
				PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.showView(EndTagView.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			ViewPropertyChange.getInstance().firePropertyChangeListener(FirePropertyConstants.ENDTAG_EDIT, object);
		}
		
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
}