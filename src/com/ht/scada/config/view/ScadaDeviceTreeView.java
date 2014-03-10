package com.ht.scada.config.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.JFileChooser;

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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.ht.scada.common.tag.entity.AcquisitionChannel;
import com.ht.scada.common.tag.entity.AcquisitionDevice;
import com.ht.scada.common.tag.entity.SensorDevice;
import com.ht.scada.common.tag.entity.TagCfgTpl;
import com.ht.scada.common.tag.service.AcquisitionChannelService;
import com.ht.scada.common.tag.service.AcquisitionDeviceService;
import com.ht.scada.common.tag.service.SensorDeviceService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ViewPropertyChange;
import com.ht.scada.config.view.tree.ScadaDeviceTreeContentProvider;
import com.ht.scada.config.view.tree.ScadaDeviceTreeLabelProvider;

/**
 * 赵磊、王蓬
 * @author Administrator
 *
 */
public class ScadaDeviceTreeView extends ViewPart {
	
	//private static final Logger log = LoggerFactory.getLogger(ScadaDeviceTreeView.class);
	
	private AcquisitionChannelService acquisitionChannelService = (AcquisitionChannelService) Activator.getDefault()
			.getApplicationContext().getBean("acquisitionChannelService");
	private AcquisitionDeviceService acquisitionDeviceService = (AcquisitionDeviceService) Activator.getDefault()
			.getApplicationContext().getBean("acquisitionDeviceService");
	private SensorDeviceService sensorDeviceService = (SensorDeviceService) Activator.getDefault()
			.getApplicationContext().getBean("sensorDeviceService");
	
	public ScadaDeviceTreeView() {
	}

	public static final String ID = "com.ht.scada.config.view.ScadaDeviceTreeView";

	private MenuManager menuMng;
	public static TreeViewer treeViewer;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite filterComposite = new Composite(parent, SWT.BORDER);
		filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(filterComposite, SWT.NONE);
		label.setBounds(188, 5, 51, 17);
		label.setText("1 / 3 ");
		
		Button btnNewButton = new Button(filterComposite, SWT.NONE);
		btnNewButton.setBounds(67, 2, 51, 22);
		btnNewButton.setText("上一页");
		
		Button btnNewButton_1 = new Button(filterComposite, SWT.NONE);
		btnNewButton_1.setBounds(124, 2, 48, 22);
		btnNewButton_1.setText("下一页");
		
		Combo combo = new Combo(filterComposite, SWT.NONE);
		combo.setBounds(10, 2, 51, 20);
		combo.setText("100");
		
		Composite treeComposite = new Composite(parent, SWT.NONE);
		treeComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_treeComposite = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_treeComposite.heightHint = 119;
		treeComposite.setLayoutData(gd_treeComposite);
		
		treeViewer = new TreeViewer(treeComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		treeViewer.setAutoExpandLevel(3);
		
		treeViewer.setContentProvider(new ScadaDeviceTreeContentProvider());
		treeViewer.setLabelProvider(new ScadaDeviceTreeLabelProvider());
		treeViewer.setInput("channel");

		Tree tree = treeViewer.getTree();
		menuMng = new MenuManager();
		menuMng.setRemoveAllWhenShown(true);
		
		menuMng.addMenuListener(new MenuListener(treeViewer));
		tree.setMenu(menuMng.createContextMenu(tree));
		
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
			} else if (selectedObject instanceof AcquisitionChannel) { // 采集通道
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
						if (MessageDialog.openConfirm(treeViewer.getTree()
								.getShell(), "删除", "确认要删除吗？")) {
							acquisitionChannelService.deleteById(acquisitionChannel
									.getId().intValue());
							treeViewer.remove(acquisitionChannel);
						}
					}
				};
				objectIndex.setText("删除采集通道(&D)");
				menuMng.add(objectIndex);
			}
			else if (selectedObject instanceof AcquisitionDevice) { // 采集设备
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
				
				//===============导出传感器模板(王蓬)====================
			    objectIndex = new Action() {
			    	public void run() {	
			    		//获得该设备下的所有传感器
						List<SensorDevice> sensorDeviceList = sensorDeviceService.getSensorByDeviceId(acquisitionDevice.getId());
						if(sensorDeviceList.size()==0 || sensorDeviceList==null) {	//传感器为空的设备不可导出模板
							MessageDialog.openError(treeViewer.getTree().getShell(), "错误", "该设备下无传感器可导出!");
							return;
						} else {													//存在传感器，导出模板
				    		File file = new File(".\\TagModels\\SensorModels");
							if ( !file.exists()){
								 file.mkdirs();
							} 
							JFileChooser chooser = new JFileChooser(file); 	//文件选择对话框 (打开TagModels 文件夹)
							chooser.setSelectedFile(new File("SensorModel.dic"));
							int returnVal = chooser.showOpenDialog(null);
							String fileName = "";
							if(returnVal == JFileChooser.APPROVE_OPTION) {
								System.out.println("You chose to open this file: " +
							    chooser.getSelectedFile().getName());
								fileName = chooser.getSelectedFile().getAbsolutePath();
							}
							
							// 导出变量模板  
							try {
								ObjectOutputStream out = new ObjectOutputStream( 
								     new FileOutputStream(fileName));
								
								out.writeObject(sensorDeviceList);		// 写文件
								out.flush();
								out.close();			
							} catch ( IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}	
							
						}

			    	}
			    };
			    objectIndex.setText("相关传感器模板导出！(&O)");
			    menuMng.add(objectIndex);
			    
			    //===============导入传感器模板======================
			    objectIndex = new Action() {
			    	public void run() {
			    		System.out.println("要进行传感器模板的导入！");
						// 从dic字典文件导入对象集合
						try {
							
							File file = new File(".");
							JFileChooser chooser = new JFileChooser(file); 	//文件选择对话框 (打开TagModels 文件夹)
							int returnVal = chooser.showOpenDialog(null);
							String fileName = "";
							if(returnVal == JFileChooser.APPROVE_OPTION) {
								System.out.println("You chose to open this file: " +
							    chooser.getSelectedFile().getName());
								fileName = chooser.getSelectedFile().getAbsolutePath();
							}

							ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)); 
							//获得文件中的传感器对象集合
							List<SensorDevice> sensorDeviceList = ( List<SensorDevice> ) in.readObject();	
							makeNewModelSensors( sensorDeviceList , acquisitionDevice );	//导入传感器对象集合
							
							in.close();
						} catch ( IOException | ClassNotFoundException e1 ) {  
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}		
			    	}
			    };
			    objectIndex.setText("相关传感器模板导入！(&I)");
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
						if (MessageDialog.openConfirm(treeViewer.getTree()
								.getShell(), "删除", "确认要删除吗？")) {
							acquisitionDeviceService.deleteById(acquisitionDevice
									.getId().intValue());

							treeViewer.remove(acquisitionDevice);
						}
					}
				};
				objectIndex.setText("删除设备(&D)");
				menuMng.add(objectIndex);
			}
			else if ( selectedObject instanceof SensorDevice ) {	
				final SensorDevice sensorDevice = (SensorDevice) selectedObject;
				//===============删除传感器设备（王蓬）===============
				Action objectIndex = new Action () {
					public void run() {
						if (MessageDialog.openConfirm(treeViewer.getTree()
								.getShell(), "删除", "确认要删除吗？")) {
							sensorDeviceService.deleteById(sensorDevice
									.getId().intValue());
							
							treeViewer.remove(sensorDevice);
						}
					}
				};
				objectIndex.setText("删除传感器(&D)");
				menuMng.add(objectIndex);
			}
			
		}

	}
	
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
	 * 将导入模板中的传感器在内存中构建出来，并加入到当前模板集合中
	 * @autor 王蓬
	 * @param tagCfgTplListImport
	 */
	private void makeNewModelSensors(List<SensorDevice> sensorDeviceList, AcquisitionDevice acquisitionDevice){
		for( int j=0; j<sensorDeviceList.size(); j++ ) {
			// 更新数据库
			SensorDevice sensorTemp = sensorDeviceList.get(j);
			SensorDevice sensorDevice = new SensorDevice();
			
			sensorDevice.setAddress(sensorTemp.getAddress());
			sensorDevice.setCheckInterval(sensorTemp.getCheckInterval());
			sensorDevice.setFixPositin(sensorTemp.getFixPositin());
			sensorDevice.setFixTime(sensorTemp.getFixTime());
			sensorDevice.setManufacture(sensorTemp.getManufacture());
			sensorDevice.setName(sensorTemp.getName());
			sensorDevice.setNickName(sensorTemp.getNickName());
			sensorDevice.setNumber(sensorTemp.getNumber());
			sensorDevice.setRemark(sensorTemp.getRemark());
			sensorDevice.setRtuDevice(acquisitionDevice);
			sensorDevice.setType(sensorTemp.getType());
			
			// 更新数据库
			sensorDeviceService.create(sensorDevice);
			
			// 更新左边的树状结构
			Object parentObject = acquisitionDevice; //关联选中的对象为父对象
			ScadaDeviceTreeView.treeViewer.add(parentObject, sensorDevice);
			ScadaDeviceTreeView.treeViewer.setExpandedState(parentObject, true);		
		}
	}
	
	
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
}