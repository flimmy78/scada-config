package com.ht.scada.config.view;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.service.TagService;
import com.ht.scada.common.tag.well.consts.EndTagType;
import com.ht.scada.common.tag.well.consts.MajorTagType;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ViewPropertyChange;
import com.ht.scada.config.view.tree.RootTreeModel;
import org.eclipse.swt.widgets.Combo;

public class AreaIndexView extends ViewPart implements IPropertyChangeListener {

	public AreaIndexView() {
		int lenght = MajorTagType.values().length;
		majorTagTypeArray = new String[lenght + 1];
		majorTagTypeArray[0] = "";
		for (int i = 1; i <= lenght; i++) {
			majorTagTypeArray[i] = MajorTagType.values()[i - 1].getValue();
		}

	}

	public static final String ID = "com.ht.scada.config.view.AreaIndexView";
	private Text textIndex;
	private MajorTag majorTag;
	private String[] majorTagTypeArray;

	private TagService tagService = (TagService) Activator.getDefault().getApplicationContext().getBean("tagService");
	private Text textType;

	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.verticalSpacing = 20;
		gl_parent.marginTop = 25;
		gl_parent.marginLeft = 40;
		parent.setLayout(gl_parent);

		Label labIndex = new Label(parent, SWT.NONE);
		labIndex.setText("索引名：");

		textIndex = new Text(parent, SWT.BORDER);
		GridData gd_textIndex = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textIndex.widthHint = 100;
		textIndex.setLayoutData(gd_textIndex);

		Label labType = new Label(parent, SWT.NONE);
		labType.setText("类型：");
		
		textType = new Text(parent, SWT.BORDER);
		GridData gd_textType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textType.widthHint = 100;
		textType.setLayoutData(gd_textType);

		Button btnSave = new Button(parent, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (majorTag.getId() == null) {// 新建
					if ("".equals(textIndex.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误", "索引名字不能为空！");
						return;
					}

					majorTag.setName(textIndex.getText().trim());
//					majorTag.setType(MajorTagType.getByValue(comboType.getText()) == null ? null : MajorTagType.getByValue(
//							comboType.getText()).toString());

					tagService.createMajorTag(majorTag);

					Object parentObject;
					if (majorTag.getParent() == null) {
						parentObject = RootTreeModel.instanse.labelIndex;
					} else {
						parentObject = majorTag.getParent();
					}

					ScadaObjectTreeView.treeViewer.add(parentObject, majorTag);
					ScadaObjectTreeView.treeViewer.setExpandedState(parentObject, true);

				} else {// 编辑
					if ("".equals(textIndex.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误", "索引名字不能为空！");
						return;
					}
					majorTag.setName(textIndex.getText().trim());
//					majorTag.setType(MajorTagType.getByValue(comboType.getText()) == null ? null : MajorTagType.getByValue(
//							comboType.getText()).toString());

					tagService.updateMajorTag(majorTag);

					ScadaObjectTreeView.treeViewer.update(majorTag, null);

				}

				// ScadaObjectTreeView.treeViewer.refresh();

				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IWorkbenchPart part = page.getActivePart();
				if (part instanceof IViewPart)
					page.hideView((IViewPart) part);
			}
		});
		btnSave.setText(" 保  存 ");

		Button btnCancel = new Button(parent, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IWorkbenchPart part = page.getActivePart();
				if (part instanceof IViewPart)
					page.hideView((IViewPart) part);
			}
		});
		btnCancel.setText(" 取  消 ");

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
		} else if (event.getProperty().equals(FirePropertyConstants.MAJOR_EDIT)) {
			majorTag = (MajorTag) event.getNewValue();

			// 初始化控件值
			textIndex.setText(majorTag.getName());

//			if (majorTag.getType() != null) {
//				comboType.setText(MajorTagType.valueOf(majorTag.getType()).getValue());
//			} else {
//				comboType.select(0);
//			}

		}
	}

	@Override
	public void dispose() {
		ViewPropertyChange.getInstance().removePropertyChangeListener("main");
		super.dispose();
	}

}
