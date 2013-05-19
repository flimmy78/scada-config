package com.ht.scada.config.window;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.ht.scada.common.tag.entity.VarGroupCfg;
import com.ht.scada.common.tag.type.EndTagSubType;
import com.ht.scada.common.tag.type.EndTagType;
import com.ht.scada.common.tag.type.MajorTagType;
import com.ht.scada.common.tag.type.VarType;
import com.ht.scada.common.tag.type.service.TypeService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.LayoutUtil;

public class ProjectInitWindow extends ApplicationWindow {

	
	private TypeService typeService = (TypeService) Activator.getDefault()
			.getApplicationContext().getBean("typeService");

	public ProjectInitWindow(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create contents of the window.
	 */
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_shell = new GridLayout(2, false);
		gl_shell.marginTop = 20;
		gl_shell.marginLeft = 50;
		container.setLayout(gl_shell);

		LayoutUtil.centerShell(Display.getCurrent(), container.getShell());

		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setBounds(21, 36, 78, 17);
		label.setText("工程类型：");

		Combo combo = new Combo(container, SWT.NONE);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_combo.widthHint = 85;
		combo.setLayoutData(gd_combo);
		combo.setBounds(105, 33, 153, 25);
		combo.setItems(new String[] { "数字化油田", "配电自动化", "电气火灾", "能源管理" });
		combo.select(0);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Button button = new Button(container, SWT.NONE);
		GridData gd_button = new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1);
		gd_button.widthHint = 80;
		button.setLayoutData(gd_button);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				initDataBase();
			}
		});
		button.setBounds(178, 228, 80, 27);
		button.setText("系统初始化");

		return container;
	}

	@Override
	protected void configureShell(Shell shell) {
		shell.setText("工程初始化");
		shell.setSize(370, 210);
		super.configureShell(shell);
	}

	@Override
	protected void initializeBounds() {
		// TODO Auto-generated method stub
		super.initializeBounds();
	}

	private void initDataBase() {
		
		List<MajorTagType> majorTagTypeList = new ArrayList<MajorTagType>();
		List<EndTagType> endTagTypeList = new ArrayList<EndTagType>();
		List<EndTagSubType> endTagSubTypeList = new ArrayList<EndTagSubType>();
		List<VarGroupCfg> varGroupCfgList = new ArrayList<VarGroupCfg>();
		List<VarType> varTypeList = new ArrayList<VarType>();

		MajorTagType majorTagType = new MajorTagType();
		majorTagType.setName("CHANG_LEVEL");
		majorTagType.setValue("厂");
		
		MajorTagType majorTagType1 = new MajorTagType();
		majorTagType1.setName("KUANG_LEVEL");
		majorTagType1.setValue("矿");
		
		MajorTagType majorTagType2 = new MajorTagType();
		majorTagType2.setName("DUI_LEVEL");
		majorTagType2.setValue("队");
		
		majorTagTypeList.add(majorTagType);
		majorTagTypeList.add(majorTagType1);
		majorTagTypeList.add(majorTagType2);

		EndTagType endTagType = new EndTagType();
		endTagType.setName("YOU_JING");
		endTagType.setValue("油井");
		
		EndTagType endTagType1 = new EndTagType();
		endTagType1.setName("SHUI_YUAN_JING");
		endTagType1.setValue("水源井");
		
		EndTagType endTagType2 = new EndTagType();
		endTagType2.setName("ZHU_SHUI_JING");
		endTagType2.setValue("注水井");
		
		EndTagType endTagType3 = new EndTagType();
		endTagType3.setName("ZENG_YA_ZHAN");
		endTagType3.setValue("增压站");
		
		EndTagType endTagType4 = new EndTagType();
		endTagType4.setName("ZHU_QI_ZHAN");
		endTagType4.setValue("注汽站");
		
		EndTagType endTagType5 = new EndTagType();
		endTagType5.setName("LIAN_HE_ZHAN");
		endTagType5.setValue("联合站");
		
		EndTagType endTagType6 = new EndTagType();
		endTagType6.setName("JIE_ZHUAN_ZHAN");
		endTagType6.setValue("接转站");
		
		EndTagType endTagType7 = new EndTagType();
		endTagType7.setName("ZHU_SHUI_ZHAN");
		endTagType7.setValue("注水站");
		
		EndTagType endTagType8 = new EndTagType();
		endTagType8.setName("JI_LIANG_ZHAN");
		endTagType8.setValue("计量站");
		
		EndTagType endTagType9 = new EndTagType();
		endTagType9.setName("PEI_SHUI_JIAN");
		endTagType9.setValue("配水间");
		
		EndTagType endTagType10 = new EndTagType();
		endTagType10.setName("JI_LIANG_CHE");
		endTagType10.setValue("计量车");
		
		endTagTypeList.add(endTagType);
		endTagTypeList.add(endTagType1);
		endTagTypeList.add(endTagType2);
		endTagTypeList.add(endTagType3);
		endTagTypeList.add(endTagType4);
		endTagTypeList.add(endTagType5);
		endTagTypeList.add(endTagType6);
		endTagTypeList.add(endTagType7);
		endTagTypeList.add(endTagType8);
		endTagTypeList.add(endTagType9);
		endTagTypeList.add(endTagType10);

		EndTagSubType endTagSubType = new EndTagSubType();
		endTagSubType.setEndTagType(endTagType);
		endTagSubType.setName("YOU_LIANG_SHI");
		endTagSubType.setValue("油梁式");
		
		EndTagSubType endTagSubType1 = new EndTagSubType();
		endTagSubType1.setEndTagType(endTagType);
		endTagSubType1.setName("DIAN_GUN_TONG");
		endTagSubType1.setValue("电滚筒");
		
		EndTagSubType endTagSubType2 = new EndTagSubType();
		endTagSubType2.setEndTagType(endTagType);
		endTagSubType2.setName("GAO_YUAN_JI");
		endTagSubType2.setValue("高原机");
		
		EndTagSubType endTagSubType3 = new EndTagSubType();
		endTagSubType3.setEndTagType(endTagType);
		endTagSubType3.setName("LUO_GAN_BENG");
		endTagSubType3.setValue("螺杆泵");
		
		EndTagSubType endTagSubType4 = new EndTagSubType();
		endTagSubType4.setEndTagType(endTagType);
		endTagSubType4.setName("DIAN_QIAN_BENG");
		endTagSubType4.setValue("电潜泵");
		
		endTagSubTypeList.add(endTagSubType);
		endTagSubTypeList.add(endTagSubType1);
		endTagSubTypeList.add(endTagSubType2);
		endTagSubTypeList.add(endTagSubType3);
		endTagSubTypeList.add(endTagSubType4);
		
		VarGroupCfg varGroupCfg = new VarGroupCfg();
		varGroupCfg.setName("DIAN_YC");
		varGroupCfg.setValue("电力数据");
		
		VarGroupCfg varGroupCfg1 = new VarGroupCfg();
		varGroupCfg1.setName("DIAN_YM");
		varGroupCfg1.setValue("电能数据");
		
		VarGroupCfg varGroupCfg2 = new VarGroupCfg();
		varGroupCfg2.setName("DIAN_XB");
		varGroupCfg2.setValue("谐波");
		
		VarGroupCfg varGroupCfg3 = new VarGroupCfg();
		varGroupCfg3.setName("YOU_JING");
		varGroupCfg3.setValue("油井");
		
		VarGroupCfg varGroupCfg4 = new VarGroupCfg();
		varGroupCfg4.setName("YOU_JING_SGT");
		varGroupCfg4.setValue("示功图");
		
		VarGroupCfg varGroupCfg5 = new VarGroupCfg();
		varGroupCfg5.setName("YOU_JING_DGT");
		varGroupCfg5.setValue("电功图");
		
		VarGroupCfg varGroupCfg6 = new VarGroupCfg();
		varGroupCfg6.setName("SHUI_JING");
		varGroupCfg6.setValue("水井");
		
		VarGroupCfg varGroupCfg7 = new VarGroupCfg();
		varGroupCfg7.setName("JI_LIANG");
		varGroupCfg7.setValue("计量车");
		
		VarGroupCfg varGroupCfg8 = new VarGroupCfg();
		varGroupCfg8.setName("ZHU_CAI");
		varGroupCfg8.setValue("注采");
		
		varGroupCfgList.add(varGroupCfg);
		varGroupCfgList.add(varGroupCfg1);
		varGroupCfgList.add(varGroupCfg2);
		varGroupCfgList.add(varGroupCfg3);
		varGroupCfgList.add(varGroupCfg4);
		varGroupCfgList.add(varGroupCfg5);
		varGroupCfgList.add(varGroupCfg6);
		varGroupCfgList.add(varGroupCfg7);
		varGroupCfgList.add(varGroupCfg8);
		
		Set<VarGroupCfg> varGroupCfgSet = new HashSet<VarGroupCfg>();
		varGroupCfgSet.add(varGroupCfg);
		varGroupCfgSet.add(varGroupCfg1);
		varGroupCfgSet.add(varGroupCfg2);
		varGroupCfgSet.add(varGroupCfg3);
		varGroupCfgSet.add(varGroupCfg4);
		varGroupCfgSet.add(varGroupCfg5);
		varGroupCfgSet.add(varGroupCfg6);
		varGroupCfgSet.add(varGroupCfg7);
		varGroupCfgSet.add(varGroupCfg8);
		
		endTagType.setVarGroupCfgSet(varGroupCfgSet);
		
		VarType varType = new VarType();
		varType.setName("YC");
		varType.setValue("遥测");
		
		VarType varType1 = new VarType();
		varType1.setName("YX");
		varType1.setValue("遥信");
		
		VarType varType2 = new VarType();
		varType2.setName("YM");
		varType2.setValue("遥脉");
		
		VarType varType3 = new VarType();
		varType3.setName("YK");
		varType3.setValue("遥控");
		
		VarType varType4 = new VarType();
		varType4.setName("YT");
		varType4.setValue("遥调");
		
		VarType varType5 = new VarType();
		varType5.setName("QT");
		varType5.setValue("其他");
		
		varTypeList.add(varType);
		varTypeList.add(varType1);
		varTypeList.add(varType2);
		varTypeList.add(varType3);
		varTypeList.add(varType4);
		varTypeList.add(varType5);

		typeService.insertMajorTagType(majorTagTypeList);
		typeService.insertEndTagType(endTagTypeList);
		typeService.insertEndTagSubType(endTagSubTypeList);
		typeService.insertVarGroupCfg(varGroupCfgList);
		typeService.insertVarType(varTypeList);

	}

}
