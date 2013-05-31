package com.ht.scada.config.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;

public class StringModifySettingsDialog extends Dialog {

	private StringModifyTypeModel model;
	private Text text;
	private Button button_3;
	private Button button_2;
	private Button button_1;
	private Label label_1;
	private Text text_base;
	private Group group;
	private Text start;
	private Text end;
	private Button button_fanwei;
	private int num;	//数据个数
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public StringModifySettingsDialog(Shell parentShell, StringModifyTypeModel model, int num) {
		super(parentShell);
		this.model = model;
		this.num = num;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("值改变方式");
		super.configureShell(newShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);
		
		group = new Group(container, SWT.NONE);
		group.setText("改变方式");
		group.setBounds(7, 34, 304, 62);
		
		button_1 = new Button(group, SWT.RADIO);
		button_1.setBounds(10, 26, 54, 16);
		button_1.setSelection(true);
		button_1.setText("相同值");
		
		button_2 = new Button(group, SWT.RADIO);
		button_2.setBounds(70, 26, 45, 16);
		button_2.setText("递增");
		
		button_3 = new Button(group, SWT.RADIO);
		button_3.setBounds(118, 26, 45, 16);
		button_3.setText("递减");
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(181, 26, 34, 26);
		label.setAlignment(SWT.RIGHT);
		label.setText("间隔");
		
		text = new Text(group, SWT.BORDER);
		text.setBounds(221, 25, 70, 18);
		text.setText("1");
		
		label_1 = new Label(container, SWT.NONE);
		label_1.setBounds(10, 10, 34, 20);
		label_1.setText("基值");
		
		text_base = new Text(container, SWT.BORDER);
		text_base.setToolTipText("16进制数请加前缀0x或0X");
		text_base.setText("1");
		text_base.setBounds(50, 9, 44, 18);
		
		button_fanwei = new Button(container, SWT.CHECK);
		button_fanwei.setText("范围：");
		button_fanwei.setBounds(119, 12, 50, 16);
		
		start = new Text(container, SWT.BORDER);
		start.setBounds(175, 10, 44, 18);
		
		end = new Text(container, SWT.BORDER);
		end.setBounds(238, 10, 44, 18);
		
		Label label_2 = new Label(container, SWT.NONE);
		label_2.setBounds(223, 13, 17, 12);
		label_2.setText("—");
		
		initValues();

		return container;
	}

	/**
	 * 
	 */
	private void initValues() {
		text_base.setText(this.model.getBase());
		text.setText(Integer.toString(this.model.getInterval()));
		if (model.getType() == 0) {
			this.button_1.setSelection(true);
		} else if (model.getType() > 0) {
			this.button_2.setSelection(true);
		} else {
			this.button_3.setSelection(true);
		}
	}

	@Override
	protected void okPressed() {
		if (button_1.getSelection()) {
			this.model.setType(0);// 不变
		} else if (button_2.getSelection()) {
			this.model.setType(1);// 递增
		} else {
			this.model.setType(-1);// 递减
		}
		
		try {
			String s = text_base.getText().trim();
//			if (s.startsWith("0x") || s.startsWith("0X")) {
//				model.setBase(Integer.parseInt(s.substring(2), 16));
//			} else {
//				model.setBase(Integer.parseInt(s));
//			}
			model.setBase(s);
		} catch (NumberFormatException e1) {
			MessageDialog.openWarning(null, "警告", "请输入正确的基值.\n");
			return;
		}
		
		if (this.model.getType() != 0) {
			try {
				int interval = Integer.parseInt(this.text.getText().trim());
				this.model.setInterval(interval);
			} catch (NumberFormatException e) {
				MessageDialog.openWarning(null, "警告", "请输入正确的间隔！");
				return;
			}
		}
		
		if(button_fanwei.getSelection()) {
			
			
			if("".equals(start.getText().trim()) || "".equals(end.getText().trim())) {
				MessageDialog.openWarning(null, "警告", "范围不能为空！");
				return;
			}
			try {
				int startValue = Integer.parseInt(start.getText().trim());
				int endValue = Integer.parseInt(end.getText().trim());
				if(endValue<startValue) {
					MessageDialog.openWarning(null, "警告", "范围结束值不能小于起始值！");
					return;
				}
				if(startValue < 1 || endValue < 1) {
					MessageDialog.openWarning(null, "警告", "范围值不能小于1！");
					return;
				}
				if(startValue > num || endValue > num) {
					MessageDialog.openWarning(null, "警告", "范围值不能超过" + num + "！");
					return;
				}
				
				model.setFanwei(true);
				model.setStart(startValue);
				model.setEnd(endValue);
			} catch (NumberFormatException e) {
				MessageDialog.openWarning(null, "警告", "请输入正确的范围！");
				return;
			}
			
		}
		
		
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(325, 172);
	}
}
