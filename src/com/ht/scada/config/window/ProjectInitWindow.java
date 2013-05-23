package com.ht.scada.config.window;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
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
import com.ht.scada.common.tag.type.entity.EndTagExtInfoName;
import com.ht.scada.common.tag.type.entity.EndTagExtInfoValue;
import com.ht.scada.common.tag.type.entity.EndTagSubType;
import com.ht.scada.common.tag.type.entity.EndTagType;
import com.ht.scada.common.tag.type.entity.MajorTagType;
import com.ht.scada.common.tag.type.entity.VarSubType;
import com.ht.scada.common.tag.type.entity.VarType;
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
				typeService.deleteAllType();
				initDataBase();
				MessageDialog.openInformation(getShell(), "提示", "数据初始成功！");
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
		List<VarSubType> varSubTypeList = new ArrayList<VarSubType>();

		MajorTagType majorTagType = new MajorTagType("CHANG_LEVEL","厂");
		MajorTagType majorTagType1 = new MajorTagType("KUANG_LEVEL","矿");
		MajorTagType majorTagType2 = new MajorTagType("DUI_LEVEL","队");
		
		majorTagTypeList.add(majorTagType);
		majorTagTypeList.add(majorTagType1);
		majorTagTypeList.add(majorTagType2);

		EndTagType endTagType = new EndTagType("YOU_JING","油井");
		EndTagType endTagType1 = new EndTagType("SHUI_YUAN_JING","水源井");
		EndTagType endTagType2 = new EndTagType("ZHU_SHUI_JING","注水井");
		EndTagType endTagType3 = new EndTagType("ZENG_YA_ZHAN","增压站");
		EndTagType endTagType4 = new EndTagType("ZHU_QI_ZHAN","注汽站");
		EndTagType endTagType5 = new EndTagType("LIAN_HE_ZHAN","联合站");
		EndTagType endTagType6 = new EndTagType("JIE_ZHUAN_ZHAN","接转站");
		EndTagType endTagType7 = new EndTagType("ZHU_SHUI_ZHAN","注水站");
		EndTagType endTagType8 = new EndTagType("JI_LIANG_ZHAN","计量站");
		EndTagType endTagType9 = new EndTagType("PEI_SHUI_JIAN","配水间");
		EndTagType endTagType10 = new EndTagType("JI_LIANG_CHE","计量车");
		
		
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

		EndTagSubType endTagSubType = new EndTagSubType("YOU_LIANG_SHI","油梁式");
		endTagSubType.setEndTagType(endTagType);
		EndTagSubType endTagSubType1 = new EndTagSubType("DIAN_GUN_TONG","电滚筒");
		endTagSubType1.setEndTagType(endTagType);
		EndTagSubType endTagSubType2 = new EndTagSubType("GAO_YUAN_JI","高原机");
		endTagSubType2.setEndTagType(endTagType);
		EndTagSubType endTagSubType3 = new EndTagSubType("LUO_GAN_BENG","螺杆泵");
		endTagSubType3.setEndTagType(endTagType);
		EndTagSubType endTagSubType4 = new EndTagSubType("DIAN_QIAN_BENG","电潜泵");
		endTagSubType4.setEndTagType(endTagType);
		
		endTagSubTypeList.add(endTagSubType);
		endTagSubTypeList.add(endTagSubType1);
		endTagSubTypeList.add(endTagSubType2);
		endTagSubTypeList.add(endTagSubType3);
		endTagSubTypeList.add(endTagSubType4);
		
		VarGroupCfg varGroupCfgDianYC = new VarGroupCfg("DIAN_YC","电力数据");
		VarGroupCfg varGroupCfgDianYM = new VarGroupCfg("DIAN_YM","电能数据");
		VarGroupCfg varGroupCfgDianXB = new VarGroupCfg("DIAN_XB","谐波");
		VarGroupCfg varGroupCfgYouJing = new VarGroupCfg("YOU_JING","油井");
		VarGroupCfg varGroupCfgSGT = new VarGroupCfg("YOU_JING_SGT","示功图");
		VarGroupCfg varGroupCfgDGT = new VarGroupCfg("YOU_JING_DGT","电功图");
		VarGroupCfg varGroupCfgShuiJing = new VarGroupCfg("SHUI_JING","水井");
		VarGroupCfg varGroupCfgJiLiangChe = new VarGroupCfg("JI_LIANG","计量车");
		VarGroupCfg varGroupCfgZhuCai = new VarGroupCfg("ZHU_CAI","注采");
		VarGroupCfg varGroupCfgRTUStatus = new VarGroupCfg("RTU_ZHUANG_TAI", "RTU状态");
		VarGroupCfg varGroupCfgSensorRun = new VarGroupCfg("SENSOR_RUN", "传感器运行");
		VarGroupCfg varGroupCfgZYZYC = new VarGroupCfg("ZYZ_YC", "增压站遥测量");
		VarGroupCfg varGroupCfgZSZYC = new VarGroupCfg("ZSZ_YC", "注水站遥测量");
		VarGroupCfg varGroupCfgJZZYC = new VarGroupCfg("JZZ_YC", "接转站遥测量");
		VarGroupCfg varGroupCfgLHZYC = new VarGroupCfg("LHZ_YC", "联合站遥测量");
		
		varGroupCfgList.add(varGroupCfgDianYC);
		varGroupCfgList.add(varGroupCfgDianYM);
		varGroupCfgList.add(varGroupCfgDianXB);
		varGroupCfgList.add(varGroupCfgYouJing);
		varGroupCfgList.add(varGroupCfgSGT);
		varGroupCfgList.add(varGroupCfgDGT);
		varGroupCfgList.add(varGroupCfgShuiJing);
		varGroupCfgList.add(varGroupCfgJiLiangChe);
		varGroupCfgList.add(varGroupCfgZhuCai);
		varGroupCfgList.add(varGroupCfgRTUStatus);
		varGroupCfgList.add(varGroupCfgSensorRun);
		varGroupCfgList.add(varGroupCfgZYZYC);
		varGroupCfgList.add(varGroupCfgZSZYC);
		varGroupCfgList.add(varGroupCfgJZZYC);
		varGroupCfgList.add(varGroupCfgLHZYC);
		
		Set<VarGroupCfg> varGroupCfgSet = new HashSet<VarGroupCfg>();
		varGroupCfgSet.add(varGroupCfgDianYC);
		varGroupCfgSet.add(varGroupCfgDianYM);
		varGroupCfgSet.add(varGroupCfgDianXB);
		varGroupCfgSet.add(varGroupCfgYouJing);
		varGroupCfgSet.add(varGroupCfgSGT);
		varGroupCfgSet.add(varGroupCfgDGT);
		varGroupCfgSet.add(varGroupCfgShuiJing);
		varGroupCfgSet.add(varGroupCfgJiLiangChe);
		varGroupCfgSet.add(varGroupCfgZhuCai);
		varGroupCfgSet.add(varGroupCfgRTUStatus);
		
		endTagType.setVarGroupCfgSet(varGroupCfgSet);
		
		VarType varTypeYC = new VarType("YC","遥测");
		VarType varTypeYX = new VarType("YX","遥信");
		VarType varTypeYM = new VarType("YM","遥脉");
		VarType varTypeYK = new VarType("YK","遥控");
		VarType varTypeYT = new VarType("YT","遥调");
		VarType varTypeQT = new VarType("QT","其他");
		
		varTypeList.add(varTypeYC);
		varTypeList.add(varTypeYX);
		varTypeList.add(varTypeYM);
		varTypeList.add(varTypeYK);
		varTypeList.add(varTypeYT);
		varTypeList.add(varTypeQT);
		
		VarSubType varSubType = new VarSubType("YOU_YA","油压",varGroupCfgYouJing,varTypeYC);
		VarSubType varSubType1 = new VarSubType("TAO_YA","套压",varGroupCfgYouJing,varTypeYC);
		VarSubType varSubType2 = new VarSubType("HUI_YA","回压",varGroupCfgYouJing,varTypeYC);
		VarSubType varSubType3 = new VarSubType("JING_KOU_WEN_DU","井口温度",varGroupCfgYouJing,varTypeYC);
		VarSubType varSubType4 = new VarSubType("HUI_GUAN_WEN_DU","汇管温度",varGroupCfgYouJing,varTypeYC);
		VarSubType varSubType5 = new VarSubType("QI_TING_ZHUANG_TAI","启停状态",varGroupCfgYouJing,varTypeYX);
		varSubType5.setRemark("0为停，1为启（运行）");
		
		VarSubType varSubType6 = new VarSubType("CHONG_CHENG","冲程",varGroupCfgSGT,varTypeYC);
		VarSubType varSubType7 = new VarSubType("CHONG_CI","冲次",varGroupCfgSGT,varTypeYC);
		VarSubType varSubType8 = new VarSubType("SHANG_XING_CHONG_CI","上行冲次",varGroupCfgSGT,varTypeYC);
		VarSubType varSubType9 = new VarSubType("XIA_XING_CHONG_CI","下行冲次",varGroupCfgSGT,varTypeYC);
		VarSubType varSubType10 = new VarSubType("ZUI_DA_ZAI_HE","最大载荷",varGroupCfgSGT,varTypeYC);
		VarSubType varSubType11 = new VarSubType("ZUI_XIAO_ZAI_HE","最小载荷",varGroupCfgSGT,varTypeYC);
		VarSubType varSubType12 = new VarSubType("WEI_YI_ARRAY","位移数组",varGroupCfgSGT,varTypeQT);
		VarSubType varSubType13 = new VarSubType("ZAI_HE_ARRAY","载荷数组",varGroupCfgSGT,varTypeQT);
		
		VarSubType varSubType14 = new VarSubType("DIAN_LIU_ARRAY","电流数组",varGroupCfgDGT,varTypeQT);
		VarSubType varSubType15 = new VarSubType("GONG_LV_ARRAY","功率数组",varGroupCfgDGT,varTypeQT);
		VarSubType varSubType16 = new VarSubType("GONG_LV_YIN_SHU_ARRAY","功率因数数组",varGroupCfgDGT,varTypeQT);
		VarSubType varSubType17 = new VarSubType("DIAN_GONG_TU_ARRAY","电功图数组",varGroupCfgDGT,varTypeQT);
		
		VarSubType varSubType18 = new VarSubType("I_A","A相电流",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType19 = new VarSubType("I_B","B相电流",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType20 = new VarSubType("I_C","C相电流",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType21 = new VarSubType("U_A","A相电压",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType22 = new VarSubType("U_B","B相电压",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType23 = new VarSubType("U_C","C相电压",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType24 = new VarSubType("I_3XBPH","3相不平衡电流",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType25 = new VarSubType("U_AB","AB线电压",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType26 = new VarSubType("U_BC","BC线电压",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType27 = new VarSubType("U_CA","CA线电压",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType28 = new VarSubType("GV_YG","有功功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType29 = new VarSubType("GV_WG","无功功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType30 = new VarSubType("GV_SZ","视在功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType31 = new VarSubType("GV_ZB","周波",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType32 = new VarSubType("GV_YG_A","A相有功功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType33 = new VarSubType("GV_YG_B","B相有功功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType34 = new VarSubType("GV_YG_C","C相有功功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType35 = new VarSubType("GV_WG_A","A相无功功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType36 = new VarSubType("GV_WG_B","B相无功功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType37 = new VarSubType("GV_WG_C","C相无功功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType38 = new VarSubType("GV_SZ_A","A相视在功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType39 = new VarSubType("GV_SZ_B","B相视在功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType40 = new VarSubType("GV_SZ_C","C相视在功率",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType41 = new VarSubType("GV_GLYS_A","A相功率因数",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType42 = new VarSubType("GV_GLYS_B","B相功率因数",varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType43 = new VarSubType("GV_GLYS_C","C相功率因数",varGroupCfgDianYC,varTypeYC);
		
		VarSubType varSubType44 = new VarSubType("DL_ZX_Z","正向有功总电能",varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType45 = new VarSubType("DL_ZX_J","正向有功尖时电能",varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType46 = new VarSubType("DL_ZX_F","正向有功峰时电能",varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType47 = new VarSubType("DL_ZX_P","正向有功平时电能",varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType48 = new VarSubType("DL_ZX_G","正向有功谷时电能",varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType49 = new VarSubType("DL_FX_Z","反向有功总电能",varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType50 = new VarSubType("DL_FX_J","反向有功尖时电能",varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType51 = new VarSubType("DL_FX_F","反向有功峰时电能",varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType52 = new VarSubType("DL_FX_P","反向有功平时电能",varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType53 = new VarSubType("DL_FX_G","反向有功谷时电能",varGroupCfgDianYM,varTypeYM);
		
		VarSubType varSubType54 = new VarSubType("XB_IA","A相电流总谐波含量",varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType55 = new VarSubType("XB_IB","B相电流总谐波含量",varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType56 = new VarSubType("XB_IC","C相电流总谐波含量",varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType57 = new VarSubType("XB_UA","A相电压总谐波含量",varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType58 = new VarSubType("XB_UB","B相电压总谐波含量",varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType59 = new VarSubType("XB_UC","C相电压总谐波含量",varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType60 = new VarSubType("XB_IA_ARRAY","A相电流谐波数组",varGroupCfgDianXB,varTypeQT);
		VarSubType varSubType61 = new VarSubType("XB_IB_ARRAY","B相电流谐波数组",varGroupCfgDianXB,varTypeQT);
		VarSubType varSubType62 = new VarSubType("XB_IC_ARRAY","C相电流谐波数组",varGroupCfgDianXB,varTypeQT);
		VarSubType varSubType63 = new VarSubType("XB_UA_ARRAY","A相电压谐波数组",varGroupCfgDianXB,varTypeQT);
		VarSubType varSubType64 = new VarSubType("XB_UB_ARRAY","B相电压谐波数组",varGroupCfgDianXB,varTypeQT);
		VarSubType varSubType65 = new VarSubType("XB_UC_ARRAY","C相电压谐波数组",varGroupCfgDianXB,varTypeQT);
		
		VarSubType varSubType66 = new VarSubType("JLC_QL_SH","气量瞬时流量",varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType67 = new VarSubType("JLC_QL_LJ","气量累计流量",varGroupCfgJiLiangChe,varTypeYM);
		VarSubType varSubType68 = new VarSubType("JLC_YL_SH","油量瞬时流量",varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType69 = new VarSubType("JLC_YL_LJ","油量瞬时流量",varGroupCfgJiLiangChe,varTypeYM);
		VarSubType varSubType70 = new VarSubType("JLC_SL_SH","水量瞬时流量",varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType71 = new VarSubType("JLC_SL_LJ","水量瞬时流量",varGroupCfgJiLiangChe,varTypeYM);
		VarSubType varSubType72 = new VarSubType("JLC_HSL_SH","瞬时含水率",varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType73 = new VarSubType("JLC_WD_SH","温度",varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType74 = new VarSubType("JLC_YL_SH","压力",varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType75 = new VarSubType("JLC_MD_SH","密度",varGroupCfgJiLiangChe,varTypeYC);
		
		VarSubType varSubType76 = new VarSubType("ZC_ZQLL_SH","蒸汽流量瞬时值",varGroupCfgZhuCai,varTypeYC);
		VarSubType varSubType77 = new VarSubType("ZC_ZQLL_LJ","蒸汽流量累计值",varGroupCfgZhuCai,varTypeYM);
		VarSubType varSubType78 = new VarSubType("ZC_ZQYL","蒸汽压力",varGroupCfgZhuCai,varTypeYC);
		VarSubType varSubType79 = new VarSubType("ZC_ZQWD","蒸汽温度",varGroupCfgZhuCai,varTypeYC);
		VarSubType varSubType80 = new VarSubType("ZC_ZQGD","蒸汽干度",varGroupCfgZhuCai,varTypeYC);
		
		VarSubType varSubType81 = new VarSubType("RTU_RJ45_STATUS","以太网通讯状态",varGroupCfgRTUStatus,varTypeYX);
		VarSubType varSubType82 = new VarSubType("RTU_COM1_STATUS","COM1通讯状态",varGroupCfgRTUStatus,varTypeYX);
		VarSubType varSubType83 = new VarSubType("RTU_COM2_STATUS","COM2通讯状态",varGroupCfgRTUStatus,varTypeYX);
		VarSubType varSubType84 = new VarSubType("RTU_COM3_STATUS","COM3通讯状态",varGroupCfgRTUStatus,varTypeYX);
		VarSubType varSubType85 = new VarSubType("RTU_COM4_STATUS","COM4通讯状态",varGroupCfgRTUStatus,varTypeYX);
		VarSubType varSubType86 = new VarSubType("RTU_ZIGBEE_STATUS","ZigBee通讯状态",varGroupCfgRTUStatus,varTypeYX);
		
		VarSubType varSubType87 = new VarSubType("CGQ_RTU_STATUS","传感器通讯状态",varGroupCfgSensorRun,varTypeYX);
		VarSubType varSubType88 = new VarSubType("CGQ_RTU_TIME","传感器运行时间",varGroupCfgSensorRun,varTypeYC);
		VarSubType varSubType89 = new VarSubType("CGQ_REMAINED_TIME","剩余工作时间",varGroupCfgSensorRun,varTypeYC);
		VarSubType varSubType90 = new VarSubType("CGQ_REMAINED_DIANLIANG","剩余电量",varGroupCfgSensorRun,varTypeYC);
		
		varSubTypeList.add(varSubType);
		varSubTypeList.add(varSubType1);
		varSubTypeList.add(varSubType2);
		varSubTypeList.add(varSubType3);
		varSubTypeList.add(varSubType4);
		varSubTypeList.add(varSubType5);
		varSubTypeList.add(varSubType6);
		varSubTypeList.add(varSubType7);
		varSubTypeList.add(varSubType8);
		varSubTypeList.add(varSubType9);
		varSubTypeList.add(varSubType10);
		varSubTypeList.add(varSubType11);
		varSubTypeList.add(varSubType12);
		varSubTypeList.add(varSubType13);
		varSubTypeList.add(varSubType14);
		varSubTypeList.add(varSubType15);
		varSubTypeList.add(varSubType16);
		varSubTypeList.add(varSubType17);
		varSubTypeList.add(varSubType18);
		varSubTypeList.add(varSubType19);
		varSubTypeList.add(varSubType20);
		varSubTypeList.add(varSubType21);
		varSubTypeList.add(varSubType22);
		varSubTypeList.add(varSubType23);
		varSubTypeList.add(varSubType24);
		varSubTypeList.add(varSubType25);
		varSubTypeList.add(varSubType26);
		varSubTypeList.add(varSubType27);
		varSubTypeList.add(varSubType28);
		varSubTypeList.add(varSubType29);
		varSubTypeList.add(varSubType30);
		varSubTypeList.add(varSubType31);
		varSubTypeList.add(varSubType32);
		varSubTypeList.add(varSubType33);
		varSubTypeList.add(varSubType34);
		varSubTypeList.add(varSubType35);
		varSubTypeList.add(varSubType36);
		varSubTypeList.add(varSubType37);
		varSubTypeList.add(varSubType38);
		varSubTypeList.add(varSubType39);
		varSubTypeList.add(varSubType40);
		varSubTypeList.add(varSubType41);
		varSubTypeList.add(varSubType42);
		varSubTypeList.add(varSubType43);
		varSubTypeList.add(varSubType44);
		varSubTypeList.add(varSubType45);
		varSubTypeList.add(varSubType46);
		varSubTypeList.add(varSubType47);
		varSubTypeList.add(varSubType48);
		varSubTypeList.add(varSubType49);
		varSubTypeList.add(varSubType50);
		varSubTypeList.add(varSubType51);
		varSubTypeList.add(varSubType52);
		varSubTypeList.add(varSubType53);
		varSubTypeList.add(varSubType54);
		varSubTypeList.add(varSubType55);
		varSubTypeList.add(varSubType56);
		varSubTypeList.add(varSubType57);
		varSubTypeList.add(varSubType58);
		varSubTypeList.add(varSubType59);
		varSubTypeList.add(varSubType60);
		varSubTypeList.add(varSubType61);
		varSubTypeList.add(varSubType62);
		varSubTypeList.add(varSubType63);
		varSubTypeList.add(varSubType64);
		varSubTypeList.add(varSubType65);
		varSubTypeList.add(varSubType66);
		varSubTypeList.add(varSubType67);
		varSubTypeList.add(varSubType68);
		varSubTypeList.add(varSubType69);
		varSubTypeList.add(varSubType70);
		varSubTypeList.add(varSubType71);
		varSubTypeList.add(varSubType72);
		varSubTypeList.add(varSubType73);
		varSubTypeList.add(varSubType74);
		varSubTypeList.add(varSubType75);
		varSubTypeList.add(varSubType76);
		varSubTypeList.add(varSubType77);
		varSubTypeList.add(varSubType78);
		varSubTypeList.add(varSubType79);
		varSubTypeList.add(varSubType80);
		varSubTypeList.add(varSubType81);
		varSubTypeList.add(varSubType82);
		varSubTypeList.add(varSubType83);
		varSubTypeList.add(varSubType84);
		varSubTypeList.add(varSubType85);
		varSubTypeList.add(varSubType86);
		varSubTypeList.add(varSubType87);
		varSubTypeList.add(varSubType88);
		varSubTypeList.add(varSubType89);
		varSubTypeList.add(varSubType90);
		
		List<EndTagExtInfoName> endInfoNameList = new ArrayList<EndTagExtInfoName>();
		List<EndTagExtInfoValue> endInfoValueList = new ArrayList<EndTagExtInfoValue>();
		
		EndTagExtInfoName endTagExtInfoName = new EndTagExtInfoName("STAGE", "油井阶段", endTagType);
		EndTagExtInfoName endTagExtInfoName1 = new EndTagExtInfoName("TECHNOLOGY", "油井工艺", endTagType);
		EndTagExtInfoName endTagExtInfoName2 = new EndTagExtInfoName("BENG_JING", "泵径", endTagType);
		EndTagExtInfoName endTagExtInfoName3 = new EndTagExtInfoName("BENG_SHENG", "泵深", endTagType);
		EndTagExtInfoName endTagExtInfoName4 = new EndTagExtInfoName("BENG_GUA_SHEN_DU", "泵挂深度", endTagType);
		EndTagExtInfoName endTagExtInfoName5 = new EndTagExtInfoName("HAN_SHUI_LV", "含水率", endTagType);
		EndTagExtInfoName endTagExtInfoName6 = new EndTagExtInfoName("YOU_QI_BI", "油气比", endTagType);
		EndTagExtInfoName endTagExtInfoName7 = new EndTagExtInfoName("MI_DU", "原油密度", endTagType);
		EndTagExtInfoName endTagExtInfoName8 = new EndTagExtInfoName("NIAN_DU", "粘度", endTagType);
		EndTagExtInfoName endTagExtInfoName9 = new EndTagExtInfoName("KUANG_HUA_DU", "矿化度", endTagType);
		EndTagExtInfoName endTagExtInfoName10 = new EndTagExtInfoName("YOU_GUAN_ZU_HE", "油管组合", endTagType);
		EndTagExtInfoName endTagExtInfoName11 = new EndTagExtInfoName("CHOU_YOU_GAN_ZU_HE", "抽油杆组合", endTagType);
		
		EndTagExtInfoValue endTagExtInfoValue = new EndTagExtInfoValue("ZI_PEN","自喷",endTagExtInfoName);
		EndTagExtInfoValue endTagExtInfoValue1 = new EndTagExtInfoValue("ZHU_QI","注汽",endTagExtInfoName);
		EndTagExtInfoValue endTagExtInfoValue2 = new EndTagExtInfoValue("MEN_JING","焖井",endTagExtInfoName);
		EndTagExtInfoValue endTagExtInfoValue3 = new EndTagExtInfoValue("CHOU_YOU","抽油",endTagExtInfoName);
		
		EndTagExtInfoValue endTagExtInfoValue4 = new EndTagExtInfoValue("XI_YOU","稀油",endTagExtInfoName1);
		EndTagExtInfoValue endTagExtInfoValue5 = new EndTagExtInfoValue("CHOU_YOU_JING","稠油",endTagExtInfoName1);
		
		endInfoNameList.add(endTagExtInfoName);
		endInfoNameList.add(endTagExtInfoName1);
		endInfoNameList.add(endTagExtInfoName2);
		endInfoNameList.add(endTagExtInfoName3);
		endInfoNameList.add(endTagExtInfoName4);
		endInfoNameList.add(endTagExtInfoName5);
		endInfoNameList.add(endTagExtInfoName6);
		endInfoNameList.add(endTagExtInfoName7);
		endInfoNameList.add(endTagExtInfoName8);
		endInfoNameList.add(endTagExtInfoName9);
		endInfoNameList.add(endTagExtInfoName10);
		endInfoNameList.add(endTagExtInfoName11);
		
		endInfoValueList.add(endTagExtInfoValue);
		endInfoValueList.add(endTagExtInfoValue1);
		endInfoValueList.add(endTagExtInfoValue2);
		endInfoValueList.add(endTagExtInfoValue3);
		endInfoValueList.add(endTagExtInfoValue4);
		endInfoValueList.add(endTagExtInfoValue5);
		
		typeService.insertMajorTagType(majorTagTypeList);
		typeService.insertEndTagType(endTagTypeList);
		typeService.insertEndTagSubType(endTagSubTypeList);
		typeService.insertVarGroupCfg(varGroupCfgList);
		typeService.insertVarType(varTypeList);
		typeService.insertVarSubType(varSubTypeList);
		typeService.insertEndTagExtInfoName(endInfoNameList);
		typeService.insertEndTagExtInfoValue(endInfoValueList);

	}

}
