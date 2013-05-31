package com.ht.scada.config.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.EndTagExtInfo;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.service.EndTagExtInfoService;
import com.ht.scada.common.tag.service.EndTagService;
import com.ht.scada.common.tag.type.entity.EndTagExtInfoName;
import com.ht.scada.common.tag.type.entity.EndTagExtInfoValue;
import com.ht.scada.common.tag.type.entity.EndTagSubType;
import com.ht.scada.common.tag.type.entity.EndTagType;
import com.ht.scada.common.tag.type.service.TypeService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.LayoutUtil;
import com.ht.scada.config.util.ViewPropertyChange;

public class EndTagView extends ViewPart implements IPropertyChangeListener {
	private static final Logger log = LoggerFactory.getLogger(EndTagView.class);

	public EndTagView() {
		// 初始化监控对象类型
		EndTagType endTagType = new EndTagType(null,"");
		endTagTypeList.add(endTagType);
		List<EndTagType> list = typeService.getAllEndTagType();
		if(list != null && !list.isEmpty()) {
			endTagTypeList.addAll(list);
		}
		
		// 初始化监控对象子类型
		EndTagSubType endTagSubType = new EndTagSubType(null, "");
		endTagSubTypeList.add(endTagSubType);
		
		//初始化油井阶段与油井工艺属性
		endTagExtInfoValueStageList = typeService.getExtInfoValuesByInfoName(STAGE_ATTR);
		endTagExtInfoValueTechList = typeService.getExtInfoValuesByInfoName(TECH_ATTR);
		if(endTagExtInfoValueStageList!=null && !endTagExtInfoValueStageList.isEmpty()) {
			extValueStageStr = new String[endTagExtInfoValueStageList.size()];
			for(int i = 0;i<endTagExtInfoValueStageList.size();i++) {
				extValueStageStr[i] = endTagExtInfoValueStageList.get(i).getValue();
			}
		}
		if(endTagExtInfoValueTechList!=null && !endTagExtInfoValueTechList.isEmpty()) {
			extValueTechStr = new String[endTagExtInfoValueTechList.size()];
			for(int i = 0;i<endTagExtInfoValueTechList.size();i++) {
				extValueTechStr[i] = endTagExtInfoValueTechList.get(i).getValue();
			}
		}
		
	}

	public static final String ID = "com.ht.scada.config.view.EndTagView";
	
	private final String STAGE_ATTR = "STAGE";
	private final String TECH_ATTR = "TECHNOLOGY";
	private Text text_name;
	private EndTag endTag;
	private List<EndTagType> endTagTypeList = new ArrayList<EndTagType>();
	private List<EndTagSubType> endTagSubTypeList = new ArrayList<EndTagSubType>();
	private List<EndTagExtInfoName> endTagExtInfoNameList = new ArrayList<EndTagExtInfoName>();
	
	private List<EndTagExtInfoValue> endTagExtInfoValueStageList = new ArrayList<EndTagExtInfoValue>();	//采油阶段
	private List<EndTagExtInfoValue> endTagExtInfoValueTechList = new ArrayList<EndTagExtInfoValue>();	//油井工艺
	private String extValueStageStr[] = new String[]{""};
	private String extValueTechStr[] = new String[]{""};

	private EndTagService endTagService = (EndTagService) Activator
			.getDefault().getApplicationContext().getBean("endTagService");
	private EndTagExtInfoService endTagExtInfoService = (EndTagExtInfoService) Activator
			.getDefault().getApplicationContext()
			.getBean("endTagExtInfoService");
	private TypeService typeService = (TypeService) Activator.getDefault()
			.getApplicationContext().getBean("typeService");
	private GridTableViewer gridTableViewer;
	private List<EndTagExtInfo> endTagExtInfoList = new ArrayList<EndTagExtInfo>(); // 末端节点扩展信息列表
	private ComboViewer comboViewer_endType;
	private ComboViewer comboViewer_endSubType;
	private Text text_code;

	public void createPartControl(Composite parent) {

		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.verticalSpacing = 20;
		gl_parent.marginTop = 25;
		gl_parent.marginLeft = 40;
		parent.setLayout(gl_parent);

		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("监控对象名：");

		text_name = new Text(parent, SWT.BORDER);
		GridData gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_text_name.widthHint = 100;
		text_name.setLayoutData(gd_text_name);
		
		Label label_1 = new Label(parent, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("编号：");
		
		text_code = new Text(parent, SWT.BORDER);
		GridData gd_text_code = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_code.widthHint = 100;
		text_code.setLayoutData(gd_text_code);

		Label typeLabel = new Label(parent, SWT.NONE);
		typeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		typeLabel.setText("监控对象类型：");
		
		comboViewer_endType = new ComboViewer(parent, SWT.READ_ONLY);
		comboViewer_endType.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				//初始化子类型
				endTagSubTypeList = new ArrayList<EndTagSubType>();
				EndTagSubType endTagSubType = new EndTagSubType(null, "");
				endTagSubTypeList.add(endTagSubType);
				
				List<EndTagSubType> list = typeService.getSubTypeByEndTagTypeValue(comboViewer_endType.getCombo().getText().trim());
				if(list != null && !list.isEmpty()) {
					endTagSubTypeList.addAll(list);
				}
				comboViewer_endSubType.setInput(endTagSubTypeList);
				comboViewer_endSubType.refresh();

				
				IStructuredSelection iss = (IStructuredSelection) comboViewer_endType.getSelection();
				EndTagType endTagType = (EndTagType) iss.getFirstElement();
				if(endTagType!=null && endTagType.getName()!=null && !endTagType.getName().equals(endTag.getType())) {//类型改变了
					//初始化属性
					endTagExtInfoNameList = new ArrayList<EndTagExtInfoName>();
					List<EndTagExtInfoName> nameList = typeService.getExtInfoNamesByEndTagValue(comboViewer_endType.getCombo().getText().trim());
					if(nameList != null && !nameList.isEmpty()) {
						endTagExtInfoNameList.addAll(nameList);
					}
					//按类型属性重新定义属性名
					endTagExtInfoList = new ArrayList<EndTagExtInfo>();
					if(endTagExtInfoNameList != null && !endTagExtInfoNameList.isEmpty()) {
						for(EndTagExtInfoName name : endTagExtInfoNameList) {
							EndTagExtInfo info = new EndTagExtInfo();
							info.setEndTag(endTag);
							info.setName(name.getValue());
							info.setValue("");
							info.setKeyName(name.getName());
							
							endTagExtInfoList.add(info);
						}
					}
				} else if(endTagType!=null && endTagType.getName()!=null && endTagType.getName().equals(endTag.getType())) {//仍为原来指则重新查询数据库
					// 初始化末端节点扩展信息列表
					endTagExtInfoList = new ArrayList<EndTagExtInfo>();
					if (endTag.getId() != null) {
						List<EndTagExtInfo> endTagExtList = endTagExtInfoService.getByEndTagId(endTag
								.getId());
						if(endTagExtList != null && !endTagExtList.isEmpty()) {
							endTagExtInfoList.addAll(endTagExtList);
						}
					}
				}

				gridTableViewer.setInput(endTagExtInfoList);
				gridTableViewer.refresh();
			}
		});
		Combo combo = comboViewer_endType.getCombo();
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 90;
		combo.setLayoutData(gd_combo);
		comboViewer_endType.setLabelProvider(new ViewerLabelProvider_1());
		comboViewer_endType.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer_endType.setInput(endTagTypeList);

		Label subTypeLabel = new Label(parent, SWT.NONE);
		subTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		subTypeLabel.setText("监控对象子类型：");
		
		comboViewer_endSubType = new ComboViewer(parent, SWT.READ_ONLY);
		Combo combo_1 = comboViewer_endSubType.getCombo();
		GridData gd_combo_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo_1.widthHint = 90;
		combo_1.setLayoutData(gd_combo_1);
		comboViewer_endSubType.setLabelProvider(new ViewerLabelProvider_2());
		comboViewer_endSubType.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer_endSubType.setInput(endTagSubTypeList);

		gridTableViewer = new GridTableViewer(parent, SWT.BORDER);
		final Grid grid = gridTableViewer.getGrid();
		grid.setHeaderVisible(true);
		GridData gd_grid = new GridData(SWT.LEFT, SWT.FILL, true, true, 2, 1);
		gd_grid.widthHint = 305;
		grid.setLayoutData(gd_grid);

		GridViewerColumn nameGridViewerColumn = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		
		GridColumn nameGridColumn = nameGridViewerColumn.getColumn();
		nameGridColumn.setAlignment(SWT.CENTER);
		nameGridColumn.setWidth(150);
		nameGridColumn.setText("属性名");

		GridViewerColumn valueGridViewerColumn = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		valueGridViewerColumn.setEditingSupport(new EditingSupport(
				gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				EndTagExtInfo info = (EndTagExtInfo) element;
				if(info.getKeyName().equals(STAGE_ATTR)) {
					ComboBoxCellEditor ce = new ComboBoxCellEditor(grid, extValueStageStr);
					return ce;
				} else if(info.getKeyName().equals(TECH_ATTR)) {
					ComboBoxCellEditor ce = new ComboBoxCellEditor(grid, extValueTechStr);
					return ce;
				} else {
					CellEditor ce = new TextCellEditor(grid);
					return ce;
				}
				
			}

			protected Object getValue(Object element) {
				EndTagExtInfo info = (EndTagExtInfo) element;
				int i = 0;
				if(info.getKeyName().equals(STAGE_ATTR)) {
					for(String s : extValueStageStr) {
						if(info.getValue().equals(s)) {
							return i;
						}
						i++;
					}
					return 0;
				} else if(info.getKeyName().equals(TECH_ATTR)) {
					for(String s : extValueTechStr) {
						if(info.getValue().equals(s)) {
							return i;
						}
						i++;
					}
					return 0;
				} else {
					return info.getValue();
				}
				
			}

			protected void setValue(Object element, Object value) {
				EndTagExtInfo info = (EndTagExtInfo) element;
				//属性值
				if(info.getKeyName().equals(STAGE_ATTR)) {
					int i = (int)value;
					info.setValue(extValueStageStr[i]);
				} else if(info.getKeyName().equals(TECH_ATTR)) {
					int i = (int)value;
					info.setValue(extValueTechStr[i]);
				} else {
					info.setValue((String) value);
				}
				
				gridTableViewer.update(info, null);
			}
		});
		GridColumn valueGridColumn = valueGridViewerColumn.getColumn();
		valueGridColumn.setAlignment(SWT.CENTER);
		valueGridColumn.setCellSelectionEnabled(false);
		valueGridColumn.setWidth(150);
		valueGridColumn.setText("属性值");

		gridTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		gridTableViewer.setLabelProvider(new ViewerLabelProvider());
		gridTableViewer.setInput(endTagExtInfoList);

		Button saveButton = new Button(parent, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (endTag.getId() == null) {// 新建
					if ("".equals(text_name.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"监控对象名字不能为空！");
						return;
					}

					endTag.setName(text_name.getText().trim());
					endTag.setCode("".equals(text_code.getText().trim())?null:text_code.getText().trim());
					
					IStructuredSelection iss = (IStructuredSelection) comboViewer_endType.getSelection();
					EndTagType endTagType = (EndTagType) iss.getFirstElement();
					endTag.setType(endTagType.getName());
					
					IStructuredSelection iss2 = (IStructuredSelection) comboViewer_endSubType.getSelection();
					EndTagSubType endTagSubType = (EndTagSubType)iss2.getFirstElement();
					if(endTagSubType != null) {
						endTag.setSubType(endTagSubType.getName());
					} else {
						endTag.setSubType(null);
					}

					endTagService.create(endTag);
					
					/**
					 * 保存属性
					 */
					endTagExtInfoService.saveAll(endTagExtInfoList);


					ScadaObjectTreeView.treeViewer.add(endTag.getMajorTag(),
							endTag);
					ScadaObjectTreeView.treeViewer.setExpandedState(
							endTag.getMajorTag(), true);

				} else {// 编辑
					if ("".equals(text_name.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"监控对象名字不能为空！");
						return;
					}
					endTag = endTagService.getById(endTag.getId());

					endTag.setName(text_name.getText().trim());
					endTag.setCode("".equals(text_code.getText().trim())?null:text_code.getText().trim());
					
					String oldType = endTag.getType();	//原始类型
					
					IStructuredSelection iss = (IStructuredSelection) comboViewer_endType.getSelection();
					EndTagType endTagType = (EndTagType) iss.getFirstElement();
					endTag.setType(endTagType.getName());
					
					IStructuredSelection iss2 = (IStructuredSelection) comboViewer_endSubType.getSelection();
					EndTagSubType endTagSubType = (EndTagSubType)iss2.getFirstElement();
					if(endTagSubType != null) {
						endTag.setSubType(endTagSubType.getName());
					} else {
						endTag.setSubType(null);
					}
					
					/**
					 * 保存属性
					 */
					if(oldType.equals(endTag.getType())) {
						log.debug("类型未变，更新保存");
						endTagExtInfoService.saveAll(endTagExtInfoList);
					} else {
						log.debug("类型改变，删除原来关联，保存新关联");
						endTagExtInfoService.deleteByEndTagId(endTag.getId());
						endTagExtInfoService.saveAll(endTagExtInfoList);
					}
					endTagService.update(endTag);

					ScadaObjectTreeView.treeViewer.update(endTag, null);
				}

				 LayoutUtil.hideViewPart();
			}
		});
		saveButton.setText(" 保  存 ");

		Button cancelButton = new Button(parent, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LayoutUtil.hideViewPart();
			}
		});
		cancelButton.setText(" 取  消 ");

		ViewPropertyChange.getInstance().addPropertyChangeListener("endtag",
				this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(FirePropertyConstants.ENDTAG_ADD)) {
			endTag = new EndTag();
			Object object = event.getNewValue();
			if (object instanceof MajorTag) {
				endTag.setMajorTag((MajorTag) object);
			}

			// 初始化控件值
			text_name.setText("");
			text_code.setText("");
			comboViewer_endType.getCombo().setText("");
			comboViewer_endSubType.getCombo().setText("");
			
			//初始化属性
			endTagExtInfoNameList = new ArrayList<EndTagExtInfoName>();
			List<EndTagExtInfoName> nameList = typeService.getExtInfoNamesByEndTagValue(comboViewer_endType.getCombo().getText().trim());
			if(nameList != null && !nameList.isEmpty()) {
				endTagExtInfoNameList.addAll(nameList);
			}
			//按类型属性重新定义属性名
			endTagExtInfoList = new ArrayList<EndTagExtInfo>();
			if(endTagExtInfoNameList != null && !endTagExtInfoNameList.isEmpty()) {
				for(EndTagExtInfoName name : endTagExtInfoNameList) {
					EndTagExtInfo info = new EndTagExtInfo();
					info.setEndTag(endTag);
					info.setName(name.getValue());
					info.setValue("");
					info.setKeyName(name.getName());
					
					endTagExtInfoList.add(info);
				}
			}

		} else if (event.getProperty()
				.equals(FirePropertyConstants.ENDTAG_EDIT)) {
			endTag = (EndTag) event.getNewValue();

			// 初始化控件值
			text_name.setText(endTag.getName());
			text_code.setText(endTag.getCode()==null?"":endTag.getCode());

			//初始化监控对象类型
			if (endTag.getType() != null) {
				for(EndTagType endTagType : endTagTypeList) {
					if(endTagType.getName()!=null && endTagType.getName().equals(endTag.getType())) {
						comboViewer_endType.getCombo().setText(endTagType.getValue());
						break;
					}
				}
				
				//初始化监控对象子类型
				endTagSubTypeList = new ArrayList<EndTagSubType>();
				EndTagSubType endTagSubType = new EndTagSubType(null, "");
				endTagSubTypeList.add(endTagSubType);
				
				List<EndTagSubType> list = typeService.getSubTypeByEndTagTypeValue(comboViewer_endType.getCombo().getText().trim());
				if(list != null && !list.isEmpty()) {
					endTagSubTypeList.addAll(list);
				}
				
				comboViewer_endSubType.setInput(endTagSubTypeList);
				comboViewer_endSubType.refresh();
				
				if (endTag.getSubType() != null) {
					for(EndTagSubType sub : endTagSubTypeList) {
						if(sub.getName()!=null && sub.getName().equals(endTag.getSubType())) {
							comboViewer_endSubType.getCombo().setText(sub.getValue());
							break;
						}
					}
				}
			} 

			// 初始化末端节点扩展信息列表
			endTagExtInfoList = new ArrayList<EndTagExtInfo>();
			if (endTag.getId() != null) {
				List<EndTagExtInfo> list = endTagExtInfoService.getByEndTagId(endTag
						.getId());
				if(list != null && !list.isEmpty()) {
					endTagExtInfoList.addAll(list);
				}
			}
			
			

		}
		// 给扩展信息表格赋值并刷新
		gridTableViewer.setInput(endTagExtInfoList);
		gridTableViewer.refresh();
	}

	

	@Override
	public void dispose() {
		ViewPropertyChange.getInstance().removePropertyChangeListener("endtag");
		super.dispose();
	}

	private static class ViewerLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			EndTagExtInfo endTagExtInfo = (EndTagExtInfo) element;

			switch (columnIndex) {
			case 0:
				return endTagExtInfo.getName() == null ? null : endTagExtInfo
						.getName();
			case 1:
				return endTagExtInfo.getValue() == null ? null : endTagExtInfo
						.getValue();

			default:
				break;
			}
			return null;
		}
	}
	
	private static class ViewerLabelProvider_2 extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		public String getText(Object element) {
			EndTagSubType endTagSubType = (EndTagSubType)element;
			return endTagSubType.getValue();
		}
	}
	private static class ViewerLabelProvider_1 extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		public String getText(Object element) {
			EndTagType endTagType = (EndTagType)element;
			return endTagType.getValue();
		}
	}
}
