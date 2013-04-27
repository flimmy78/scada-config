package com.ht.scada.config.view.tree;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.ht.scada.common.tag.entity.AcquisitionChannel;
import com.ht.scada.common.tag.entity.AcquisitionDevice;
import com.ht.scada.common.tag.entity.SensorDevice;

/**
 * 通道标签提供器
 * 
 * @author 陈志强
 * 
 */
public class ScadaDeviceTreeLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(Object object) {
		return null;
	}

	@Override
	public String getText(Object object) {
		if (object instanceof String) {
			return (String) object;
		} else if (object instanceof AcquisitionChannel) {
			return ((AcquisitionChannel) object).getName();
		} else if (object instanceof AcquisitionDevice) {
			return ((AcquisitionDevice) object).getName();
		}else if (object instanceof SensorDevice) {
			return ((SensorDevice) object).getName();
		}
		return null;
	}

}
