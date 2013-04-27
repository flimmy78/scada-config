package com.ht.scada.config.view;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.service.EndTagService;
import com.ht.scada.common.tag.well.consts.EndTagSubType;
import com.ht.scada.common.tag.well.consts.EndTagType;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.LayoutUtil;
import com.ht.scada.config.util.ViewPropertyChange;

public class EndTagView extends ViewPart implements IPropertyChangeListener {
	private static final Logger log = LoggerFactory.getLogger(EndTagView.class);
	
	public EndTagView() {
		//初始化监控对象类型
		int lenght = EndTagType.values().length;
		endTagType = new String[lenght+1];
		endTagType[0] = "";
		for(int i=1;i<=lenght;i++) {
			endTagType[i] = EndTagType.values()[i-1].getValue();
		}
		//初始化监控对象子类型
		endTagSubType = new String[1];
		endTagSubType[0] = "";
	}

	public static final String ID = "com.ht.scada.config.view.EndTagView";
	private Text text_name;
	private EndTag endTag;
	private String endTagType[];
	private String endTagSubType[];
	
	private EndTagService endTagService = (EndTagService) Activator.getDefault()
			.getApplicationContext().getBean("endTagService");
	private Table table;
	private Combo combo;
	private Combo combo_1;
	
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.verticalSpacing = 20;
		gl_parent.marginTop = 25;
		gl_parent.marginLeft = 40;
		parent.setLayout(gl_parent);
		
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("监控对象名：");
		
		text_name = new Text(parent, SWT.BORDER);
		GridData gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_name.widthHint = 100;
		text_name.setLayoutData(gd_text_name);
		
		Label label_1 = new Label(parent, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("监控对象类型：");
		
		combo = new Combo(parent, SWT.READ_ONLY);
		
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 85;
		combo.setLayoutData(gd_combo);
		combo.setItems(endTagType);
		
		Label label_2 = new Label(parent, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setText("监控对象子类型：");
		
		combo_1 = new Combo(parent, SWT.NONE);
		GridData gd_combo_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo_1.widthHint = 85;
		combo_1.setLayoutData(gd_combo_1);
		combo_1.setItems(endTagSubType);
		
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if(combo.getText().equals(EndTagType.YOU_JING.getValue())) {//油井
					int len = EndTagSubType.values().length;
					endTagSubType = new String[len+1];
					endTagSubType[0] = "";
					for(int i = 1;i<=len;i++) {
						endTagSubType[i] = EndTagSubType.values()[i-1].getValue();
					}
				} else {
					endTagSubType = new String[1];
					endTagSubType[0] = "";
				}
				combo_1.setItems(endTagSubType);
			}
		});
		
		TableViewer tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		GridData gd_table = new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1);
		gd_table.heightHint = 300;
		gd_table.widthHint = 220;
		table.setLayoutData(gd_table);
		
		Button btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(endTag.getId() == null) {//新建
					if("".equals(text_name.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误", "监控对象名字不能为空！");
						return;
					}
					
					endTag.setName(text_name.getText().trim());
					endTag.setType(EndTagType.getByValue(combo.getText())==null?null:EndTagType.getByValue(combo.getText()).toString());
					endTag.setSubType((EndTagSubType.getByValue(combo_1.getText().trim())==null)?combo_1.getText().trim():EndTagSubType.getByValue(combo_1.getText().trim()).toString());
					
					endTagService.create(endTag);
					
					ScadaObjectTreeView.treeViewer.add(endTag.getMajorTag(), endTag);
					ScadaObjectTreeView.treeViewer.setExpandedState(endTag.getMajorTag(), true);
					
				} else {//编辑
					if("".equals(text_name.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误", "监控对象名字不能为空！");
						return;
					}
					
					endTag.setName(text_name.getText().trim());
					endTag.setType(EndTagType.getByValue(combo.getText())==null?null:EndTagType.getByValue(combo.getText()).toString());
					endTag.setSubType((EndTagSubType.getByValue(combo_1.getText().trim())==null)?combo_1.getText().trim():EndTagSubType.getByValue(combo_1.getText().trim()).toString());
					
					endTagService.update(endTag);
					
					ScadaObjectTreeView.treeViewer.update(endTag, null);
				}
				
				LayoutUtil.hideViewPart();
			}
		});
		btnNewButton.setText(" 保  存 ");
		
		Button btnNewButton_1 = new Button(parent, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IWorkbenchPart part = page.getActivePart();
				if (part instanceof IViewPart)
					page.hideView((IViewPart) part);
			}
		});
		btnNewButton_1.setText(" 取  消 ");
		
		ViewPropertyChange.getInstance().addPropertyChangeListener("endtag", this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty().equals(FirePropertyConstants.ENDTAG_ADD)) {
			endTag = new EndTag();
			Object object = event.getNewValue();
			if(object instanceof MajorTag) {
				endTag.setMajorTag((MajorTag)object);
			}
			
			//初始化控件值
			text_name.setText("");
			combo.select(0);
			combo_1.select(0);
			
		} else if(event.getProperty().equals(FirePropertyConstants.ENDTAG_EDIT)) {
			endTag = (EndTag)event.getNewValue();
			
			//初始化控件值
			text_name.setText(endTag.getName());
			
			if(endTag.getType() != null) {
				combo.setText(EndTagType.valueOf(endTag.getType()).getValue());
			} else {
				combo.select(0);
			}
			if(endTag.getSubType() != null) {
				EndTagSubType subType;
				try {
					subType = EndTagSubType.valueOf(endTag.getSubType());
				} catch (Exception e) {
					subType = null;
//					e.printStackTrace();
				}
				
				combo_1.setText(subType==null?endTag.getSubType():subType.getValue());
			} else {
				combo_1.select(0);
			}
			
		}
	}

	@Override
	public void dispose() {
		ViewPropertyChange.getInstance().removePropertyChangeListener("endtag");
		super.dispose();
	}
	
	
}
