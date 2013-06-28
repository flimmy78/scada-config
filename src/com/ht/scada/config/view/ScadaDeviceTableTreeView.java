package com.ht.scada.config.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.ht.scada.common.tag.entity.AcquisitionChannel;
import com.ht.scada.common.tag.entity.AcquisitionDevice;
import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.SensorDevice;
import com.ht.scada.common.tag.entity.VarIOInfo;
import com.ht.scada.common.tag.service.AcquisitionChannelService;
import com.ht.scada.common.tag.service.AcquisitionDeviceService;
import com.ht.scada.common.tag.service.EndTagService;
import com.ht.scada.common.tag.service.SensorDeviceService;
import com.ht.scada.common.tag.service.TagService;
import com.ht.scada.common.tag.service.VarIOInfoService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ImagePath;
import com.ht.scada.config.util.ViewPropertyChange;

import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.layout.GridLayout;

/**
 * 表格树形式实现监控设备管理
 * @author 王蓬
 *
 */
public class ScadaDeviceTableTreeView extends ViewPart {

	public static final String ID = "com.ht.scada.config.view.ScadaDeviceTableTreeView"; //$NON-NLS-1$

	public ScadaDeviceTableTreeView() {
	}

	private TagService tagService = (TagService)Activator.getDefault()
			.getApplicationContext().getBean("tagService");
	private VarIOInfoService varIOInfoService = (VarIOInfoService)Activator.getDefault()
			.getApplicationContext().getBean("varIOInfoService");
	
	private AcquisitionChannelService acquistionChannelService=(AcquisitionChannelService) Activator.getDefault()
			.getApplicationContext().getBean("acquisitionChannelService");
	
	private SensorDeviceService sensorDeviceService = (SensorDeviceService) Activator
			.getDefault().getApplicationContext()
			.getBean("sensorDeviceService");
	
	private AcquisitionDeviceService acquisitionDeviceService = (AcquisitionDeviceService) Activator
	.getDefault().getApplicationContext()
	.getBean("acquisitionDeviceService");
	
	//控制表格树的一级根节点目录
	private List<String> rootArrayList=new ArrayList();
	
	private MenuManager menuMng;


	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		Label tempLabel = new Label(container, SWT.NONE);
		tempLabel.setText("此行是否添加控件待定");
		
		//注意控件是GridTreeViewer 而不是 GridTableViewer
		final GridTreeViewer gridTreeViewer = new GridTreeViewer(container, SWT.BORDER| SWT.H_SCROLL | SWT.V_SCROLL);
	    final Grid grid = gridTreeViewer.getGrid();
		GridData gd_grid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grid.heightHint = 380;
		grid.setLayoutData(gd_grid);
		grid.setRowHeaderVisible(true);
		grid.setHeaderVisible(true);
		grid.setCellSelectionEnabled(true);
				
		GridViewerColumn gridViewerColumn = new GridViewerColumn(gridTreeViewer, SWT.NONE);
		GridColumn gridColumn = gridViewerColumn.getColumn();
		gridColumn.setTree(true);
		gridColumn.setWidth(150);
		gridColumn.setText("监控设备");
		
		GridViewerColumn gridViewerColumn_1 = new GridViewerColumn(gridTreeViewer, SWT.NONE);
		GridColumn gridColumn_1 = gridViewerColumn_1.getColumn();
		gridColumn_1.setWidth(60);
		gridColumn_1.setText("地址");
		
		GridViewerColumn gridViewerColumn_2 = new GridViewerColumn(gridTreeViewer, SWT.NONE);
		GridColumn gridColumn_2 = gridViewerColumn_2.getColumn();
		gridColumn_2.setWidth(60);
		gridColumn_2.setText("型号");
		
		GridViewerColumn gridViewerColumn_3 = new GridViewerColumn(gridTreeViewer, SWT.NONE);
		GridColumn gridColumn_3 = gridViewerColumn_3.getColumn();
		gridColumn_3.setWidth(75);
		gridColumn_3.setText("序号");
		
		GridViewerColumn gridViewerColumn_4 = new GridViewerColumn(gridTreeViewer, SWT.NONE);
		GridColumn gridColumn_4 = gridViewerColumn_4.getColumn();
		gridColumn_4.setWidth(75);
		gridColumn_4.setText("生产厂家");
		
		GridViewerColumn gridViewerColumn_5 = new GridViewerColumn(gridTreeViewer, SWT.NONE);
		GridColumn gridColumn_5 = gridViewerColumn_5.getColumn();
		gridColumn_5.setWidth(75);
		gridColumn_5.setText("安装日期");
			
		
		Composite composite = new Composite(container, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.widthHint = 517;
		gd_composite.heightHint = 44;
		composite.setLayoutData(gd_composite);
		
		Label label = new Label(composite, SWT.NONE);
		label.setBounds(231, 10, 276, 17);
		label.setText("添加保存按钮，左右两侧同步问题看看是否需要实现");
		
		//控制表格树数据
		rootArrayList.add("采集通道");
		gridTreeViewer.setContentProvider(new ScadaDeviceTreeContentProvider());
		gridTreeViewer.setLabelProvider(new ScadaDeviceTreeLabelProvider());
		gridTreeViewer.setInput(rootArrayList);
		
		createActions();
		initializeToolBar();
		initializeMenu();
		
		
		//进行菜单显示事件关联
		menuMng = new MenuManager();
		menuMng.setRemoveAllWhenShown(true);
		menuMng.addMenuListener(new MenuListener(gridTreeViewer));								//控制监听事件
		gridTreeViewer.getGrid().setMenu(menuMng.createContextMenu(gridTreeViewer.getGrid()));  //控制显示
		//单击相应选项，弹出编辑窗口
	    grid.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseDown(MouseEvent e) {
	    		if (e.button == 1) { // 右键
					IStructuredSelection sel = ((IStructuredSelection) gridTreeViewer
							.getSelection());
					if (!sel.isEmpty()) {
						final Object obj = ((IStructuredSelection) gridTreeViewer
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

	/**
	 * 编辑窗体选择
	 * @param object
	 */
	private void edit(Object object) {
		if (object instanceof AcquisitionChannel) {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().showView(ScadaChannelConfigView.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			ViewPropertyChange.getInstance().firePropertyChangeListener(
					FirePropertyConstants.ACQUISITIONCHANNEL_EDIT, object);
		} else if (object instanceof AcquisitionDevice) {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().showView(ScadaDeviceConfigView.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			ViewPropertyChange.getInstance().firePropertyChangeListener(
					FirePropertyConstants.ACQUISITIONDEVICE_EDIT, object);
		} else if (object instanceof SensorDevice) {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().showView(ScadaSensorConfigView.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			ViewPropertyChange.getInstance().firePropertyChangeListener(
					FirePropertyConstants.SENSORDEVICE_EDIT, object);
		}
		
	}
	
	
	
	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
	
	/**
	 * 表格树事件监听类
	 * @author hhh
	 *
	 */
	private class MenuListener implements IMenuListener {
		private GridTreeViewer gridTreeViewer;

		public MenuListener(GridTreeViewer gridTreeViewer) {
			this.gridTreeViewer = gridTreeViewer;
		}

		@Override
		public void menuAboutToShow(IMenuManager manager) {
			IStructuredSelection selection = (IStructuredSelection) gridTreeViewer
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

				if (str.equals("采集通道")) {// 采集通道
					Action objectIndex = new Action() {
						public void run() {
							try {
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow()
										.getActivePage()
										.showView(ScadaChannelConfigView.ID);
							} catch (PartInitException e) {
								e.printStackTrace();
							}
							ViewPropertyChange
									.getInstance()
									.firePropertyChangeListener(
											FirePropertyConstants.ACQUISITIONCHANNEL_ADD,
											selectedObject);

						}
					};
					objectIndex.setText("添加采集通道(&A)");
					menuMng.add(objectIndex);
				}
			} else if (selectedObject instanceof AcquisitionChannel) { 			// 采集通道
				final AcquisitionChannel acquisitionChannel = (AcquisitionChannel) selectedObject;

				// ===============添加设备=======================
				Action objectIndex = new Action() {
					public void run() {
						try {
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage()
									.showView(ScadaDeviceConfigView.ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						ViewPropertyChange.getInstance()
								.firePropertyChangeListener(
										FirePropertyConstants.ACQUISITIONDEVICE_ADD,
										selectedObject);
						System.out.println(((AcquisitionChannel) selectedObject).getName());

					}
				};
				objectIndex.setText("添加设备(&A)");
				menuMng.add(objectIndex);
				
				menuMng.add(new Separator());

				// ===============修改采集通道(E)=======================
				objectIndex = new Action() {
					public void run() {
						try {
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage()
									.showView(ScadaChannelConfigView.ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						ViewPropertyChange.getInstance()
								.firePropertyChangeListener(
										FirePropertyConstants.ACQUISITIONCHANNEL_EDIT,
										selectedObject);

					}
				};
				objectIndex.setText("修改采集通道(&E)");
				menuMng.add(objectIndex);

				// ===============删除采集通道(D)=======================
				objectIndex = new Action() {
					public void run() {
						if (MessageDialog.openConfirm(gridTreeViewer.getGrid()
								.getShell(), "删除", "确认要删除吗？")) {
							acquistionChannelService.deleteById(acquisitionChannel
									.getId().intValue());
							gridTreeViewer.remove(acquisitionChannel);
						}
					}
				};
				objectIndex.setText("删除采集通道(&D)");
				menuMng.add(objectIndex);
			}else if (selectedObject instanceof AcquisitionDevice) { 			// 采集设备
				final AcquisitionDevice acquisitionDevice = (AcquisitionDevice) selectedObject;

				// ===============添加传感器=======================
				Action objectIndex = new Action() {
					public void run() {
						try {
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage()
									.showView(ScadaSensorConfigView.ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						ViewPropertyChange.getInstance()
								.firePropertyChangeListener(
										FirePropertyConstants.SENSORDEVICE_ADD,
										selectedObject);
					}
				};
				objectIndex.setText("添加传感器(&A)");
				menuMng.add(objectIndex);
				
				menuMng.add(new Separator());

				// ===============修改设备(E)=======================
				objectIndex = new Action() {
					public void run() {
						try {
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage()
									.showView(ScadaChannelConfigView.ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						ViewPropertyChange.getInstance()
								.firePropertyChangeListener(
										FirePropertyConstants.ACQUISITIONDEVICE_EDIT,
										selectedObject);

					}
				};
				objectIndex.setText("修改设备(&E)");
				menuMng.add(objectIndex);

				// ===============删除设备(D)=======================
				objectIndex = new Action() {
					public void run() {
						if (MessageDialog.openConfirm(gridTreeViewer.getGrid()
								.getShell(), "删除", "确认要删除吗？")) {
							acquisitionDeviceService.deleteById(acquisitionDevice
									.getId().intValue());
							gridTreeViewer.remove(acquisitionDevice);
						}
					}
				};
				objectIndex.setText("删除设备(&D)");
				menuMng.add(objectIndex);
			}
		}

	}
	
	/**
	 * 通道配置内容提供器（表格树） 
	 * @author 王蓬
	 */
	public class ScadaDeviceTreeContentProvider implements ITreeContentProvider {
		
		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}
		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub

		}
		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof String) {
				// TODO
				String str = (String) parentElement;
				if (str.equals("采集通道")) { // 采集通道
					List<AcquisitionChannel> acquisitionChannelList = acquistionChannelService
							.getAllChannel();
					if (acquisitionChannelList != null) {
						return acquisitionChannelList.toArray();
					}
				}
			} else if (parentElement instanceof AcquisitionChannel) {
				List<Object> objectList = new ArrayList<Object>();
				List<AcquisitionDevice> acquisitionDeviceList = acquisitionDeviceService
						.getDeviceByChannelId(((AcquisitionChannel) parentElement)
								.getId());
				if (acquisitionDeviceList != null) {
					objectList.addAll(acquisitionDeviceList);
				}
				if (!objectList.isEmpty()) {
					return objectList.toArray();
				}
			} else if (parentElement instanceof AcquisitionDevice) {
				List<Object> objectList = new ArrayList<Object>();
				List<SensorDevice> sensorDeviceList = sensorDeviceService
						.getSensorByDeviceId(((AcquisitionDevice) parentElement)
								.getId());
				if (sensorDeviceList != null) {
					objectList.addAll(sensorDeviceList);
				}
				if (!objectList.isEmpty()) {
					return objectList.toArray();
				}
			}
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			List<String> list = (List<String>) inputElement;
			return list.toArray();

		}

		@Override
		public Object getParent(Object arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasChildren(Object arg0) {
						
			Object[] children = this.getChildren(arg0);
			if (children == null) {
				return false;
			} else if (children.length > 0) {
				return true;
			} else {
				return false;
			}
		}

	}
	
	
	/**
	 * 通道配置标签提供器（表格树）
	 * @author 王蓬
	 */
	public class ScadaDeviceTreeLabelProvider implements  ITableLabelProvider {

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			if(columnIndex == 0){				//仅仅第一列相应对象变换图标
				if(element instanceof String) {
					return Activator.getDefault().getImageDescriptor(ImagePath.ACQUISITION_CHANNEL_INDEX_IMAGE).createImage();
				}else if(element instanceof AcquisitionChannel) {
					return Activator.getDefault().getImageDescriptor(ImagePath.ACQUISITION_CHANNEL_IMAGE).createImage();
				} else if(element instanceof AcquisitionDevice) {
					return Activator.getDefault().getImageDescriptor(ImagePath.ACQUISITION_DEVICE_IMAGE).createImage();
				} else if(element instanceof String) {
					return Activator.getDefault().getImageDescriptor(ImagePath.ACQUISITION_CHANNEL_INDEX_IMAGE).createImage();
				}
			}else{
				//doNothing
			}

			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			if(element instanceof String) {
				String varTmp = (String)element;
				switch (columnIndex) {
				case 0:
					return varTmp;
				case 1:
					return null;
				case 2:
					return null;
				case 3:
					return null;
				case 4:
					return null;
				case 5:
					return null;
				
				default:
					break;
				}
			}else if(element instanceof AcquisitionChannel) {
				AcquisitionChannel var = (AcquisitionChannel)element;
				switch (columnIndex) {
				case 0:
					return var.getName() + ":"+ var.getIdx();
				case 1:
					return null;
				case 2:
					return null;
				case 3:
					return var.getIdx()+"";
				case 4:
					return null;
				case 5:
					return null;
				
				default:
					break;
				}
			}  else if(element instanceof AcquisitionDevice) {
				AcquisitionDevice var = (AcquisitionDevice)element;
				switch (columnIndex) {
				case 0:
					return var.getName() + ":" + var.getAddress();
				case 1:
					return var.getAddress()+"";
				case 2:
					return var.getType();
				case 3:
					return var.getNumber();
				case 4:
					return var.getManufacture();
				case 5:{
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");   
					Date time=var.getFixTime();
					if(time==null){
						return "";
					}else{
						return format.format(time);
					}
				}
				default:
					break;
				}
			}  else if(element instanceof SensorDevice) {
				SensorDevice var = (SensorDevice)element;
				switch (columnIndex) {
				case 0:
					return var.getName();
				case 1:
					return var.getAddress()+ "";
				case 2:
					return var.getType();
				case 3:
					return var.getNumber();
				case 4:
					return var.getManufacture();
				case 5:{
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");   
					Date time=var.getFixTime();
					if(time==null){
						return "";
					}else{
						return format.format(time);
					}
				}
				default:
					break;
				}
			}  
			
			return null;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

	}
}
