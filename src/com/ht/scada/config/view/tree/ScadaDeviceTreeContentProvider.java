package com.ht.scada.config.view.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.ht.scada.common.tag.entity.AcquisitionChannel;
import com.ht.scada.common.tag.entity.AcquisitionDevice;
import com.ht.scada.common.tag.entity.SensorDevice;
import com.ht.scada.common.tag.service.AcquisitionChannelService;
import com.ht.scada.common.tag.service.AcquisitionDeviceService;
import com.ht.scada.common.tag.service.SensorDeviceService;
import com.ht.scada.config.scadaconfig.Activator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * 通道配置内容提供器
 * 
 * @author 陈志强
 * 
 */
public class ScadaDeviceTreeContentProvider implements ITreeContentProvider {
	// private final Logger log =
	// LoggerFactory.getLogger(ScadaDeviceTreeContentProvider.class);

	private AcquisitionChannelService acquisitionChannelService = (AcquisitionChannelService) Activator
			.getDefault().getApplicationContext()
			.getBean("acquisitionChannelService");
	private AcquisitionDeviceService acquisitionDeviceService = (AcquisitionDeviceService) Activator
			.getDefault().getApplicationContext()
			.getBean("acquisitionDeviceService");

	private SensorDeviceService sensorDeviceService = (SensorDeviceService) Activator
			.getDefault().getApplicationContext()
			.getBean("sensorDeviceService");

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof String) {
			// TODO
			String str = (String) parentElement;
			if (str.equals("采集通道")) { // 采集通道
				List<AcquisitionChannel> acquisitionChannelList = acquisitionChannelService
						.getAllChannel();
				if (acquisitionChannelList != null) {
					return acquisitionChannelList.toArray();
				}
			}
		} else if (parentElement instanceof AcquisitionChannel) {
			List<Object> objectList = new ArrayList<Object>();
			List<AcquisitionDevice> acquisitionDeviceList = acquisitionDeviceService
					.getDeviceByChannelId(((AcquisitionChannel) parentElement)
							.getId());
			if (acquisitionDeviceList != null) {
				objectList.addAll(acquisitionDeviceList);
			}
			if (!objectList.isEmpty()) {
				return objectList.toArray();
			}
		} else if (parentElement instanceof AcquisitionDevice) {
			List<Object> objectList = new ArrayList<Object>();
			List<SensorDevice> sensorDeviceList = sensorDeviceService
					.getSensorByDeviceId(((AcquisitionDevice) parentElement)
							.getId());
			if (sensorDeviceList != null) {
				objectList.addAll(sensorDeviceList);
			}
			if (!objectList.isEmpty()) {
				return objectList.toArray();
			}
		}
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// if (inputElement.equals("通道配置")) { // 通道配置
		// List<AcquisitionChannel> acquisitionChannelList =
		// acquisitionChannelService
		// .getRootAcquisitionChannel();
		// if (acquisitionChannelList != null) {
		// return acquisitionChannelList.toArray();
		// }
		// }
		if (inputElement instanceof String) {
			return new String[] { "采集通道" };
		}

		return null;
	}

	@Override
	public Object getParent(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {
		Object[] children = this.getChildren(arg0);
		if (children == null) {
			return false;
		} else if (children.length > 0) {
			return true;
		} else {
			return false;
		}
	}

}
