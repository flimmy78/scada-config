package com.ht.scada.config.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.internal.CellSelection;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.scada.common.tag.dao.TplModelConfigDao;
import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.TagCfgTpl;
import com.ht.scada.common.tag.entity.TplModelConfig;
import com.ht.scada.common.tag.entity.VarGroupCfg;
import com.ht.scada.common.tag.service.EndTagConfigService;
import com.ht.scada.common.tag.service.TagCfgTplService;
import com.ht.scada.common.tag.service.TplModelConfigService;
import com.ht.scada.common.tag.type.entity.DataValueType;
import com.ht.scada.common.tag.type.entity.VarSubType;
import com.ht.scada.common.tag.type.entity.VarType;
import com.ht.scada.common.tag.type.service.TypeService;
import com.ht.scada.config.dialog.FloatModifySettingsDialog;
import com.ht.scada.config.dialog.FloatModifyTypeModel;
import com.ht.scada.config.dialog.StringModifySettingsDialog;
import com.ht.scada.config.dialog.StringModifyTypeModel;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.GridViewerColumnSorter;
import com.ht.scada.config.util.PinyinComparator;
import com.ht.scada.config.window.StorageDetailInfor;
import com.ht.scada.config.window.TplModelConfigDesignWindow;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

/**
 * 变量模板配置
 * 
 * @author 赵磊,王蓬
 * 
 */
public class VariableTemplateConfigView extends ViewPart {
	public static final String ID = "com.ht.scada.config.view.VariableTemplateConfigView";
	
	private static final Logger log = LoggerFactory
			.getLogger(VariableTemplateConfigView.class);
	
	private TagCfgTplService tagCfgTplService = (TagCfgTplService) Activator
			.getDefault().getApplicationContext().getBean("tagCfgTplService");
	private final TypeService typeService = (TypeService) Activator.getDefault()
			.getApplicationContext().getBean("typeService");
	private TplModelConfigService tplModelConfigService = (TplModelConfigService) Activator
			.getDefault().getApplicationContext().getBean("tplModelConfigService");			// 服务对象

	private MenuManager menuMng;

	private Text text_tpl_name;
	private ListViewer listViewer_1;
	private List<String> tplNameList; // 所有变量模板名字
	private List<TagCfgTpl> tagCfgTplList = new ArrayList<>(); // 当前模板所有变量
	private GridTableViewer gridTableViewer;
	private Grid grid;
	private List<TagCfgTpl> deletedTplList = new ArrayList<>(); // 要删除的变量模板
	private List<TagCfgTpl> tagCfgTplListSelect = new ArrayList<>(); // 筛选后的模板集合
	
	private List<VarType> varTypeList;
	private List<VarSubType> varSubTypeList;
	private List<VarGroupCfg> varGroupCfgList;
	private List<DataValueType> dataTypeList;
	private List<VarSubType> allSubTypeList;
	
	private String selectedTplName = null; // 选定的变量模板名字
	private String addedTplName = null;			 // 新增的变量模板名字
	private String[] varTypeArray = new String[]{""}; // 变量类型
	private String[] varSubTypeArray = new String[]{""};	//变量子类型
	private String[] varDataTypeArray = new String[]{""};	//值类型	
	private String[] varGroupCfgArray = new String[]{""};	//变量分组
	private String[] varUnitArray = {"", "m", "cm", "KPa", "MPa", "KW", "KW·h", "KVA", "KVar", "V", "A", "Hz", "m³", "m³/h", "℃"};// 变量单位数组
	
	private Combo combo;
	private Combo combo_1;
	private Button btnConfigDesign ;

	public VariableTemplateConfigView() {
		tplNameList = tagCfgTplService.findAllTplName();
		//类型
		varTypeList = typeService.getAllVarType();
		if(varTypeList != null && !varTypeList.isEmpty()) {
			int length = varTypeList.size();
			varTypeArray = new String[length + 1];
			varTypeArray[0] = "全部";
			for (int i = 1; i <= length; i++) {
				varTypeArray[i] = varTypeList.get(i - 1).getValue();
			}
		}
		
		//子类型
		varSubTypeList = typeService.getAllVarSubType();
		allSubTypeList = typeService.getAllVarSubType();
		if(varSubTypeList != null && !varSubTypeList.isEmpty()) {
			int len = varSubTypeList.size();
			varSubTypeArray = new String[len + 1];
			varSubTypeArray[0] = "";
			for (int i = 1; i <= len; i++) {
				varSubTypeArray[i] = varSubTypeList.get(i - 1).getValue();
			}
		}
		//分组
		varGroupCfgList = typeService.getAllVarGroupCfg();
		if(varGroupCfgList != null && !varGroupCfgList.isEmpty()) {
			int length = varGroupCfgList.size();
			varGroupCfgArray = new String[length + 1];
			varGroupCfgArray[0] = "全部";
			for (int i = 1; i <= length; i++) {
				varGroupCfgArray[i] = varGroupCfgList.get(i - 1).getValue();
			}
		}
		
		//值类型
		dataTypeList = typeService.getAllDataType();
		if(dataTypeList != null && !dataTypeList.isEmpty()) {
			int len1 = dataTypeList.size();
			varDataTypeArray = new String[len1 + 1];
			varDataTypeArray[0] = "";
			for (int i = 1; i <= len1; i++) {
				varDataTypeArray[i] = dataTypeList.get(i - 1).getValue();
			}
		}
		

	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(parent, SWT.BORDER | SWT.SMOOTH);
		sashForm.setSashWidth(1);

		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		menuMng = new MenuManager();
		menuMng.setRemoveAllWhenShown(true);

		Group group = new Group(composite, SWT.NONE);
		group.setText("变量模板");
		group.setLayout(new FillLayout(SWT.HORIZONTAL));

		listViewer_1 = new ListViewer(group, SWT.BORDER | SWT.V_SCROLL
				| SWT.MULTI);
		listViewer_1
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();
						if (!selection.isEmpty()) {
							String tplName = (String) selection
									.getFirstElement();
							selectedTplName = tplName;
							addedTplName = null;
							initTplInfo(tplName);		//进行名称控件设置及获得全部的变量
							deletedTplList.clear();
						
							//点击模板时，初始化变量类型及变量分组
							combo.setItems(varTypeArray);
							combo.select(0);
							
							combo_1.setItems(varGroupCfgArray);
							combo_1.select(0);
						}

					}
				});
		org.eclipse.swt.widgets.List list = listViewer_1.getList();
		menuMng.addMenuListener(new MenuListener(listViewer_1));

		list.setMenu(menuMng.createContextMenu(list)); // 添加菜单
		listViewer_1.setLabelProvider(new ViewerLabelProvider());

		listViewer_1.setContentProvider(ArrayContentProvider.getInstance());
		listViewer_1.setInput(tplNameList);

		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));

		Group group_2 = new Group(composite_1, SWT.NONE);
		group_2.setLayout(new GridLayout(12, false));
		group_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		group_2.setText("模板信息");
		group_2.setBounds(0, 0, 70, 84);

		Label label = new Label(group_2, SWT.NONE);
		label.setText("模板名：");

		text_tpl_name = new Text(group_2, SWT.BORDER);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		
		btnConfigDesign = new Button(group_2, SWT.NONE);
		btnConfigDesign.setEnabled(false);
		btnConfigDesign.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String tplName = text_tpl_name.getText().trim();
				
				TplModelConfigDesignWindow tmcd = new TplModelConfigDesignWindow(tplName);
				tmcd.open();
				
			}
		});
		btnConfigDesign.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnConfigDesign.setText("组 态 图 设 计");

		Group group_1 = new Group(composite_1, SWT.NONE);
		group_1.setLayout(new GridLayout(1, false));
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		group_1.setText("变量信息");

		Composite composite_3 = new Composite(group_1, SWT.NONE);
		composite_3.setLayout(new GridLayout(6, false));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
								
										Label lblNewLabel = new Label(composite_3, SWT.NONE);
										lblNewLabel.setText("变量类型：");
						
								combo = new Combo(composite_3, SWT.NONE);
								combo.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent e) {									
										tagSearch();	//根据条件筛选变量
									}
								});
				
						Label label_1 = new Label(composite_3, SWT.NONE);
						label_1.setText("变量分组：");
		
				combo_1 = new Combo(composite_3, SWT.NONE);
				combo_1.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						tagSearch();	//根据条件筛选变量
					}
				});

		Button btnNewButton_1 = new Button(composite_3, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	 
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
					
					if (fileName == null || fileName.equals("") ) {
						MessageDialog.openWarning(grid.getShell(), "提示", "请选择要导入的模板文件!");
						return;
					}
					
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)); 
					List<TagCfgTpl> tagCfgTplListImport = ( List<TagCfgTpl> ) in.readObject();
					makeNewModelTags(tagCfgTplListImport);			//从内存中新申请部分地址存放导入的变量模板
	
					gridTableViewer.setInput(tagCfgTplList);
					gridTableViewer.refresh();

					in.close();
				} catch ( IOException | ClassNotFoundException e1) {  
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}		
			}
		});
		btnNewButton_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnNewButton_1.setText("  导入变量词典  ");

		Button button_2 = new Button(composite_3, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {			
				
				if (grid.getItems()==null || grid.getItems().length==0) {
					MessageDialog.openWarning(grid.getShell(), "提示", "不能导出空模板!");
					return;
				}
				
				File file = new File(".\\TagModels");
				if ( !file.exists()){
					 file.mkdirs();
				} 
				JFileChooser chooser = new JFileChooser(file); 	//文件选择对话框 (打开TagModels 文件夹)
				chooser.setSelectedFile(new File("TagModel.dic"));
				int returnVal = chooser.showOpenDialog(null);
				String fileName = "";
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("You chose to open this file: " +
				    chooser.getSelectedFile().getName());
					fileName = chooser.getSelectedFile().getAbsolutePath();
				}
				
				if (fileName == null || fileName.equals("") ) {
					MessageDialog.openWarning(grid.getShell(), "提示", "请选择模板要导出至的文件!");
					return;
				}
				
				// 导出变量模板  
				List<TagCfgTpl> tagCfgTplListExport = new ArrayList<TagCfgTpl>();				
				try {
					ObjectOutputStream out = new ObjectOutputStream( 
					     new FileOutputStream(fileName));
					GridItem [] giArray = grid.getItems();		// 从gird获得所有变量模板
					for (int i = 0 ; i < giArray.length ; i++ ) {					
						tagCfgTplListExport.add( (TagCfgTpl)giArray[i].getData() );
					}
					out.writeObject(tagCfgTplListExport);		// 写文件

					out.flush();
					out.close();
			
				} catch ( IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
			}
		});
		button_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		button_2.setText("导出变量词典 ");

		Composite composite_4 = new Composite(group_1, SWT.NONE);
		composite_4.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		gridTableViewer = new GridTableViewer(composite_4, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);

		grid = gridTableViewer.getGrid();
		grid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
		
//				sdi.setLocationX(e.x);
//				sdi.setLocationY(e.y);
			}
		});
		grid.setHeaderVisible(true);
		grid.setColumnScrolling(true);
		grid.setCellSelectionEnabled(true);
		grid.setRowHeaderVisible(true);

		GridViewerColumn gridViewerColumn = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		gridViewerColumn.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return tct.getTagName();
			}

			protected void setValue(Object element, Object value) {
				if("".equals((String) value)) {
					MessageDialog.openError(grid.getShell(), "错误", "变量名不能为空！");
					return;
				}
				TagCfgTpl tct = (TagCfgTpl) element;
				tct.setTagName((String) value);
				gridTableViewer.update(tct, null);
			}
		});
		GridColumn gridColumn = gridViewerColumn.getColumn();
		gridColumn.setText("变量名");
		gridColumn.setWidth(70);
		new GridViewerColumnSorter(gridViewerColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TagCfgTpl v1 = (TagCfgTpl) e1;
				TagCfgTpl v2 = (TagCfgTpl) e2;

				return PinyinComparator.INSTANCE.compare(v1.getTagName(),
						v2.getTagName());
			}
		};

		
		GridViewerColumn gridViewerColumn_19 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		gridViewerColumn_19.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return tct.getTagNameShow();
			}

			protected void setValue(Object element, Object value) {
//				if("".equals((String) value)) {
//					MessageDialog.openError(grid.getShell(), "错误", "设计显示名不能为空！");
//					return;
//				}
				TagCfgTpl tct = (TagCfgTpl) element;
				tct.setTagNameShow((String) value);
				gridTableViewer.update(tct, null);
			}
		});
		GridColumn gridColumn_19 = gridViewerColumn_19.getColumn();
		gridColumn_19.setText("设计显示名");
		gridColumn_19.setWidth(70);
		
		GridViewerColumn gridViererColumn_6 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_6 = gridViererColumn_6.getColumn();
		gridColumn_6.setText("变量类型");
		gridColumn_6.setWidth(60);
		gridViererColumn_6
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					protected boolean canEdit(Object element) {
						return true;
					}

					protected CellEditor getCellEditor(Object element) {
						CellEditor ce = new ComboBoxCellEditor(grid,
								varTypeArray);
						return ce;
					}

					protected Object getValue(Object element) {
						TagCfgTpl tagCfgTpl = (TagCfgTpl) element;
						if (tagCfgTpl.getVarType() != null) {
							int i = 1;
							for (VarType varType : varTypeList) {
								if (varType.getName().equals(tagCfgTpl.getVarType())) {
									return i;
								}
								i++;
							}
						}
						return 0;
					}

					protected void setValue(Object element, Object value) {
						TagCfgTpl tagCfgTpl = (TagCfgTpl) element;
						int index = (int)value;
						if (index<=0) {
							tagCfgTpl.setVarType(null);
							//设置子类型为空
							varSubTypeArray = new String[]{""};
							varSubTypeList.clear();
							
						} else {
							tagCfgTpl.setVarType(varTypeList.get(index-1).getName());
							
							//重新初始化子类型
							varSubTypeList = typeService.getVarSubTypeByVarTypeName(tagCfgTpl.getVarType());
							if(varSubTypeList != null && !varSubTypeList.isEmpty()) {
								int len = varSubTypeList.size();
								varSubTypeArray = new String[len + 1];
								varSubTypeArray[0] = "";
								for (int i = 1; i <= len; i++) {
									varSubTypeArray[i] = varSubTypeList.get(i - 1).getValue();
								}
							}
							
						}

						gridTableViewer.update(tagCfgTpl, null);
					}
				});

		GridViewerColumn gridViererColumn_1 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_1 = gridViererColumn_1.getColumn();
		gridColumn_1.setText("子类型");
		gridColumn_1.setWidth(70);
		gridViererColumn_1
		.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new ComboBoxCellEditor(grid,
						varSubTypeArray);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tagCfgTpl = (TagCfgTpl) element;
				if (tagCfgTpl.getSubType() != null) {
					int i = 1;
					for (VarSubType varSubType : varSubTypeList) {
						if (varSubType.getName().equals(tagCfgTpl.getSubType())) {
							return i;
						}
						i++;
					}
				}
				return 0;
			}

			protected void setValue(Object element, Object value) {
				TagCfgTpl tagCfgTpl = (TagCfgTpl) element;
				int index = (int)value;
				if (index<=0) {
					tagCfgTpl.setSubType(null);
					//设置变量分组为空
					tagCfgTpl.setVarGroup(null);
					//设置变量名为空
					tagCfgTpl.setVarName(null);
				} else {
					VarSubType varSubType = varSubTypeList.get(index-1);
					tagCfgTpl.setSubType(varSubType.getName());
					//自动设置变量分组
					tagCfgTpl.setVarGroup(varSubType.getVarGroupCfg().getName());
					//设置变量名
					tagCfgTpl.setVarName(varSubType.getName().toLowerCase());
					// TODO 非唯一的需要处理
				}

				gridTableViewer.update(tagCfgTpl, null);
			}
		});

		GridViewerColumn gridViererColumn_2 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_2 = gridViererColumn_2.getColumn();
		gridColumn_2.setText("变量分组");
		gridColumn_2.setWidth(70);
		gridViererColumn_2
		.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new ComboBoxCellEditor(grid,
						varGroupCfgArray);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tagCfgTpl = (TagCfgTpl) element;
				if (tagCfgTpl.getVarGroup() != null) {
					int i = 1;
					for (VarGroupCfg varGroupCfg : varGroupCfgList) {
						if (varGroupCfg.getName().equals(tagCfgTpl.getVarGroup())) {
							return i;
						}
						i++;
					}
				}
				return 0;
			}

			protected void setValue(Object element, Object value) {
				TagCfgTpl tagCfgTpl = (TagCfgTpl) element;
				int index = (int)value;
				if (index<=0) {
					tagCfgTpl.setVarGroup(null);
					
				} else {
					tagCfgTpl.setVarGroup(varGroupCfgList.get(index-1).getName());
					
					//重新初始化子类型
//					varSubTypeList = typeService.getVarSubTypeByVarTypeName(tagCfgTpl.getVarType());
//					if(varSubTypeList != null && !varSubTypeList.isEmpty()) {
//						int len = varSubTypeList.size();
//						varSubTypeArray = new String[len + 1];
//						varSubTypeArray[0] = "";
//						for (int i = 1; i <= len; i++) {
//							varSubTypeArray[i] = varSubTypeList.get(i - 1).getValue();
//						}
//					}
					
				}

				gridTableViewer.update(tagCfgTpl, null);
			}
		});

		GridViewerColumn gridViererColumn_3 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_3 = gridViererColumn_3.getColumn();
		gridColumn_3.setText("变量标志");
		gridColumn_3.setWidth(70);
		gridViererColumn_3.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return tct.getVarName()==null?"":tct.getVarName();
			}

			protected void setValue(Object element, Object value) {
				if("".equals((String) value)) {
					MessageDialog.openError(grid.getShell(), "错误", "变量标志不能为空！");
					return;
				}
				TagCfgTpl tct = (TagCfgTpl) element;
				tct.setVarName((String) value);
				gridTableViewer.update(tct, null);
			}
		});

		GridViewerColumn gridViewerColumn_5 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_5 = gridViewerColumn_5.getColumn();
		gridColumn_5.setText("功能码");
		gridColumn_5.setWidth(50);
		gridViewerColumn_5.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return String.valueOf(tct.getFunCode());
			}

			protected void setValue(Object element, Object value) {
				CellSelection cellSel = (CellSelection) gridTableViewer
						.getSelection();
				@SuppressWarnings("unchecked")
				List<TagCfgTpl> tagTplList = cellSel.toList();
				
				if (tagTplList.size() > 1) {
					StringModifyTypeModel im = new StringModifyTypeModel();// 值修改方式模型
					String channelIndex = (String)value;
					im.setBase(channelIndex);
					StringModifySettingsDialog dlg = new StringModifySettingsDialog(
							grid.getShell(), im, tagTplList.size());
					
					if (dlg.open() == Window.OK) {
						channelIndex = im.getBase();
						if(im.isFanwei()) {//按范围
							int start = im.getStart();
							int end = im.getEnd();
							for(;start<=end;start ++) {
								TagCfgTpl tagCfgTpl=(TagCfgTpl)gridTableViewer.getElementAt(start-1);
								tagCfgTpl.setFunCode(channelIndex.equals("")?null:Integer.valueOf(channelIndex));
								
								int ci;
								try {
									ci = Integer.parseInt(channelIndex);
								} catch (NumberFormatException e) {
									MessageDialog.openError(grid.getShell(), "错误", "非数字字符串不能计算");
									e.printStackTrace();
									return;
								}
								
								if (im.getType() > 0) {
									channelIndex = String.valueOf(ci + im.getInterval());
								} else if (im.getType() < 0) {
									channelIndex = String.valueOf(ci - im.getInterval());
								}
							}
							gridTableViewer.update(tagTplList.toArray(), null);
							return;
						}
						for(TagCfgTpl tagCfgTpl : tagTplList) {
							tagCfgTpl.setFunCode(channelIndex.equals("")?null:Integer.valueOf(channelIndex));
							
							int ci;
							try {
								ci = Integer.parseInt(channelIndex);
							} catch (NumberFormatException e) {
								MessageDialog.openError(grid.getShell(), "错误", "非数字字符串不能计算");
								e.printStackTrace();
								return;
							}
							if (im.getType() > 0) {
								channelIndex = String.valueOf(ci + im.getInterval());
							} else if (im.getType() < 0) {
								channelIndex = String.valueOf(ci - im.getInterval());
							}
						}
						gridTableViewer.update(tagTplList.toArray(), null);
					} else {
						return;
					}
				} else {// 选中一个单元格
					if("".equals((String) value)) {
						MessageDialog.openError(grid.getShell(), "错误", "功能码不能为空！");
						return;
					}
					int myValue;
					try {
						myValue = Integer.valueOf((String)value);
					} catch (NumberFormatException e) {
						MessageDialog.openError(grid.getShell(), "错误", "功能码应该为整形！");
						e.printStackTrace();
						return;
					}
					
					TagCfgTpl tct = (TagCfgTpl) element;
					tct.setFunCode(myValue);
					gridTableViewer.update(tct, null);
				}
			}
		});

		GridViewerColumn gridViewerColumn_7 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_7 = gridViewerColumn_7.getColumn();
		gridColumn_7.setText("数据地址");
		gridColumn_7.setWidth(60);
		gridViewerColumn_7.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return String.valueOf(tct.getDataId());
			}

			protected void setValue(Object element, Object value) {
				CellSelection cellSel = (CellSelection) gridTableViewer
						.getSelection();
				@SuppressWarnings("unchecked")
				List<TagCfgTpl> tagTplList = cellSel.toList();
				
				if (tagTplList.size() > 1) {
					StringModifyTypeModel im = new StringModifyTypeModel();// 值修改方式模型
					String channelIndex = (String)value;
					im.setBase(channelIndex);
					StringModifySettingsDialog dlg = new StringModifySettingsDialog(
							grid.getShell(), im, tagTplList.size());
					
					if (dlg.open() == Window.OK) {
						channelIndex = im.getBase();
						if(im.isFanwei()) {//按范围
							int start = im.getStart();
							int end = im.getEnd();
							for(;start<=end;start ++) {
								TagCfgTpl tagCfgTpl=(TagCfgTpl)gridTableViewer.getElementAt(start-1);
								tagCfgTpl.setDataId(channelIndex.equals("")?null:Integer.valueOf(channelIndex));
								
								int ci;
								try {
									ci = Integer.parseInt(channelIndex);
								} catch (NumberFormatException e) {
									MessageDialog.openError(grid.getShell(), "错误", "非数字字符串不能计算");
									e.printStackTrace();
									return;
								}
								
								if (im.getType() > 0) {
									channelIndex = String.valueOf(ci + im.getInterval());
								} else if (im.getType() < 0) {
									channelIndex = String.valueOf(ci - im.getInterval());
								}
							}
							gridTableViewer.update(tagTplList.toArray(), null);
							return;
						}
						for(TagCfgTpl tagCfgTpl : tagTplList) {
							tagCfgTpl.setDataId(channelIndex.equals("")?null:Integer.valueOf(channelIndex));
							
							int ci;
							try {
								ci = Integer.parseInt(channelIndex);
							} catch (NumberFormatException e) {
								MessageDialog.openError(grid.getShell(), "错误", "非数字字符串不能计算");
								e.printStackTrace();
								return;
							}
							if (im.getType() > 0) {
								channelIndex = String.valueOf(ci + im.getInterval());
							} else if (im.getType() < 0) {
								channelIndex = String.valueOf(ci - im.getInterval());
							}
						}
						gridTableViewer.update(tagTplList.toArray(), null);
					} else {
						return;
					}
				} else {// 选中一个单元格
					if("".equals((String) value)) {
						MessageDialog.openError(grid.getShell(), "错误", "数据地址不能为空！");
						return;
					}
					int myValue;
					try {
						myValue = Integer.valueOf((String)value);
					} catch (NumberFormatException e) {
						MessageDialog.openError(grid.getShell(), "错误", "数据地址应该为整形！");
						e.printStackTrace();
						return;
					}
					
					TagCfgTpl tct = (TagCfgTpl) element;
					tct.setDataId(myValue);
					gridTableViewer.update(tct, null);
				}
				
				
			}
		});

		GridViewerColumn gridViewerColumn_8 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_8 = gridViewerColumn_8.getColumn();
		gridColumn_8.setText("字节长度");
		gridColumn_8.setWidth(60);
		gridViewerColumn_8.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return String.valueOf(tct.getByteLen());
			}

			protected void setValue(Object element, Object value) {
				CellSelection cellSel = (CellSelection) gridTableViewer
						.getSelection();
				@SuppressWarnings("unchecked")
				List<TagCfgTpl> tagTplList = cellSel.toList();
				
				if (tagTplList.size() > 1) {
					StringModifyTypeModel im = new StringModifyTypeModel();// 值修改方式模型
					String channelIndex = (String)value;
					im.setBase(channelIndex);
					StringModifySettingsDialog dlg = new StringModifySettingsDialog(
							grid.getShell(), im, tagTplList.size());
					
					if (dlg.open() == Window.OK) {
						channelIndex = im.getBase();
						if(im.isFanwei()) {//按范围
							int start = im.getStart();
							int end = im.getEnd();
							for(;start<=end;start ++) {
								TagCfgTpl tagCfgTpl=(TagCfgTpl)gridTableViewer.getElementAt(start-1);
								tagCfgTpl.setByteLen(channelIndex.equals("")?null:Integer.valueOf(channelIndex));
								
								int ci;
								try {
									ci = Integer.parseInt(channelIndex);
								} catch (NumberFormatException e) {
									MessageDialog.openError(grid.getShell(), "错误", "非数字字符串不能计算");
									e.printStackTrace();
									return;
								}
								
								if (im.getType() > 0) {
									channelIndex = String.valueOf(ci + im.getInterval());
								} else if (im.getType() < 0) {
									channelIndex = String.valueOf(ci - im.getInterval());
								}
							}
							gridTableViewer.update(tagTplList.toArray(), null);
							return;
						}
						for(TagCfgTpl tagCfgTpl : tagTplList) {
							tagCfgTpl.setByteLen(channelIndex.equals("")?null:Integer.valueOf(channelIndex));
							
							int ci;
							try {
								ci = Integer.parseInt(channelIndex);
							} catch (NumberFormatException e) {
								MessageDialog.openError(grid.getShell(), "错误", "非数字字符串不能计算");
								e.printStackTrace();
								return;
							}
							if (im.getType() > 0) {
								channelIndex = String.valueOf(ci + im.getInterval());
							} else if (im.getType() < 0) {
								channelIndex = String.valueOf(ci - im.getInterval());
							}
						}
						gridTableViewer.update(tagTplList.toArray(), null);
					} else {
						return;
					}
				} else {// 选中一个单元格
					if("".equals((String) value)) {
						MessageDialog.openError(grid.getShell(), "错误", "字节长度不能为空！");
						return;
					}
					int myValue;
					try {
						myValue = Integer.valueOf((String)value);
					} catch (NumberFormatException e) {
						MessageDialog.openError(grid.getShell(), "错误", "字节长度应该为整形！");
						e.printStackTrace();
						return;
					}
					
					TagCfgTpl tct = (TagCfgTpl) element;
					tct.setByteLen(myValue);
					gridTableViewer.update(tct, null);
				}
				
				
				
			}
		});

		GridViewerColumn gridViewerColumn_9 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_9 = gridViewerColumn_9.getColumn();
		gridColumn_9.setText("字节偏移量");
		gridColumn_9.setWidth(75);
		gridViewerColumn_9.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return String.valueOf(tct.getByteOffset());
			}

			protected void setValue(Object element, Object value) {
				CellSelection cellSel = (CellSelection) gridTableViewer
						.getSelection();
				@SuppressWarnings("unchecked")
				List<TagCfgTpl> tagTplList = cellSel.toList();
				
				if (tagTplList.size() > 1) {
					StringModifyTypeModel im = new StringModifyTypeModel();// 值修改方式模型
					String channelIndex = (String)value;
					im.setBase(channelIndex);
					StringModifySettingsDialog dlg = new StringModifySettingsDialog(
							grid.getShell(), im, tagTplList.size());
					
					if (dlg.open() == Window.OK) {
						channelIndex = im.getBase();
						if(im.isFanwei()) {//按范围
							int start = im.getStart();
							int end = im.getEnd();
							for(;start<=end;start ++) {
								TagCfgTpl tagCfgTpl=(TagCfgTpl)gridTableViewer.getElementAt(start-1);
								tagCfgTpl.setByteOffset(channelIndex.equals("")?null:Integer.valueOf(channelIndex));
								
								int ci;
								try {
									ci = Integer.parseInt(channelIndex);
								} catch (NumberFormatException e) {
									MessageDialog.openError(grid.getShell(), "错误", "非数字字符串不能计算");
									e.printStackTrace();
									return;
								}
								
								if (im.getType() > 0) {
									channelIndex = String.valueOf(ci + im.getInterval());
								} else if (im.getType() < 0) {
									channelIndex = String.valueOf(ci - im.getInterval());
								}
							}
							gridTableViewer.update(tagTplList.toArray(), null);
							return;
						}
						for(TagCfgTpl tagCfgTpl : tagTplList) {
							tagCfgTpl.setByteOffset(channelIndex.equals("")?null:Integer.valueOf(channelIndex));
							
							int ci;
							try {
								ci = Integer.parseInt(channelIndex);
							} catch (NumberFormatException e) {
								MessageDialog.openError(grid.getShell(), "错误", "非数字字符串不能计算");
								e.printStackTrace();
								return;
							}
							if (im.getType() > 0) {
								channelIndex = String.valueOf(ci + im.getInterval());
							} else if (im.getType() < 0) {
								channelIndex = String.valueOf(ci - im.getInterval());
							}
						}
						gridTableViewer.update(tagTplList.toArray(), null);
					} else {
						return;
					}
				} else {// 选中一个单元格
					if("".equals((String) value)) {
						MessageDialog.openError(grid.getShell(), "错误", "字节偏移量不能为空！");
						return;
					}
					int myValue;
					try {
						myValue = Integer.valueOf((String)value);
					} catch (NumberFormatException e) {
						MessageDialog.openError(grid.getShell(), "错误", "字节偏移量应该为整形！");
						e.printStackTrace();
						return;
					}
					TagCfgTpl tct = (TagCfgTpl) element;
					tct.setByteOffset(myValue);
					gridTableViewer.update(tct, null);
				}
				
				
				
			}
		});

		GridViewerColumn gridViewerColumn_10 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_10 = gridViewerColumn_10.getColumn();
		gridColumn_10.setText("位偏移量");
		gridColumn_10.setWidth(70);
		gridViewerColumn_10.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return String.valueOf(tct.getBitOffset());
			}

			protected void setValue(Object element, Object value) {
				CellSelection cellSel = (CellSelection) gridTableViewer
						.getSelection();
				@SuppressWarnings("unchecked")
				List<TagCfgTpl> tagTplList = cellSel.toList();
				
				if (tagTplList.size() > 1) {
					StringModifyTypeModel im = new StringModifyTypeModel();// 值修改方式模型
					String channelIndex = (String)value;
					im.setBase(channelIndex);
					StringModifySettingsDialog dlg = new StringModifySettingsDialog(
							grid.getShell(), im, tagTplList.size());
					
					if (dlg.open() == Window.OK) {
						channelIndex = im.getBase();
						if(im.isFanwei()) {//按范围
							int start = im.getStart();
							int end = im.getEnd();
							for(;start<=end;start ++) {
								TagCfgTpl tagCfgTpl=(TagCfgTpl)gridTableViewer.getElementAt(start-1);
								tagCfgTpl.setBitOffset(channelIndex.equals("")?null:Integer.valueOf(channelIndex));
								
								int ci;
								try {
									ci = Integer.parseInt(channelIndex);
								} catch (NumberFormatException e) {
									MessageDialog.openError(grid.getShell(), "错误", "非数字字符串不能计算");
									e.printStackTrace();
									return;
								}
								
								if (im.getType() > 0) {
									channelIndex = String.valueOf(ci + im.getInterval());
								} else if (im.getType() < 0) {
									channelIndex = String.valueOf(ci - im.getInterval());
								}
							}
							gridTableViewer.update(tagTplList.toArray(), null);
							return;
						}
						for(TagCfgTpl tagCfgTpl : tagTplList) {
							tagCfgTpl.setBitOffset(channelIndex.equals("")?null:Integer.valueOf(channelIndex));
							
							int ci;
							try {
								ci = Integer.parseInt(channelIndex);
							} catch (NumberFormatException e) {
								MessageDialog.openError(grid.getShell(), "错误", "非数字字符串不能计算");
								e.printStackTrace();
								return;
							}
							if (im.getType() > 0) {
								channelIndex = String.valueOf(ci + im.getInterval());
							} else if (im.getType() < 0) {
								channelIndex = String.valueOf(ci - im.getInterval());
							}
						}
						gridTableViewer.update(tagTplList.toArray(), null);
					} else {
						return;
					}
				} else {// 选中一个单元格
					if("".equals((String) value)) {
						MessageDialog.openError(grid.getShell(), "错误", "位偏移量不能为空！");
						return;
					}
					int myValue;
					try {
						myValue = Integer.valueOf((String)value);
					} catch (NumberFormatException e) {
						MessageDialog.openError(grid.getShell(), "错误", "位偏移量应该为整形！");
						e.printStackTrace();
						return;
					}
					TagCfgTpl tct = (TagCfgTpl) element;
					tct.setBitOffset(myValue);
					gridTableViewer.update(tct, null);
				}
				
				
				
			}
		});

		GridViewerColumn gridViewerColumn_4 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_4 = gridViewerColumn_4.getColumn();
		gridColumn_4.setText("值类型");
		gridColumn_4.setWidth(70);
		gridViewerColumn_4
		.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new ComboBoxCellEditor(grid,
						varDataTypeArray);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tagCfgTpl = (TagCfgTpl) element;
				if (tagCfgTpl.getDataType() != null) {
					for (int i = 1; i < varDataTypeArray.length; i++) {
						if (tagCfgTpl.getDataType().equals(
								getDataType(varDataTypeArray[i]).getName())) {
							return i;
						}
					}
				}
				return 0;
			}

			protected void setValue(Object element, Object value) {
				TagCfgTpl tagCfgTpl = (TagCfgTpl) element;
				int index = (int)value;
				if (index<=0) {
					tagCfgTpl.setDataType(null);
				} else {
					tagCfgTpl.setDataType(getDataType(varDataTypeArray[index]).getName());
				}

				gridTableViewer.update(tagCfgTpl, null);
			}
		});

		GridViewerColumn gridViewerColumn_11 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_11 = gridViewerColumn_11.getColumn();
		gridColumn_11.setText("基数");
		gridColumn_11.setWidth(40);
		gridViewerColumn_11.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return tct.getBaseValue()==null?"":String.valueOf(tct.getBaseValue());
			}

			protected void setValue(Object element, Object value) {
				CellSelection cellSel = (CellSelection) gridTableViewer
						.getSelection();
				@SuppressWarnings("unchecked")
				List<TagCfgTpl> tagTplList = cellSel.toList();
				
				if (tagTplList.size() > 1) {
					FloatModifyTypeModel im = new FloatModifyTypeModel();// 值修改方式模型
					Float channelIndex = Float.valueOf((String)value);
					im.setBase(channelIndex);
					FloatModifySettingsDialog dlg = new FloatModifySettingsDialog(
							grid.getShell(), im, tagTplList.size());
					
					if (dlg.open() == Window.OK) {
						channelIndex = im.getBase();
						if(im.isFanwei()) {//按范围
							int start = im.getStart();
							int end = im.getEnd();
							for(;start<=end;start ++) {
								TagCfgTpl tagCfgTpl=(TagCfgTpl)gridTableViewer.getElementAt(start-1);
								tagCfgTpl.setBaseValue(channelIndex);
								
								
								if (im.getType() > 0) {
									channelIndex = channelIndex + im.getInterval();
								} else if (im.getType() < 0) {
									channelIndex = channelIndex - im.getInterval();
								}
							}
							gridTableViewer.update(tagTplList.toArray(), null);
							return;
						}
						for(TagCfgTpl tagCfgTpl : tagTplList) {
							tagCfgTpl.setBaseValue(channelIndex);
							
							
							if (im.getType() > 0) {
								channelIndex = channelIndex + im.getInterval();
							} else if (im.getType() < 0) {
								channelIndex = channelIndex - im.getInterval();
							}
						}
						gridTableViewer.update(tagTplList.toArray(), null);
					} else {
						return;
					}
				} else {// 选中一个单元格
					TagCfgTpl tct = (TagCfgTpl) element;
					String myValue = (String)value;
					tct.setBaseValue("".equals(myValue)?null:Float.valueOf(myValue));
					gridTableViewer.update(tct, null);
				}
			}
		});

		GridViewerColumn gridViewerColumn_12 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_12 = gridViewerColumn_12.getColumn();
		gridColumn_12.setText("系数");
		gridColumn_12.setWidth(40);
		gridViewerColumn_12.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return tct.getCoefValue()==null?"":String.valueOf(tct.getCoefValue());
			}

			protected void setValue(Object element, Object value) {
				CellSelection cellSel = (CellSelection) gridTableViewer
						.getSelection();
				@SuppressWarnings("unchecked")
				List<TagCfgTpl> tagTplList = cellSel.toList();
				
				if (tagTplList.size() > 1) {
					FloatModifyTypeModel im = new FloatModifyTypeModel();// 值修改方式模型
					Float channelIndex = Float.valueOf((String)value);
					im.setBase(channelIndex);
					FloatModifySettingsDialog dlg = new FloatModifySettingsDialog(
							grid.getShell(), im, tagTplList.size());
					
					if (dlg.open() == Window.OK) {
						channelIndex = im.getBase();
						if(im.isFanwei()) {//按范围
							int start = im.getStart();
							int end = im.getEnd();
							for(;start<=end;start ++) {
								TagCfgTpl tagCfgTpl=(TagCfgTpl)gridTableViewer.getElementAt(start-1);
								tagCfgTpl.setCoefValue(channelIndex);
								
								
								if (im.getType() > 0) {
									channelIndex = channelIndex + im.getInterval();
								} else if (im.getType() < 0) {
									channelIndex = channelIndex - im.getInterval();
								}
							}
							gridTableViewer.update(tagTplList.toArray(), null);
							return;
						}
						for(TagCfgTpl tagCfgTpl : tagTplList) {
							tagCfgTpl.setCoefValue(channelIndex);
							
							
							if (im.getType() > 0) {
								channelIndex = channelIndex + im.getInterval();
							} else if (im.getType() < 0) {
								channelIndex = channelIndex - im.getInterval();
							}
						}
						gridTableViewer.update(tagTplList.toArray(), null);
					} else {
						return;
					}
				} else {// 选中一个单元格
					TagCfgTpl tct = (TagCfgTpl) element;
					String myValue = (String)value;
					tct.setCoefValue("".equals(myValue)?null:Float.valueOf(myValue));
					gridTableViewer.update(tct, null);
				}
			}
		});

		// GridColumnGroup gridColumnGroup_2 = new GridColumnGroup(grid,
		// SWT.TOGGLE);
		// gridColumnGroup_2.setText("扩展信息");

		GridViewerColumn gridViewerColumn_13 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_13 = gridViewerColumn_13.getColumn();
		gridColumn_13.setText("存储规则");
		gridColumn_13.setWidth(65);
		gridViewerColumn_13.setEditingSupport(new EditingSupport(gridTableViewer) {
			StorageDetailInfor sdi;
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				
				sdi =new StorageDetailInfor();
				if (tct.getStorage() == null || tct.getStorage().length() == 0) {
					sdi.setStorageStr("fault|-|-|-|-");
				} else {
					sdi.setStorageStr(tct.getStorage()); 
				}
					
//				sdi =new StorageDetailInfor();
//				sdi.setStorageStr(tct.getStorage());
				sdi.open();
				return sdi.getStorageStr();
				//return tct.getStorage()==null?"":tct.getStorage();
			
			}

			protected void setValue(Object element, Object value) {
				TagCfgTpl tct = (TagCfgTpl) element;
				tct.setStorage("".equals((String)value)?null:(String)value);			
				gridTableViewer.update(tct, null);
			}
		});

		GridViewerColumn gridViewerColumn_14 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_14 = gridViewerColumn_14.getColumn();
		gridColumn_14.setText("触发规则");
		gridColumn_14.setWidth(65);
		gridViewerColumn_14.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return tct.getTriggerName()==null?"":tct.getTriggerName();
			}

			protected void setValue(Object element, Object value) {
				TagCfgTpl tct = (TagCfgTpl) element;
				tct.setTriggerName("".equals((String)value)?null:(String)value);
				gridTableViewer.update(tct, null);
			}
		});
		
		GridViewerColumn gridViewerColumn_18 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_18 = gridViewerColumn_18.getColumn();
		gridColumn_18.setText("单位");
		gridColumn_18.setWidth(50);
		gridViewerColumn_18.setEditingSupport(new EditingSupport(gridTableViewer){

			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new ComboBoxCellEditor(grid,
						varUnitArray);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tagCfgTpl = (TagCfgTpl) element;
				if (tagCfgTpl.getUnit() != null) {
					int i = 1;
					for (String varUnit : varUnitArray) {
						if (varUnit.equals(tagCfgTpl.getUnit())) {
							return i-1;
						}
						i++;
					}
				}
				return 0;
			}

			protected void setValue(Object element, Object value) {
				TagCfgTpl tagCfgTpl = (TagCfgTpl) element;
				int index = (int)value;
				if (index<=0) {
//					System.out.println("000000000000000000");
					tagCfgTpl.setUnit(null);		
				} else {
//					System.out.println("111111111111111111 " + value);
					tagCfgTpl.setUnit("".equals(varUnitArray[index])?null:varUnitArray[index]);
					//tagCfgTpl.setUnit("hjhjfghfghfghfgh");
					//tct.setUnit("".equals((String)value)?null:Integer.valueOf((String)value));
//					//重新初始化子类型
//					varSubTypeList = typeService.getVarSubTypeByVarTypeName(tagCfgTpl.getVarType());
//					if(varSubTypeList != null && !varSubTypeList.isEmpty()) {
//						int len = varSubTypeList.size();
//						varSubTypeArray = new String[len + 1];
//						varSubTypeArray[0] = "";
//						for (int i = 1; i <= len; i++) {
//							varSubTypeArray[i] = varSubTypeList.get(i - 1).getValue();
//						}
//					}
					
				}

//				System.out.println("进行刷新..." + tagCfgTpl.getUnit());
				gridTableViewer.update(tagCfgTpl, null);
			}
		
		});
		
		GridViewerColumn gridViewerColumn_15 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_15 = gridViewerColumn_15.getColumn();
		gridColumn_15.setText("最大值");
		gridColumn_15.setWidth(60);
		gridViewerColumn_15.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return tct.getMaxValue()==null?"":String.valueOf(tct.getMaxValue());
			}

			protected void setValue(Object element, Object value) {
				TagCfgTpl tct = (TagCfgTpl) element;
				tct.setMaxValue("".equals((String)value)?null:Double.valueOf((String)value));
				gridTableViewer.update(tct, null);
			}
		});

		GridViewerColumn gridViewerColumn_16 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_16 = gridViewerColumn_16.getColumn();
		gridColumn_16.setWidth(60);
		gridColumn_16.setText("最小值");
		gridViewerColumn_16.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return tct.getMinValue()==null?"":String.valueOf(tct.getMinValue());
			}

			protected void setValue(Object element, Object value) {
				TagCfgTpl tct = (TagCfgTpl) element;
				tct.setMinValue("".equals((String)value)?null:Double.valueOf((String)value));
				gridTableViewer.update(tct, null);
			}
		});

		GridViewerColumn gridViewerColumn_17 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_17 = gridViewerColumn_17.getColumn();
		gridColumn_17.setWidth(65);
		gridColumn_17.setText("脉冲单位");
		gridViewerColumn_17.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				TagCfgTpl tct = (TagCfgTpl) element;
				return tct.getUnitValue()==null?"":String.valueOf(tct.getUnitValue());
			}

			protected void setValue(Object element, Object value) {
				TagCfgTpl tct = (TagCfgTpl) element;
				tct.setUnitValue("".equals((String)value)?null:Integer.valueOf((String)value));
				gridTableViewer.update(tct, null);
			}
		});

		Menu menu = new Menu(grid);
		grid.setMenu(menu);

		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) listViewer_1
						.getSelection();
				if (addedTplName == null && selection.isEmpty()) {// 非新增变量且未选择
					MessageDialog.openWarning(grid.getShell(), "提示", "未选择模板！");
					return;
				}

				TagCfgTpl tagCfgTpl = new TagCfgTpl();
				tagCfgTpl.setTagName("新增的变量");
				tagCfgTpl.setTagNameShow("新增的变量CD");
				tagCfgTpl.setBaseValue(0.0f);
				tagCfgTpl.setCoefValue(1.0f);
				tagCfgTpl.setStorage("");

				tagCfgTplList.add(tagCfgTpl);

				gridTableViewer.setInput(tagCfgTplList);
				gridTableViewer.refresh();
				// gridTableViewer.setSelection(selection);
			}
		});
		menuItem.setText("添加变量");

		MenuItem menuItem_1 = new MenuItem(menu, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) gridTableViewer
						.getSelection();
				if (selection.isEmpty()) {
					MessageDialog.openWarning(grid.getShell(), "提示", "未选择变量！");
					return;
				}

				GridItem gridItems[] = grid.getSelection();
				for (GridItem gi : gridItems) {
					TagCfgTpl selectedTpl = (TagCfgTpl) gi.getData();
					tagCfgTplList.remove(selectedTpl);

					if (selectedTpl.getId() != null) {
						deletedTplList.add(selectedTpl);
					}
				}
				gridTableViewer.refresh();

			}
		});
		menuItem_1.setText("删除变量");

		gridTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		gridTableViewer.setLabelProvider(new ViewerLabelProvider_1());
		gridTableViewer.setInput(tagCfgTplList);

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		composite_2.setBounds(0, 0, 64, 64);

		Button button_1 = new Button(composite_2, SWT.NONE);
		button_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ("".equals(text_tpl_name.getText().trim())) {
					MessageDialog.openError(grid.getShell(), "错误", "模板名不能为空！");
					return;
				}

				if (tagCfgTplList == null || tagCfgTplList.isEmpty()) {
					MessageDialog.openError(grid.getShell(), "错误",
							"模板无变量，无法保存！");
					return;
				}

				for (TagCfgTpl tct : deletedTplList) {
					tagCfgTplService.deleteById(tct.getId());
				}

				for (TagCfgTpl tct : tagCfgTplList) {
					tct.setTplName(text_tpl_name.getText().trim());//更新模板名字
//					//更新变量分组与变量Key
//					if(tct.getSubType() == null) {
//						tct.setVarGroup(null);
//						tct.setVarName(null);
//					} else {
////						tct.setVarGroup(tct.getSubType().getVarGroup());
////						tct.setVarName(tct.getSubType().toString().toLowerCase());
//					}
					
					tagCfgTplService.update(tct); 	//正确的 
										
				}

				tplNameList = tagCfgTplService.findAllTplName();
				listViewer_1.setInput(tplNameList);
				listViewer_1.refresh();
				deletedTplList.clear();
				
				gridTableViewer.refresh();
				
				MessageDialog.openInformation(gridTableViewer.getGrid().getShell(), "提示", "保存成功！");
			}
		});
		button_1.setText("  保 存 模 板  ");
		sashForm.setWeights(new int[] { 78, 953 });

	}

	private class MenuListener implements IMenuListener {
		private ListViewer listViewer;

		public MenuListener(ListViewer listViewer) {
			this.listViewer = listViewer;
		}

		@Override
		public void menuAboutToShow(IMenuManager manager) {
			IStructuredSelection selection = (IStructuredSelection) listViewer
					.getSelection();
			if (!selection.isEmpty()) {
				createContextMenu(selection.getFirstElement());
			} else {// 新建
				Action action = new Action() {
					public void run() {
						addTpl();
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
				Action action = new Action() {
					public void run() {
						addTpl();
					}
				};
				action.setText("新建变量模板");
				menuMng.add(action);

				action = new Action() {
					public void run() {
						String tplNames[] = listViewer_1.getList()
								.getSelection();
						for (String name : tplNames) {
							tagCfgTplService.deleteAllVariablesByTplName(name);
						}

						tplNameList = tagCfgTplService.findAllTplName();
						listViewer_1.setInput(tplNameList);
						listViewer_1.refresh();

						tagCfgTplList.clear();
						deletedTplList.clear();
						gridTableViewer.setInput(tagCfgTplList);
						gridTableViewer.refresh();
						
						// 删除该模板关联的组态图
						tplModelConfigService.deleteByTplname(text_tpl_name.getText().trim());
					}
				};
				action.setText("删除变量模板");
				menuMng.add(action);
			}
		}

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	private class ViewerLabelProvider_1 extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			TagCfgTpl tagCfgTpl = (TagCfgTpl) element;

			switch (columnIndex) {
			case 0:// 变量名
				return tagCfgTpl.getTagName();
			case 1:// 组态设计显示名
				return tagCfgTpl.getTagNameShow() ;
			case 2:// 变量类型
				return tagCfgTpl.getVarType() == null ? null : getVarType(tagCfgTpl.getVarType()).getValue();
			case 3:// 变量子类型
				return tagCfgTpl.getSubType() == null ? null : (getVarSubType(tagCfgTpl.getSubType())==null?null:getVarSubType(tagCfgTpl.getSubType()).getValue());
			case 4:// 变量分组
			{
//				System.out.println(tagCfgTpl.getVarGroup()+ "   ---------------------------------------------------");
				return tagCfgTpl.getVarGroup() == null ? null : getVarGroupCfg(tagCfgTpl.getVarGroup()).getValue();
				
			}
			case 5:// 变量key
				return tagCfgTpl.getVarName();
			case 6:// 功能码
				return String.valueOf(tagCfgTpl.getFunCode());
			case 7:// 数据地址
				return String.valueOf(tagCfgTpl.getDataId());
			case 8:// 字节长度
				return String.valueOf(tagCfgTpl.getByteLen());
			case 9:// 字节偏移量
				return String.valueOf(tagCfgTpl.getByteOffset());
			case 10:// 位偏移量
				return String.valueOf(tagCfgTpl.getBitOffset());
			case 11:// 值类型
				return tagCfgTpl.getDataType() == null ? null : getDataType(tagCfgTpl.getDataType()).getValue();
			case 12:// 基数
				return tagCfgTpl.getBaseValue() == null ? "" : String
						.valueOf(tagCfgTpl.getBaseValue());
			case 13:// 系数
				return tagCfgTpl.getCoefValue() == null ? "" : String
						.valueOf(tagCfgTpl.getCoefValue());
			case 14:// 存储规则
				return tagCfgTpl.getStorage();
			case 15:// 触发规则
				return tagCfgTpl.getTriggerName();
			case 16:// 单位
				return tagCfgTpl.getUnit();
			case 17:// 最大值
				return tagCfgTpl.getMaxValue() == null ? "" : String
						.valueOf(tagCfgTpl.getMaxValue());
			case 18:// 最小值
				return tagCfgTpl.getMinValue() == null ? "" : String
						.valueOf(tagCfgTpl.getMinValue());
			case 19:// 脉冲单位
				return tagCfgTpl.getUnitValue() == null ? "" : String
						.valueOf(tagCfgTpl.getUnitValue());

			default:
				break;
			}

			return null;
		}
	}

	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}

		public String getText(Object element) {
			if (element instanceof String) {
				return (String) element;
			}
			return super.getText(element);
		}
	}

	/**
	 * 初始化模板信息
	 * 
	 * @param tplName
	 */
	private void initTplInfo(String tplName) {
		text_tpl_name.setText(tplName);
		if (!"".equals(tplName)) {
			initVariableByTplName(tplName);
		}
		
		btnConfigDesign.setEnabled(true);	// 选择模板后，组态设计按钮可用
	}

	private void initVariableByTplName(String tplName) {
		tagCfgTplList.clear();

		tagCfgTplList = tagCfgTplService.findVariablesByTplName(tplName);
		log.debug("变量个数：" + tagCfgTplList.size());

		gridTableViewer.setInput(tagCfgTplList);
		gridTableViewer.refresh();

	}

	/**
	 * 新建变量模板
	 */
	private void addTpl() {
		text_tpl_name.setText("新增变量模板");
		
		addedTplName = text_tpl_name.getText();
		selectedTplName = null;

		listViewer_1.setSelection(null);

		tagCfgTplList.clear();
		deletedTplList.clear();
		gridTableViewer.setInput(tagCfgTplList);
		gridTableViewer.refresh();

	}
	/**
	 * 通过变量名或值获得变量类型
	 * @param key
	 * @return
	 */
	private VarType getVarType(String key) {
		if(varTypeList!=null && !varTypeList.isEmpty()) {
			for(VarType varType : varTypeList) {
				if(varType.getName().equals(key) || varType.getValue().equals(key)) {
					return varType;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 通过变量名或值获得变量子类型
	 * @param key
	 * @return
	 */
	private VarSubType getVarSubType(String key) {
		if(allSubTypeList!=null && !allSubTypeList.isEmpty()) {
			for(VarSubType varSubType : allSubTypeList) {
				if(varSubType.getName().equals(key) || varSubType.getValue().equals(key)) {
					return varSubType;
				}
			}
		}
		return null;
	}
	/**
	 * 通过关键字获得变量分组
	 * @param key
	 * @return
	 */
	private VarGroupCfg getVarGroupCfg(String key) {
		if(varGroupCfgList!=null && !varGroupCfgList.isEmpty()) {
			for(VarGroupCfg type : varGroupCfgList) {
				if(type.getName().equals(key) || type.getValue().equals(key)) {
					return type;
				}
			}
		}
		return null;
	}
	/**
	 * 获得值类型
	 * @param key
	 * @return
	 */
	private DataValueType getDataType(String key) {
		if(dataTypeList!=null && !dataTypeList.isEmpty()) {
			for(DataValueType type : dataTypeList) {
				if(type.getName().equals(key) || type.getValue().equals(key)) {
					return type;
				}
			}
		}
		return null;
	}

	/*
	 * 根据条件筛选变量
	 */
	private void tagSearch() {
		//System.out.println("变量类型进行了选择！");
		
		tagCfgTplListSelect.clear();
		TagCfgTpl temp = new TagCfgTpl();	//临时变量
		
		String tagName = "";				//获得value对应的name，例如value为遥测，name获得为：YC
		for (int j=0 ; j< varTypeList.size(); j++) {
			VarType temp1=varTypeList.get(j);
			if (temp1.getValue().equals(combo.getText().trim())) {
				tagName =  temp1.getName();
				break;
			}
		}
		
		String tagGroupName = "";			//分组的名字
		for ( int jj=0 ; jj < varGroupCfgList.size(); jj++ ) {
			VarGroupCfg temp11 = varGroupCfgList.get(jj);
			if (temp11.getValue().equals(combo_1.getText().trim())) {
				tagGroupName = temp11.getName();
				break;
			}
		}
		
		if(combo.getSelectionIndex() == 0 && combo_1.getSelectionIndex() == 0 ) {	//全部选择'全部'
			for (int k=0; k<tagCfgTplList.size(); k++) {
				tagCfgTplListSelect.add(tagCfgTplList.get(k));
			}
		} else if (combo.getSelectionIndex() == 0 && combo_1.getSelectionIndex() != 0 ) {
			
			for ( int i=0; i< tagCfgTplList.size(); i++) {
				temp = tagCfgTplList.get(i);
				//System.out.println(temp.getVarGroup() + ", " + tagGroupName);
				if (temp.getVarGroup()!=null && temp.getVarGroup().equals(tagGroupName)) {
					tagCfgTplListSelect.add(temp);
				}
			}
			
		} else if (combo.getSelectionIndex() != 0 && combo_1.getSelectionIndex() == 0 ) {
			
			for ( int i=0; i< tagCfgTplList.size(); i++) {
				temp = tagCfgTplList.get(i);

				if (temp.getVarType()!=null && temp.getVarType().equals(tagName)) {
					tagCfgTplListSelect.add(temp);
				}
			}
			
		} else {
			
			for ( int i=0; i< tagCfgTplList.size(); i++) {
				temp = tagCfgTplList.get(i);
				if (temp.getVarType()!=null && temp.getVarType().equals(tagName) &&
						temp.getVarGroup()!=null && temp.getVarGroup().equals(tagGroupName) ) {
					tagCfgTplListSelect.add(temp);
				}
			}
			
		}
		
		gridTableViewer.setInput(tagCfgTplListSelect);
		gridTableViewer.refresh();

	}
	
	/**
	 * 将导入模板中的变量在内存中构建出来，并加入到当前模板集合中
	 * @autor 王蓬
	 * @param tagCfgTplListImport
	 */
	private void makeNewModelTags(List<TagCfgTpl> tagCfgTplListImport){
	
		for ( int ii=0; ii< tagCfgTplListImport.size(); ii++) {
			TagCfgTpl temp= tagCfgTplListImport.get(ii);
			TagCfgTpl newTag = new TagCfgTpl();			//内存新申请的变量
			
			newTag.setBaseValue(temp.getBaseValue());
			newTag.setBitOffset(temp.getBitOffset());
			newTag.setByteLen(temp.getByteLen());
			newTag.setByteOffset(temp.getByteOffset());
			newTag.setCoefValue(temp.getCoefValue());
			newTag.setDataId(temp.getDataId());
			newTag.setDataType(temp.getDataType());
			newTag.setFunCode(temp.getFunCode());
			newTag.setMaxValue(temp.getMaxValue());
			newTag.setMinValue(temp.getMinValue());
			newTag.setStorage(temp.getStorage());
			newTag.setSubType(temp.getSubType());
			newTag.setTagName(temp.getTagName());
			newTag.setTplName(temp.getTplName());
			newTag.setTriggerName(temp.getTriggerName());
			newTag.setUnitValue(temp.getUnitValue());
			newTag.setVarGroup(temp.getVarGroup());
			newTag.setVarName(temp.getVarName());
			newTag.setVarType(temp.getVarType());
			
			tagCfgTplList.add(newTag);	//添加入当前模板
		}

	}
}
