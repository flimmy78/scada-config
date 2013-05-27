package com.ht.scada.config.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.service.MajorTagService;
import com.ht.scada.common.tag.type.entity.MajorTagType;
import com.ht.scada.common.tag.type.service.TypeService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.LayoutUtil;
import com.ht.scada.config.util.ViewPropertyChange;
import com.ht.scada.config.view.tree.RootTreeModel;

public class MainIndexView extends ViewPart implements IPropertyChangeListener {
	
	private static final Logger log = LoggerFactory.getLogger(MainIndexView.class);
	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		public String getText(Object element) {
			return ((MajorTagType)element).getValue();
		}
	}
	
	private TypeService typeService = (TypeService) Activator.getDefault()
			.getApplicationContext().getBean("typeService");

	public MainIndexView() {
		MajorTagType first = new MajorTagType();
		first.setName(null);
		first.setValue("");
		first.setLevel(-1);
		majorTagTypeList.add(first);
		
		List<MajorTagType> dataList = typeService.getAllMajorTagType();
		if(dataList != null && !dataList.isEmpty()) {
			majorTagTypeList.addAll(dataList);
		}
		log.info(String.valueOf(majorTagTypeList.size()));
		
	}

	public static final String ID = "com.ht.scada.config.view.MainIndexView";
	private Text text;
	private MajorTag majorTag;
	private List<MajorTagType> majorTagTypeList = new ArrayList<MajorTagType>();

	private MajorTagService majorTagService = (MajorTagService) Activator.getDefault()
			.getApplicationContext().getBean("majorTagService");
	private ComboViewer comboViewer;

	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.verticalSpacing = 20;
		gl_parent.marginTop = 25;
		gl_parent.marginLeft = 40;
		parent.setLayout(gl_parent);

		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("名称：");

		text = new Text(parent, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text.widthHint = 100;
		text.setLayoutData(gd_text);

		Label label_1 = new Label(parent, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("类型：");

		comboViewer = new ComboViewer(parent, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 90;
		combo.setLayoutData(gd_combo);
		comboViewer.setLabelProvider(new ViewerLabelProvider());
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setInput(majorTagTypeList);
		

		Button btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (majorTag.getId() == null) {// 新建
					if ("".equals(text.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误", "索引名字不能为空！");
						return;
					}

					majorTag.setName(text.getText().trim());
					majorTag.setType(((MajorTagType)((IStructuredSelection)comboViewer.getSelection()).getFirstElement()).getName());

					majorTagService.create(majorTag);

					Object parentObject;
					if (majorTag.getParent() == null) {
						parentObject = RootTreeModel.instanse.labelIndex;
					} else {
						parentObject = majorTag.getParent();
					}

					ScadaObjectTreeView.treeViewer.add(parentObject, majorTag);
					ScadaObjectTreeView.treeViewer.setExpandedState(parentObject, true);

				} else {// 编辑
					if ("".equals(text.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误", "索引名字不能为空！");
						return;
					}
					majorTag = majorTagService.getById(majorTag.getId());
					
					majorTag.setName(text.getText().trim());
					majorTag.setType(((MajorTagType)((IStructuredSelection)comboViewer.getSelection()).getFirstElement()).getName());

					majorTagService.update(majorTag);

					ScadaObjectTreeView.treeViewer.update(majorTag, null);

				}

				LayoutUtil.hideViewPart();
			}
		});
		btnNewButton.setText(" 保  存 ");

		Button btnNewButton_1 = new Button(parent, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LayoutUtil.hideViewPart();
			}
		});
		btnNewButton_1.setText(" 取  消 ");

		ViewPropertyChange.getInstance().addPropertyChangeListener("main", this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(FirePropertyConstants.MAJOR_ADD)) {
			majorTag = new MajorTag();
			Object object = event.getNewValue();
			if (object instanceof MajorTag) {
				majorTag.setParent((MajorTag) object);
			} else {
				majorTag.setParent(null);
			}
			
			// 初始化控件值
			text.setText("");
		} else if (event.getProperty().equals(FirePropertyConstants.MAJOR_EDIT)) {
			majorTag = (MajorTag) event.getNewValue();

			// 初始化控件值
			text.setText(majorTag.getName());

			if (majorTag.getType() != null) {
				for(MajorTagType type : majorTagTypeList) {
					if(type.getName() != null && type.getName().equals(majorTag.getType())) {
						comboViewer.getCombo().setText(type.getValue());
						break;
					}
					
				}
//				comboViewer.setText(MajorTagType.valueOf(majorTag.getType()).getValue());
			} else {
				comboViewer.getCombo().select(0);
			}

		}
	}

	@Override
	public void dispose() {
		ViewPropertyChange.getInstance().removePropertyChangeListener("main");
		super.dispose();
	}

}
