package com.ht.scada.config.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ViewPropertyChange;
import com.ht.scada.oildata.tag.EndTagSubType;
import com.ht.scada.oildata.tag.EndTagType;

public class EndTagView extends ViewPart implements IPropertyChangeListener {

	private static final Logger log = LoggerFactory.getLogger(EndTagView.class);

	public EndTagView() {
		// 初始化监控对象类型
		int lenght = EndTagType.values().length;
		endTagType = new String[lenght + 1];
		endTagType[0] = "请选择";
		for (int i = 1; i <= lenght; i++) {
			endTagType[i] = EndTagType.values()[i - 1].getValue();
		}
		// 初始化监控对象子类型
		endTagSubType = new String[1];
		endTagSubType[0] = "请选择";

	}

	public static final String ID = "com.ht.scada.config.view.EndTagView";
	private Text text_name;
	private EndTag endTag;
	private String endTagType[];
	private String endTagSubType[];

	private EndTagService endTagService = (EndTagService) Activator
			.getDefault().getApplicationContext().getBean("endTagService");
	private EndTagExtInfoService endTagExtInfoService = (EndTagExtInfoService) Activator
			.getDefault().getApplicationContext()
			.getBean("endTagExtInfoService");

	private Combo typeCombo;
	private Combo subTypeCombo;
	private GridTableViewer gridTableViewer;
	private List<EndTagExtInfo> endTagExtInfoList = new ArrayList<>(); // 末端节点扩展信息列表
	private List<EndTagExtInfo> deletedExtInfoList = new ArrayList<>(); // 要删除的扩展信息列表

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

		Label typeLabel = new Label(parent, SWT.NONE);
		typeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		typeLabel.setText("监控对象类型：");

		typeCombo = new Combo(parent, SWT.READ_ONLY);

		GridData gd_typeCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1);
		gd_typeCombo.widthHint = 85;
		typeCombo.setLayoutData(gd_typeCombo);
		typeCombo.setItems(endTagType);

		Label subTypeLabel = new Label(parent, SWT.NONE);
		subTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		subTypeLabel.setText("监控对象子类型：");

		subTypeCombo = new Combo(parent, SWT.NONE);
		GridData gd_subTypeCombo = new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1);
		gd_subTypeCombo.widthHint = 85;
		subTypeCombo.setLayoutData(gd_subTypeCombo);
		subTypeCombo.setItems(endTagSubType);

		typeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (typeCombo.getText().equals(EndTagType.YOU_JING.getValue())) {// 油井
					int len = EndTagSubType.values().length;
					endTagSubType = new String[len + 1];
					endTagSubType[0] = "";
					for (int i = 1; i <= len; i++) {
						endTagSubType[i] = EndTagSubType.values()[i - 1]
								.getValue();
					}
				} else {
					endTagSubType = new String[1];
					endTagSubType[0] = "";
				}
				subTypeCombo.setItems(endTagSubType);
			}
		});

		gridTableViewer = new GridTableViewer(parent, SWT.BORDER);
		final Grid grid = gridTableViewer.getGrid();
		grid.setHeaderVisible(true);
		GridData gd_grid = new GridData(SWT.LEFT, SWT.FILL, true, true, 2, 1);
		gd_grid.widthHint = 305;
		grid.setLayoutData(gd_grid);

		GridViewerColumn nameGridViewerColumn = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		nameGridViewerColumn.setEditingSupport(new EditingSupport(
				gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				EndTagExtInfo info = (EndTagExtInfo) element;
				return info.getName();
			}

			protected void setValue(Object element, Object value) {
				EndTagExtInfo info = (EndTagExtInfo) element;
				info.setName((String) value);
				gridTableViewer.update(info, null);
			}
		});
		GridColumn nameGridColumn = nameGridViewerColumn.getColumn();
		nameGridColumn.setAlignment(SWT.CENTER);
		nameGridColumn.setWidth(150);
		nameGridColumn.setText("末端结点扩展信息名称");

		GridViewerColumn valueGridViewerColumn = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		valueGridViewerColumn.setEditingSupport(new EditingSupport(
				gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				EndTagExtInfo info = (EndTagExtInfo) element;
				return info.getValue();
			}

			protected void setValue(Object element, Object value) {
				EndTagExtInfo info = (EndTagExtInfo) element;
				info.setValue((String) value);
				gridTableViewer.update(info, null);
			}
		});
		GridColumn valueGridColumn = valueGridViewerColumn.getColumn();
		valueGridColumn.setAlignment(SWT.CENTER);
		valueGridColumn.setCellSelectionEnabled(false);
		valueGridColumn.setWidth(150);
		valueGridColumn.setText("末端结点扩展信息的值");

		Menu menu = new Menu(grid);
		grid.setMenu(menu);

		MenuItem addMenuItem = new MenuItem(menu, SWT.NONE);
		addMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EndTagExtInfo endTagExtInfo = new EndTagExtInfo();
				endTagExtInfo.setName("请输入名称");
				endTagExtInfo.setValue("请输入值");
				endTagExtInfoList.add(endTagExtInfo);

				gridTableViewer.setInput(endTagExtInfoList);
				gridTableViewer.refresh();
			}
		});
		addMenuItem.setText("添加扩展信息");

		MenuItem deleteMenuItem = new MenuItem(menu, SWT.NONE);
		deleteMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) gridTableViewer
						.getSelection();
				if (selection.isEmpty()) {
					MessageDialog.openWarning(grid.getShell(), "提示", "未选择变量！");
					return;
				}

				GridItem gridItems[] = grid.getSelection();
				for (GridItem gridItem : gridItems) {
					EndTagExtInfo selectedTpl = (EndTagExtInfo) gridItem
							.getData();
					endTagExtInfoList.remove(selectedTpl);

					if (selectedTpl.getId() != null) {
						deletedExtInfoList.add(selectedTpl);
					}
				}

				gridTableViewer.setInput(endTagExtInfoList);
				gridTableViewer.refresh();
			}
		});
		deleteMenuItem.setText("删除扩展信息");

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
					endTag.setType(EndTagType.getByValue(typeCombo.getText()) == null ? null
							: EndTagType.getByValue(typeCombo.getText())
									.toString());
					endTag.setSubType((EndTagSubType.getByValue(subTypeCombo
							.getText().trim()) == null) ? subTypeCombo
							.getText().trim() : EndTagSubType.getByValue(
							subTypeCombo.getText().trim()).toString());

					endTagService.create(endTag);

					// ===================================
					editEndTagExtInfoList();
					// ===================================

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

					endTag.setName(text_name.getText().trim());
					endTag.setType(EndTagType.getByValue(typeCombo.getText()) == null ? null
							: EndTagType.getByValue(typeCombo.getText())
									.toString());
					endTag.setSubType((EndTagSubType.getByValue(subTypeCombo
							.getText().trim()) == null) ? subTypeCombo
							.getText().trim() : EndTagSubType.getByValue(
							subTypeCombo.getText().trim()).toString());

					// ===================================
					editEndTagExtInfoList();
					// ===================================

					endTagService.update(endTag);
					deletedExtInfoList.clear();

					ScadaObjectTreeView.treeViewer.update(endTag, null);
				}

				// LayoutUtil.hideViewPart();
			}
		});
		saveButton.setText(" 保  存 ");

		Button cancelButton = new Button(parent, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IWorkbenchPart part = page.getActivePart();
				if (part instanceof IViewPart)
					page.hideView((IViewPart) part);
			}
		});
		cancelButton.setText(" 取  消 ");

		// gridTableViewer.setInput(endTagExtInfoList);
		// gridTableViewer.refresh();

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
			typeCombo.select(0);
			subTypeCombo.select(0);

		} else if (event.getProperty()
				.equals(FirePropertyConstants.ENDTAG_EDIT)) {
			endTag = (EndTag) event.getNewValue();

			// 初始化控件值
			text_name.setText(endTag.getName());

			if (endTag.getType() != null) {
				typeCombo.setText(EndTagType.valueOf(endTag.getType())
						.getValue());
			} else {
				typeCombo.select(0);
			}
			if (endTag.getSubType() != null) {
				EndTagSubType subType;
				try {
					subType = EndTagSubType.valueOf(endTag.getSubType());
				} catch (Exception e) {
					subType = null;
					// e.printStackTrace();
					log.error("无子类型" + endTag.getSubType());
				}

				subTypeCombo.setText(subType == null ? endTag.getSubType()
						: subType.getValue());
			} else {
				subTypeCombo.select(0);
			}

			// 初始化末端节点扩展信息列表
			if (endTag != null) {
				endTagExtInfoList = endTagExtInfoService.getByEndTagId(endTag
						.getId());
			}

		}
		// 给扩展信息表格赋值并刷新
		gridTableViewer.setInput(endTagExtInfoList);
		gridTableViewer.refresh();
	}

	// 修改末端节点扩展信息列表
	private void editEndTagExtInfoList() {
		if (endTagExtInfoList != null) {
			for (EndTagExtInfo info : endTagExtInfoList) {
				info.setEndTag(endTag);
				endTagExtInfoService.create(info);
			}
		}

		if (deletedExtInfoList != null) {
			for (EndTagExtInfo info : deletedExtInfoList) {
				endTagExtInfoService.deleteById(info.getId());
			}
		}
		
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
}
