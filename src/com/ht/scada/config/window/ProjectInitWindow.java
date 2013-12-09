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
import com.ht.scada.common.tag.type.entity.CommunicationProtocalType;
import com.ht.scada.common.tag.type.entity.DataValueType;
import com.ht.scada.common.tag.type.entity.EndTagExtInfoName;
import com.ht.scada.common.tag.type.entity.EndTagExtInfoValue;
import com.ht.scada.common.tag.type.entity.EndTagSubType;
import com.ht.scada.common.tag.type.entity.EndTagType;
import com.ht.scada.common.tag.type.entity.MajorTagType;
import com.ht.scada.common.tag.type.entity.VarSubType;
import com.ht.scada.common.tag.type.entity.VarType;
import com.ht.scada.common.tag.type.service.TypeService;
import com.ht.scada.common.tag.util.CommunicationProtocalEnum;
import com.ht.scada.common.tag.util.DataTypeEnum;
import com.ht.scada.common.tag.util.EndTagExtNameEnum;
import com.ht.scada.common.tag.util.EndTagExtValueEnum;
import com.ht.scada.common.tag.util.EndTagSubTypeEnum;
import com.ht.scada.common.tag.util.EndTagTypeEnum;
import com.ht.scada.common.tag.util.MajorTagTypeEnum;
import com.ht.scada.common.tag.util.VarGroupEnum;
import com.ht.scada.common.tag.util.VarSubTypeEnum;
import com.ht.scada.common.tag.util.VarTypeEnum;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.LayoutUtil;

/**
 * 工程初始化窗体
 * @author 赵磊、王蓬
 *
 */
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
		
		
		
		List<MajorTagType> majorTagTypeList = new ArrayList<MajorTagType>();		// 主索引集合
		List<EndTagType> endTagTypeList = new ArrayList<EndTagType>();
		List<EndTagSubType> endTagSubTypeList = new ArrayList<EndTagSubType>();		// 末端节点子类型集合
		List<VarGroupCfg> varGroupCfgList = new ArrayList<VarGroupCfg>();			// 变量分组集合
		List<VarType> varTypeList = new ArrayList<VarType>();
		List<VarSubType> varSubTypeList = new ArrayList<VarSubType>();

		MajorTagType majorTagType = new MajorTagType(MajorTagTypeEnum.CHANG_LEVEL.toString(), MajorTagTypeEnum.CHANG_LEVEL.getValue());
		MajorTagType majorTagType1 = new MajorTagType(MajorTagTypeEnum.KUANG_LEVEL.toString(), MajorTagTypeEnum.KUANG_LEVEL.getValue());
		MajorTagType majorTagType2 = new MajorTagType(MajorTagTypeEnum.DUI_LEVEL.toString(), MajorTagTypeEnum.DUI_LEVEL.getValue());
		MajorTagType majorTagType3 = new MajorTagType(MajorTagTypeEnum.QU_LEVEL.toString(), MajorTagTypeEnum.QU_LEVEL.getValue());
		MajorTagType majorTagType4 = new MajorTagType(MajorTagTypeEnum.SYSTEM_LEVEL.toString(), MajorTagTypeEnum.SYSTEM_LEVEL.getValue());
		
		majorTagTypeList.add(majorTagType);
		majorTagTypeList.add(majorTagType1);
		majorTagTypeList.add(majorTagType2);
		majorTagTypeList.add(majorTagType3);
		majorTagTypeList.add(majorTagType4);

		EndTagType endTagType   = new EndTagType(EndTagTypeEnum.YOU_JING.toString(), EndTagTypeEnum.YOU_JING.getValue());
		EndTagType endTagType1  = new EndTagType(EndTagTypeEnum.SHUI_YUAN_JING.toString(), EndTagTypeEnum.SHUI_YUAN_JING.getValue());
		EndTagType endTagType2  = new EndTagType(EndTagTypeEnum.ZHU_SHUI_JING.toString(), EndTagTypeEnum.ZHU_SHUI_JING.getValue());
		EndTagType endTagType3  = new EndTagType(EndTagTypeEnum.ZENG_YA_ZHAN.toString(), EndTagTypeEnum.ZENG_YA_ZHAN.getValue());
		EndTagType endTagType4  = new EndTagType(EndTagTypeEnum.ZHU_QI_ZHAN.toString(), EndTagTypeEnum.ZHU_QI_ZHAN.getValue());
		EndTagType endTagType5  = new EndTagType(EndTagTypeEnum.LIAN_HE_ZHAN.toString(), EndTagTypeEnum.LIAN_HE_ZHAN.getValue());
		EndTagType endTagType6  = new EndTagType(EndTagTypeEnum.JIE_ZHUAN_ZHAN.toString(), EndTagTypeEnum.JIE_ZHUAN_ZHAN.getValue());
		EndTagType endTagType7  = new EndTagType(EndTagTypeEnum.ZHU_SHUI_ZHAN.toString(), EndTagTypeEnum.ZHU_SHUI_ZHAN.getValue());
		EndTagType endTagType8  = new EndTagType(EndTagTypeEnum.JI_LIANG_ZHAN.toString(), EndTagTypeEnum.JI_LIANG_ZHAN.getValue());
		EndTagType endTagType9  = new EndTagType(EndTagTypeEnum.PEI_SHUI_JIAN.toString(), EndTagTypeEnum.PEI_SHUI_JIAN.getValue());
		EndTagType endTagType10 = new EndTagType(EndTagTypeEnum.JI_LIANG_CHE.toString(), EndTagTypeEnum.JI_LIANG_CHE.getValue());
		EndTagType endTagType11 = new EndTagType(EndTagTypeEnum.TIAN_RAN_QI_JING.toString(),EndTagTypeEnum.TIAN_RAN_QI_JING.getValue());
				
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
		endTagTypeList.add(endTagType11);

		EndTagSubType endTagSubType = new EndTagSubType(EndTagSubTypeEnum.YOU_LIANG_SHI.toString(), EndTagSubTypeEnum.YOU_LIANG_SHI.getValue());
		endTagSubType.setEndTagType(endTagType);
		EndTagSubType endTagSubType1 = new EndTagSubType(EndTagSubTypeEnum.DIAN_GUN_TONG.toString(), EndTagSubTypeEnum.DIAN_GUN_TONG.getValue());
		endTagSubType1.setEndTagType(endTagType);
		EndTagSubType endTagSubType2 = new EndTagSubType(EndTagSubTypeEnum.GAO_YUAN_JI.toString(), EndTagSubTypeEnum.GAO_YUAN_JI.getValue());
		endTagSubType2.setEndTagType(endTagType);
		EndTagSubType endTagSubType3 = new EndTagSubType(EndTagSubTypeEnum.LUO_GAN_BENG.toString(), EndTagSubTypeEnum.LUO_GAN_BENG.getValue());
		endTagSubType3.setEndTagType(endTagType);
		EndTagSubType endTagSubType4 = new EndTagSubType(EndTagSubTypeEnum.DIAN_QIAN_BENG.toString(), EndTagSubTypeEnum.DIAN_QIAN_BENG.getValue());
		endTagSubType4.setEndTagType(endTagType);
		EndTagSubType endTagSubType5 = new EndTagSubType(EndTagSubTypeEnum.GU_lI_JING.toString(), EndTagSubTypeEnum.GU_lI_JING.getValue());
		endTagSubType5.setEndTagType(endTagType);
		
		endTagSubTypeList.add(endTagSubType);
		endTagSubTypeList.add(endTagSubType1);
		endTagSubTypeList.add(endTagSubType2);
		endTagSubTypeList.add(endTagSubType3);
		endTagSubTypeList.add(endTagSubType4);
		endTagSubTypeList.add(endTagSubType5);
		
		// 19 + 1(其它)
		VarGroupCfg varGroupCfgDianYC = new VarGroupCfg(VarGroupEnum.DIAN_YC.toString(), VarGroupEnum.DIAN_YC.getValue());
		VarGroupCfg varGroupCfgDianYM = new VarGroupCfg(VarGroupEnum.DIAN_YM.toString(), VarGroupEnum.DIAN_YM.getValue());
		VarGroupCfg varGroupCfgDianXB = new VarGroupCfg(VarGroupEnum.DIAN_XB.toString(), VarGroupEnum.DIAN_XB.getValue());
		VarGroupCfg varGroupCfgJingKou = new VarGroupCfg(VarGroupEnum.JING_KOU.toString(), VarGroupEnum.JING_KOU.getValue());
		VarGroupCfg varGroupCfgSGT = new VarGroupCfg(VarGroupEnum.YOU_JING_SGT.toString(), VarGroupEnum.YOU_JING_SGT.getValue());
		VarGroupCfg varGroupCfgDGT = new VarGroupCfg(VarGroupEnum.YOU_JING_DGT.toString(), VarGroupEnum.YOU_JING_DGT.getValue());
		VarGroupCfg varGroupCfgJiLiangChe = new VarGroupCfg(VarGroupEnum.JI_LIANG.toString(), VarGroupEnum.JI_LIANG.getValue());
		VarGroupCfg varGroupCfgZhuCai = new VarGroupCfg(VarGroupEnum.ZHU_CAI.toString(), VarGroupEnum.ZHU_CAI.getValue());
		VarGroupCfg varGroupCfgRTUStatus = new VarGroupCfg(VarGroupEnum.RTU_ZHUANG_TAI.toString(), VarGroupEnum.RTU_ZHUANG_TAI.getValue());
		VarGroupCfg varGroupCfgSensorRun = new VarGroupCfg(VarGroupEnum.SENSOR_RUN.toString(), VarGroupEnum.SENSOR_RUN.getValue());	
		VarGroupCfg varGroupCfgYouJingZhuangTai = new VarGroupCfg(VarGroupEnum.YOU_JING_ZHUANG_TAI.toString(), VarGroupEnum.YOU_JING_ZHUANG_TAI.getValue());
		VarGroupCfg varGroupCfgYouJingSOE = new VarGroupCfg(VarGroupEnum.YOU_JING_SOE.toString(), VarGroupEnum.YOU_JING_SOE.getValue());
		VarGroupCfg varGroupCfgJiaReLuSOE = new VarGroupCfg(VarGroupEnum.JIA_RE_LU_SOE.toString(), VarGroupEnum.JIA_RE_LU_SOE.getValue());
		VarGroupCfg varGroupCfgDianSOE = new VarGroupCfg(VarGroupEnum.DIAN_SOE.toString(), VarGroupEnum.DIAN_SOE.getValue());
		VarGroupCfg varGroupCfgJiaReLu = new VarGroupCfg(VarGroupEnum.JIA_RE_LU.toString(), VarGroupEnum.JIA_RE_LU.getValue());
		VarGroupCfg varGroupCfgBianPinQi = new VarGroupCfg(VarGroupEnum.BIAN_PIN_QI.toString(), VarGroupEnum.BIAN_PIN_QI.getValue());
		VarGroupCfg varGroupCfgXuLiang = new VarGroupCfg(VarGroupEnum.XU_LIANG.toString(), VarGroupEnum.XU_LIANG.getValue());
		VarGroupCfg varGroupCfgRTUCanShu = new VarGroupCfg(VarGroupEnum.RTU_CAN_SHU.toString(), VarGroupEnum.RTU_CAN_SHU.getValue());
		VarGroupCfg varGroupCfgSensorCanShu = new VarGroupCfg(VarGroupEnum.SENSOR_CAN_SHU.toString(), VarGroupEnum.SENSOR_CAN_SHU.getValue());	
		VarGroupCfg varGroupCfgQT = new VarGroupCfg(VarGroupEnum.QI_TA.toString(), VarGroupEnum.QI_TA.getValue());
		
		varGroupCfgList.add(varGroupCfgDianYC);
		varGroupCfgList.add(varGroupCfgDianYM);
		varGroupCfgList.add(varGroupCfgDianXB);
		varGroupCfgList.add(varGroupCfgJingKou);
		varGroupCfgList.add(varGroupCfgSGT);
		varGroupCfgList.add(varGroupCfgDGT);
		varGroupCfgList.add(varGroupCfgJiLiangChe);
		varGroupCfgList.add(varGroupCfgZhuCai);
		varGroupCfgList.add(varGroupCfgRTUStatus);
		varGroupCfgList.add(varGroupCfgSensorRun);
		varGroupCfgList.add(varGroupCfgYouJingZhuangTai);
		varGroupCfgList.add(varGroupCfgYouJingSOE);
		varGroupCfgList.add(varGroupCfgJiaReLuSOE);
		varGroupCfgList.add(varGroupCfgDianSOE);
		varGroupCfgList.add(varGroupCfgJiaReLu);
		varGroupCfgList.add(varGroupCfgBianPinQi);
		varGroupCfgList.add(varGroupCfgXuLiang);
		varGroupCfgList.add(varGroupCfgRTUCanShu);
		varGroupCfgList.add(varGroupCfgSensorCanShu);
		varGroupCfgList.add(varGroupCfgQT);
				
		Set<VarGroupCfg> varGroupCfgSet = new HashSet<VarGroupCfg>();
		varGroupCfgSet.add(varGroupCfgDianYC);
		varGroupCfgSet.add(varGroupCfgDianYM);
		varGroupCfgSet.add(varGroupCfgDianXB);
		varGroupCfgSet.add(varGroupCfgJingKou);
		varGroupCfgSet.add(varGroupCfgSGT);
		varGroupCfgSet.add(varGroupCfgDGT);
		varGroupCfgSet.add(varGroupCfgJiLiangChe);
		varGroupCfgSet.add(varGroupCfgZhuCai);
		varGroupCfgSet.add(varGroupCfgRTUStatus);
		varGroupCfgSet.add(varGroupCfgSensorRun);
		varGroupCfgSet.add(varGroupCfgYouJingZhuangTai);
		varGroupCfgSet.add(varGroupCfgYouJingSOE);
		varGroupCfgSet.add(varGroupCfgJiaReLuSOE);
		varGroupCfgSet.add(varGroupCfgDianSOE);
		varGroupCfgSet.add(varGroupCfgJiaReLu);
		varGroupCfgSet.add(varGroupCfgBianPinQi);
		varGroupCfgSet.add(varGroupCfgXuLiang);
		varGroupCfgSet.add(varGroupCfgRTUCanShu);
		varGroupCfgSet.add(varGroupCfgSensorCanShu);
		varGroupCfgSet.add(varGroupCfgQT);
		
		
		endTagType.setVarGroupCfgSet(varGroupCfgSet);	// 监控对象（油井）需要监控的 变量
		
		VarType varTypeYC = new VarType(VarTypeEnum.YC.toString(), VarTypeEnum.YC.getValue());
		VarType varTypeYX = new VarType(VarTypeEnum.YX.toString(), VarTypeEnum.YX.getValue());
		VarType varTypeYM = new VarType(VarTypeEnum.YM.toString(), VarTypeEnum.YM.getValue());
		VarType varTypeYK = new VarType(VarTypeEnum.YK.toString(), VarTypeEnum.YK.getValue());
		VarType varTypeYT = new VarType(VarTypeEnum.YT.toString(), VarTypeEnum.YT.getValue());
		VarType varTypeQT = new VarType(VarTypeEnum.QT.toString(), VarTypeEnum.QT.getValue());
		
		varTypeList.add(varTypeYC);
		varTypeList.add(varTypeYX);
		varTypeList.add(varTypeYM);
		varTypeList.add(varTypeYK);
		varTypeList.add(varTypeYT);
		varTypeList.add(varTypeQT);
		
		VarSubType varSubType = new VarSubType(VarSubTypeEnum.YOU_YA.toString(), VarSubTypeEnum.YOU_YA.getValue(),varGroupCfgJingKou,varTypeYC);
		VarSubType varSubType1 = new VarSubType(VarSubTypeEnum.TAO_YA.toString(), VarSubTypeEnum.TAO_YA.getValue(),varGroupCfgJingKou,varTypeYC);
		VarSubType varSubType2 = new VarSubType(VarSubTypeEnum.HUI_YA.toString(), VarSubTypeEnum.HUI_YA.getValue(),varGroupCfgJingKou,varTypeYC);
		VarSubType varSubType3 = new VarSubType(VarSubTypeEnum.JING_KOU_WEN_DU.toString(), VarSubTypeEnum.JING_KOU_WEN_DU.getValue(),varGroupCfgJingKou,varTypeYC);
		VarSubType varSubType4 = new VarSubType(VarSubTypeEnum.HUI_GUAN_WEN_DU.toString(), VarSubTypeEnum.HUI_GUAN_WEN_DU.getValue(),varGroupCfgJingKou,varTypeYC);
		VarSubType varSubType5 = new VarSubType(VarSubTypeEnum.QI_TING_ZHUANG_TAI.toString(), VarSubTypeEnum.QI_TING_ZHUANG_TAI.getValue(),varGroupCfgJingKou,varTypeYX);
		varSubType5.setRemark("0为停，1为启（运行）");
		
		VarSubType varSubType6 = new VarSubType(VarSubTypeEnum.CHONG_CHENG.toString(), VarSubTypeEnum.CHONG_CHENG.getValue(),varGroupCfgSGT,varTypeYC);
		VarSubType varSubType7 = new VarSubType(VarSubTypeEnum.CHONG_CI.toString(), VarSubTypeEnum.CHONG_CI.getValue(),varGroupCfgSGT,varTypeYC);
		VarSubType varSubType8 = new VarSubType(VarSubTypeEnum.SHANG_XING_CHONG_CI.toString(), VarSubTypeEnum.SHANG_XING_CHONG_CI.getValue(),varGroupCfgSGT,varTypeYC);
		VarSubType varSubType9 = new VarSubType(VarSubTypeEnum.XIA_XING_CHONG_CI.toString(), VarSubTypeEnum.XIA_XING_CHONG_CI.getValue(),varGroupCfgSGT,varTypeYC);
		VarSubType varSubType10 = new VarSubType(VarSubTypeEnum.ZUI_DA_ZAI_HE.toString(), VarSubTypeEnum.ZUI_DA_ZAI_HE.getValue(),varGroupCfgSGT,varTypeYC);
		VarSubType varSubType11 = new VarSubType(VarSubTypeEnum.ZUI_XIAO_ZAI_HE.toString(), VarSubTypeEnum.ZUI_XIAO_ZAI_HE.getValue(), varGroupCfgSGT,varTypeYC);
		VarSubType varSubType12 = new VarSubType(VarSubTypeEnum.WEI_YI_ARRAY.toString(), VarSubTypeEnum.WEI_YI_ARRAY.getValue(),varGroupCfgSGT,varTypeQT);
		VarSubType varSubType13 = new VarSubType(VarSubTypeEnum.ZAI_HE_ARRAY.toString(), VarSubTypeEnum.ZAI_HE_ARRAY.getValue(),varGroupCfgSGT,varTypeQT);
		
		VarSubType varSubType14 = new VarSubType(VarSubTypeEnum.DIAN_LIU_ARRAY.toString(), VarSubTypeEnum.DIAN_LIU_ARRAY.getValue(),varGroupCfgDGT,varTypeQT);
		VarSubType varSubType15 = new VarSubType(VarSubTypeEnum.GONG_LV_ARRAY.toString(), VarSubTypeEnum.GONG_LV_ARRAY.getValue(),varGroupCfgDGT,varTypeQT);
		VarSubType varSubType16 = new VarSubType(VarSubTypeEnum.GONG_LV_YIN_SHU_ARRAY.toString(), VarSubTypeEnum.GONG_LV_YIN_SHU_ARRAY.getValue(),varGroupCfgDGT,varTypeQT);
		VarSubType varSubType17 = new VarSubType(VarSubTypeEnum.DIAN_GONG_TU_ARRAY.toString(), VarSubTypeEnum.DIAN_GONG_TU_ARRAY.getValue(),varGroupCfgDGT,varTypeQT);
		
		VarSubType varSubType18 = new VarSubType(VarSubTypeEnum.I_A.toString(), VarSubTypeEnum.I_A.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType19 = new VarSubType(VarSubTypeEnum.I_B.toString(), VarSubTypeEnum.I_B.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType20 = new VarSubType(VarSubTypeEnum.I_C.toString(), VarSubTypeEnum.I_C.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType21 = new VarSubType(VarSubTypeEnum.U_A.toString(), VarSubTypeEnum.U_A.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType22 = new VarSubType(VarSubTypeEnum.U_B.toString(), VarSubTypeEnum.U_B.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType23 = new VarSubType(VarSubTypeEnum.U_C.toString(), VarSubTypeEnum.U_C.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType24 = new VarSubType(VarSubTypeEnum.I_3XBPH.toString(), VarSubTypeEnum.I_3XBPH.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType25 = new VarSubType(VarSubTypeEnum.U_AB.toString(), VarSubTypeEnum.U_AB.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType26 = new VarSubType(VarSubTypeEnum.U_BC.toString(), VarSubTypeEnum.U_BC.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType27 = new VarSubType(VarSubTypeEnum.U_CA.toString(), VarSubTypeEnum.U_CA.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType28 = new VarSubType(VarSubTypeEnum.GV_YG.toString(), VarSubTypeEnum.GV_YG.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType29 = new VarSubType(VarSubTypeEnum.GV_WG.toString(), VarSubTypeEnum.GV_WG.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType30 = new VarSubType(VarSubTypeEnum.GV_SZ.toString(), VarSubTypeEnum.GV_SZ.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType101 = new VarSubType(VarSubTypeEnum.GV_GLYS.toString(), VarSubTypeEnum.GV_GLYS.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType31 = new VarSubType(VarSubTypeEnum.GV_ZB.toString(), VarSubTypeEnum.GV_ZB.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType32 = new VarSubType(VarSubTypeEnum.GV_YG_A.toString(), VarSubTypeEnum.GV_YG_A.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType33 = new VarSubType(VarSubTypeEnum.GV_YG_B.toString(), VarSubTypeEnum.GV_YG_B.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType34 = new VarSubType(VarSubTypeEnum.GV_YG_C.toString(), VarSubTypeEnum.GV_YG_C.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType35 = new VarSubType(VarSubTypeEnum.GV_WG_A.toString(), VarSubTypeEnum.GV_WG_A.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType36 = new VarSubType(VarSubTypeEnum.GV_WG_B.toString(), VarSubTypeEnum.GV_WG_B.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType37 = new VarSubType(VarSubTypeEnum.GV_WG_C.toString(), VarSubTypeEnum.GV_WG_C.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType38 = new VarSubType(VarSubTypeEnum.GV_SZ_A.toString(), VarSubTypeEnum.GV_SZ_A.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType39 = new VarSubType(VarSubTypeEnum.GV_SZ_B.toString(), VarSubTypeEnum.GV_SZ_B.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType40 = new VarSubType(VarSubTypeEnum.GV_SZ_C.toString(), VarSubTypeEnum.GV_SZ_C.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType41 = new VarSubType(VarSubTypeEnum.GV_GVYS_A.toString(), VarSubTypeEnum.GV_GVYS_A.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType42 = new VarSubType(VarSubTypeEnum.GV_GVYS_B.toString(), VarSubTypeEnum.GV_GVYS_B.getValue(),varGroupCfgDianYC,varTypeYC);
		VarSubType varSubType43 = new VarSubType(VarSubTypeEnum.GV_GVYS_C.toString(), VarSubTypeEnum.GV_GVYS_C.getValue(),varGroupCfgDianYC,varTypeYC);
		
		VarSubType varSubType44 = new VarSubType(VarSubTypeEnum.DL_ZX_Z.toString(), VarSubTypeEnum.DL_ZX_Z.getValue(),varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType45 = new VarSubType(VarSubTypeEnum.DL_ZX_J.toString(), VarSubTypeEnum.DL_ZX_J.getValue(),varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType46 = new VarSubType(VarSubTypeEnum.DL_ZX_F.toString(), VarSubTypeEnum.DL_ZX_F.getValue(),varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType47 = new VarSubType(VarSubTypeEnum.DL_ZX_P.toString(), VarSubTypeEnum.DL_ZX_P.getValue(),varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType48 = new VarSubType(VarSubTypeEnum.DL_ZX_G.toString(), VarSubTypeEnum.DL_ZX_G.getValue(),varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType49 = new VarSubType(VarSubTypeEnum.DL_FX_Z.toString(), VarSubTypeEnum.DL_FX_Z.getValue(),varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType50 = new VarSubType(VarSubTypeEnum.DL_FX_J.toString(), VarSubTypeEnum.DL_FX_J.getValue(),varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType51 = new VarSubType(VarSubTypeEnum.DL_FX_F.toString(), VarSubTypeEnum.DL_FX_F.getValue(),varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType52 = new VarSubType(VarSubTypeEnum.DL_FX_P.toString(), VarSubTypeEnum.DL_FX_P.getValue(),varGroupCfgDianYM,varTypeYM);
		VarSubType varSubType53 = new VarSubType(VarSubTypeEnum.DL_FX_G.toString(), VarSubTypeEnum.DL_FX_G.getValue(),varGroupCfgDianYM,varTypeYM);
		
		VarSubType varSubType54 = new VarSubType(VarSubTypeEnum.XB_IA.toString(), VarSubTypeEnum.XB_IA.getValue(),varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType55 = new VarSubType(VarSubTypeEnum.XB_IB.toString(), VarSubTypeEnum.XB_IB.getValue(),varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType56 = new VarSubType(VarSubTypeEnum.XB_IC.toString(), VarSubTypeEnum.XB_IC.getValue(),varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType57 = new VarSubType(VarSubTypeEnum.XB_UA.toString(), VarSubTypeEnum.XB_UA.getValue(),varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType58 = new VarSubType(VarSubTypeEnum.XB_UB.toString(), VarSubTypeEnum.XB_UB.getValue(),varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType59 = new VarSubType(VarSubTypeEnum.XB_UC.toString(), VarSubTypeEnum.XB_UC.getValue(),varGroupCfgDianXB,varTypeYC);
		VarSubType varSubType60 = new VarSubType(VarSubTypeEnum.XB_IA_ARRAY.toString(), VarSubTypeEnum.XB_IA_ARRAY.getValue(), varGroupCfgDianXB,varTypeQT);
		VarSubType varSubType61 = new VarSubType(VarSubTypeEnum.XB_IB_ARRAY.toString(), VarSubTypeEnum.XB_IB_ARRAY.getValue(),varGroupCfgDianXB,varTypeQT);
		VarSubType varSubType62 = new VarSubType(VarSubTypeEnum.XB_IC_ARRAY.toString(), VarSubTypeEnum.XB_IC_ARRAY.getValue(),varGroupCfgDianXB,varTypeQT);
		VarSubType varSubType63 = new VarSubType(VarSubTypeEnum.XB_UA_ARRAY.toString(), VarSubTypeEnum.XB_UA_ARRAY.getValue(),varGroupCfgDianXB,varTypeQT);
		VarSubType varSubType64 = new VarSubType(VarSubTypeEnum.XB_UB_ARRAY.toString(), VarSubTypeEnum.XB_UB_ARRAY.getValue(),varGroupCfgDianXB,varTypeQT);
		VarSubType varSubType65 = new VarSubType(VarSubTypeEnum.XB_UC_ARRAY.toString(), VarSubTypeEnum.XB_UC_ARRAY.getValue(),varGroupCfgDianXB,varTypeQT);
		
		VarSubType varSubType66 = new VarSubType(VarSubTypeEnum.JLC_QL_SH.toString(), VarSubTypeEnum.JLC_QL_SH.getValue(),varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType67 = new VarSubType(VarSubTypeEnum.JLC_QL_LJ.toString(), VarSubTypeEnum.JLC_QL_LJ.getValue(),varGroupCfgJiLiangChe,varTypeYM);
		VarSubType varSubType68 = new VarSubType(VarSubTypeEnum.JLC_YL_SH.toString(), VarSubTypeEnum.JLC_YL_SH.getValue(),varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType69 = new VarSubType(VarSubTypeEnum.JLC_YL_LJ.toString(), VarSubTypeEnum.JLC_YL_LJ.getValue(),varGroupCfgJiLiangChe,varTypeYM);
		VarSubType varSubType70 = new VarSubType(VarSubTypeEnum.JLC_SL_SH.toString(), VarSubTypeEnum.JLC_SL_SH.getValue(),varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType71 = new VarSubType(VarSubTypeEnum.JLC_SL_LJ.toString(), VarSubTypeEnum.JLC_SL_LJ.getValue(),varGroupCfgJiLiangChe,varTypeYM);
		VarSubType varSubType72 = new VarSubType(VarSubTypeEnum.JLC_HSL_SH.toString(), VarSubTypeEnum.JLC_HSL_SH.getValue(),varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType73 = new VarSubType(VarSubTypeEnum.JLC_WD_SH.toString(), VarSubTypeEnum.JLC_WD_SH.getValue(),varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType74 = new VarSubType(VarSubTypeEnum.JLC_YALI_SH.toString(), VarSubTypeEnum.JLC_YALI_SH.getValue(),varGroupCfgJiLiangChe,varTypeYC);
		VarSubType varSubType75 = new VarSubType(VarSubTypeEnum.JLC_MD_SH.toString(), VarSubTypeEnum.JLC_MD_SH.getValue(),varGroupCfgJiLiangChe,varTypeYC);
		
		VarSubType varSubType76 = new VarSubType(VarSubTypeEnum.ZC_ZQLL_SH.toString(), VarSubTypeEnum.ZC_ZQLL_SH.getValue(),varGroupCfgZhuCai,varTypeYC);
		VarSubType varSubType77 = new VarSubType(VarSubTypeEnum.ZC_ZQLL_LJ.toString(), VarSubTypeEnum.ZC_ZQLL_LJ.getValue(),varGroupCfgZhuCai,varTypeYM);
		VarSubType varSubType78 = new VarSubType(VarSubTypeEnum.ZC_ZQYL.toString(), VarSubTypeEnum.ZC_ZQYL.getValue(),varGroupCfgZhuCai,varTypeYC);
		VarSubType varSubType79 = new VarSubType(VarSubTypeEnum.ZC_ZQWD.toString(), VarSubTypeEnum.ZC_ZQWD.getValue(),varGroupCfgZhuCai,varTypeYC);
		VarSubType varSubType80 = new VarSubType(VarSubTypeEnum.ZC_ZQGD.toString(), VarSubTypeEnum.ZC_ZQGD.getValue(),varGroupCfgZhuCai,varTypeYC);
		
		VarSubType varSubType81 = new VarSubType(VarSubTypeEnum.RTU_RJ45_STATUS.toString(), VarSubTypeEnum.RTU_RJ45_STATUS.getValue(),varGroupCfgRTUStatus,varTypeYX);
		VarSubType varSubType82 = new VarSubType(VarSubTypeEnum.RTU_COM1_STATUS.toString(), VarSubTypeEnum.RTU_COM1_STATUS.getValue(),varGroupCfgRTUStatus,varTypeYX);
		VarSubType varSubType83 = new VarSubType(VarSubTypeEnum.RTU_COM2_STATUS.toString(), VarSubTypeEnum.RTU_COM2_STATUS.getValue(),varGroupCfgRTUStatus,varTypeYX);
		VarSubType varSubType84 = new VarSubType(VarSubTypeEnum.RTU_COM3_STATUS.toString(), VarSubTypeEnum.RTU_COM3_STATUS.getValue(),varGroupCfgRTUStatus,varTypeYX);
		VarSubType varSubType85 = new VarSubType(VarSubTypeEnum.RTU_COM4_STATUS.toString(), VarSubTypeEnum.RTU_COM4_STATUS.getValue(),varGroupCfgRTUStatus,varTypeYX);
		VarSubType varSubType86 = new VarSubType(VarSubTypeEnum.RTU_ZIGBEE_STATUS.toString(), VarSubTypeEnum.RTU_ZIGBEE_STATUS.getValue(),varGroupCfgRTUStatus,varTypeYX);
		
		VarSubType varSubType87 = new VarSubType(VarSubTypeEnum.CGQ_RTU_STATUS.toString(), VarSubTypeEnum.CGQ_RTU_STATUS.getValue(),varGroupCfgSensorRun,varTypeYX);
		varSubType87.setRemark("非唯一子类型，需用传感器别名区分，格式为\"子类型名|传感器别名\"");
		VarSubType varSubType88 = new VarSubType(VarSubTypeEnum.CGQ_RTU_TIME.toString(), VarSubTypeEnum.CGQ_RTU_TIME.getValue(),varGroupCfgSensorRun,varTypeYC);
		varSubType88.setRemark("非唯一子类型，需用传感器别名区分，格式为\"子类型名|传感器别名\"");
		VarSubType varSubType89 = new VarSubType(VarSubTypeEnum.CGQ_REMAINED_TIME.toString(), VarSubTypeEnum.CGQ_REMAINED_TIME.getValue(),varGroupCfgSensorRun,varTypeYC);
		varSubType89.setRemark("非唯一子类型，需用传感器别名区分，格式为\"子类型名|传感器别名\"");
		VarSubType varSubType90 = new VarSubType(VarSubTypeEnum.CGQ_REMAINED_DIANLIANG.toString(), VarSubTypeEnum.CGQ_REMAINED_DIANLIANG.getValue(),varGroupCfgSensorRun,varTypeYC);
		varSubType90.setRemark("非唯一子类型，需用传感器别名区分，格式为\"子类型名|传感器别名\"");
		
//		VarSubType varSubType91 = new VarSubType("ZYZ_RU_KOU_WEN_DU","入口温度",varGroupCfgZYZYC,varTypeYC);
//		VarSubType varSubType92 = new VarSubType("ZYZ_CHU_KOU_WEN_DU","出口温度",varGroupCfgZYZYC,varTypeYC);
//		VarSubType varSubType93 = new VarSubType("ZYZ_WAI_SHU_YA_LI","外输压力",varGroupCfgZYZYC,varTypeYC);
//		VarSubType varSubType94 = new VarSubType("ZYZ_HAN_SHUI_LV","原油含水率",varGroupCfgZYZYC,varTypeYC);
//		VarSubType varSubType95 = new VarSubType("ZYZ_SHUN_SHI_LIU_LIANG","瞬时流量",varGroupCfgZYZYC,varTypeYC);
//		VarSubType varSubType96 = new VarSubType("ZYZ_LEI_JI_LIU_LIANG","累积流量",varGroupCfgZYZYC,varTypeYC);
//		VarSubType varSubType97 = new VarSubType("ZYZ_YE_WEI","缓冲罐液位",varGroupCfgZYZYC,varTypeYC);
//		VarSubType varSubType98 = new VarSubType("ZYZ_WEN_DU_1","环境温度#1",varGroupCfgZYZYC,varTypeYC);
//		VarSubType varSubType99 = new VarSubType("ZYZ_WEN_DU_2","环境温度#2",varGroupCfgZYZYC,varTypeYC);
//		VarSubType varSubType100 = new VarSubType("ZYZ_DIAN_DONG_DIE_FA","电动蝶阀值",varGroupCfgZYZYC,varTypeYC);
		
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
		varSubTypeList.add(varSubType101);
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
//		varSubTypeList.add(varSubType91);
//		varSubTypeList.add(varSubType92);
//		varSubTypeList.add(varSubType93);
//		varSubTypeList.add(varSubType94);
//		varSubTypeList.add(varSubType95);
//		varSubTypeList.add(varSubType96);
//		varSubTypeList.add(varSubType97);
//		varSubTypeList.add(varSubType98);
//		varSubTypeList.add(varSubType99);
//		varSubTypeList.add(varSubType100);
		
		List<EndTagExtInfoName> endInfoNameList = new ArrayList<EndTagExtInfoName>();
		List<EndTagExtInfoValue> endInfoValueList = new ArrayList<EndTagExtInfoValue>();
		
		EndTagExtInfoName endTagExtInfoName = new EndTagExtInfoName(EndTagExtNameEnum.STAGE.toString(), EndTagExtNameEnum.STAGE.getValue(), endTagType);
		EndTagExtInfoName endTagExtInfoName1 = new EndTagExtInfoName(EndTagExtNameEnum.TECHNOLOGY.toString(), EndTagExtNameEnum.TECHNOLOGY.getValue(), endTagType);
		EndTagExtInfoName endTagExtInfoName2 = new EndTagExtInfoName(EndTagExtNameEnum.BENG_JING.toString(), EndTagExtNameEnum.BENG_JING.getValue(), endTagType);
		EndTagExtInfoName endTagExtInfoName3 = new EndTagExtInfoName(EndTagExtNameEnum.BENG_SHEN.toString(), EndTagExtNameEnum.BENG_SHEN.getValue(), endTagType);
		EndTagExtInfoName endTagExtInfoName4 = new EndTagExtInfoName(EndTagExtNameEnum.BENG_GUA_SHEN_DU.toString(), EndTagExtNameEnum.BENG_GUA_SHEN_DU.getValue(), endTagType);
		EndTagExtInfoName endTagExtInfoName5 = new EndTagExtInfoName(EndTagExtNameEnum.HAN_SHUI_LV.toString(), EndTagExtNameEnum.HAN_SHUI_LV.getValue(), endTagType);
		EndTagExtInfoName endTagExtInfoName6 = new EndTagExtInfoName(EndTagExtNameEnum.YOU_QI_BI.toString(), EndTagExtNameEnum.YOU_QI_BI.getValue(), endTagType);
		EndTagExtInfoName endTagExtInfoName7 = new EndTagExtInfoName(EndTagExtNameEnum.MI_DU.toString(), EndTagExtNameEnum.MI_DU.getValue(), endTagType);
		EndTagExtInfoName endTagExtInfoName8 = new EndTagExtInfoName(EndTagExtNameEnum.NIAN_DU.toString(), EndTagExtNameEnum.NIAN_DU.getValue(), endTagType);
		EndTagExtInfoName endTagExtInfoName9 = new EndTagExtInfoName(EndTagExtNameEnum.KUANG_HUA_DU.toString(), EndTagExtNameEnum.KUANG_HUA_DU.getValue(), endTagType);
		EndTagExtInfoName endTagExtInfoName10 = new EndTagExtInfoName(EndTagExtNameEnum.YOU_GUAN_ZU_HE.toString(), EndTagExtNameEnum.YOU_GUAN_ZU_HE.getValue(), endTagType);
		EndTagExtInfoName endTagExtInfoName11 = new EndTagExtInfoName(EndTagExtNameEnum.CHOU_YOU_GAN_ZU_HE.toString(), EndTagExtNameEnum.CHOU_YOU_GAN_ZU_HE.getValue(), endTagType);
		
		EndTagExtInfoValue endTagExtInfoValue = new EndTagExtInfoValue(EndTagExtValueEnum.ZI_PEN.toString(), EndTagExtValueEnum.ZI_PEN.getValue(),endTagExtInfoName);
		EndTagExtInfoValue endTagExtInfoValue1 = new EndTagExtInfoValue(EndTagExtValueEnum.ZHU_QI.toString(), EndTagExtValueEnum.ZHU_QI.getValue(),endTagExtInfoName);
		EndTagExtInfoValue endTagExtInfoValue2 = new EndTagExtInfoValue(EndTagExtValueEnum.MEN_JING.toString(), EndTagExtValueEnum.MEN_JING.getValue(),endTagExtInfoName);
		EndTagExtInfoValue endTagExtInfoValue3 = new EndTagExtInfoValue(EndTagExtValueEnum.CHOU_YOU.toString(), EndTagExtValueEnum.CHOU_YOU.getValue(),endTagExtInfoName);
		
		EndTagExtInfoValue endTagExtInfoValue4 = new EndTagExtInfoValue(EndTagExtValueEnum.GY_XI_YOU.toString(), EndTagExtValueEnum.GY_XI_YOU.getValue(),endTagExtInfoName1);
		EndTagExtInfoValue endTagExtInfoValue5 = new EndTagExtInfoValue(EndTagExtValueEnum.GY_CHOU_YOU.toString(), EndTagExtValueEnum.GY_CHOU_YOU.getValue(),endTagExtInfoName1);
		
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
		
		List<DataValueType> dataTypeList = new ArrayList<DataValueType>();
		DataValueType dataType1 = new DataValueType(DataTypeEnum.BOOL.toString(), DataTypeEnum.BOOL.getValue());
		DataValueType dataType2 = new DataValueType(DataTypeEnum.INT32.toString(), DataTypeEnum.INT32.getValue());
		DataValueType dataType3 = new DataValueType(DataTypeEnum.INT16.toString(), DataTypeEnum.INT16.getValue());
		DataValueType dataType11 = new DataValueType(DataTypeEnum.UINT32.toString(), DataTypeEnum.UINT32.getValue());
		DataValueType dataType12 = new DataValueType(DataTypeEnum.UINT16.toString(), DataTypeEnum.UINT16.getValue());
		DataValueType dataType4 = new DataValueType(DataTypeEnum.BCD.toString(), DataTypeEnum.BCD.getValue());
		DataValueType dataType5 = new DataValueType(DataTypeEnum.MOD10000.toString(), DataTypeEnum.MOD10000.getValue());
		DataValueType dataType6 = new DataValueType(DataTypeEnum.FLOAT.toString(), DataTypeEnum.FLOAT.getValue());
		DataValueType dataType7 = new DataValueType(DataTypeEnum.DOUBLE.toString(), DataTypeEnum.DOUBLE.getValue());
		DataValueType dataType8 = new DataValueType(DataTypeEnum.INT16_ARRAY.toString(), DataTypeEnum.INT16_ARRAY.getValue());
		DataValueType dataType9 = new DataValueType(DataTypeEnum.ASCII.toString(), DataTypeEnum.ASCII.getValue());
		DataValueType dataType10 = new DataValueType(DataTypeEnum.MOD1000.toString(), DataTypeEnum.MOD1000.getValue());
		
		
		dataTypeList.add(dataType1);
		dataTypeList.add(dataType2);
		dataTypeList.add(dataType3);
		dataTypeList.add(dataType11);
		dataTypeList.add(dataType12);		
		dataTypeList.add(dataType4);
		dataTypeList.add(dataType5);
		dataTypeList.add(dataType6);
		dataTypeList.add(dataType7);
		dataTypeList.add(dataType8);
		dataTypeList.add(dataType9);
		dataTypeList.add(dataType10);
		
		List<CommunicationProtocalType> commProtocalList = new ArrayList<CommunicationProtocalType>();
		CommunicationProtocalType communicationProtocalType1 = new CommunicationProtocalType(CommunicationProtocalEnum.IEC104.toString(), CommunicationProtocalEnum.IEC104.getValue());
		CommunicationProtocalType communicationProtocalType2 = new CommunicationProtocalType(CommunicationProtocalEnum.ModbusTCP.toString(), CommunicationProtocalEnum.ModbusTCP.getValue());
		CommunicationProtocalType communicationProtocalType3 = new CommunicationProtocalType(CommunicationProtocalEnum.ModbusRTU.toString(), CommunicationProtocalEnum.ModbusRTU.getValue());
		CommunicationProtocalType communicationProtocalType4 = new CommunicationProtocalType(CommunicationProtocalEnum.DL645.toString(), CommunicationProtocalEnum.DL645.getValue());
		commProtocalList.add(communicationProtocalType1);
		commProtocalList.add(communicationProtocalType2);
		commProtocalList.add(communicationProtocalType3);
		commProtocalList.add(communicationProtocalType4);
		
		typeService.insertMajorTagType(majorTagTypeList);
		typeService.insertEndTagType(endTagTypeList);
		typeService.insertEndTagSubType(endTagSubTypeList);
		typeService.insertVarGroupCfg(varGroupCfgList);
		typeService.insertVarType(varTypeList);
		typeService.insertVarSubType(varSubTypeList);
		typeService.insertEndTagExtInfoName(endInfoNameList);
		typeService.insertEndTagExtInfoValue(endInfoValueList);
		typeService.insertDataType(dataTypeList);
		typeService.insertCommunicationProtocalType(commProtocalList);

	}

}
