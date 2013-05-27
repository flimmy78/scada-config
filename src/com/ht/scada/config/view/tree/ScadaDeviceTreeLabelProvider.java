package com.ht.scada.config.view.tree;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.ht.scada.common.tag.entity.AcquisitionChannel;
import com.ht.scada.common.tag.entity.AcquisitionDevice;
import com.ht.scada.common.tag.entity.SensorDevice;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.ImagePath;

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
		if(object instanceof AcquisitionChannel) {
			return Activator.getDefault().getImageDescriptor(ImagePath.ACQUISITION_CHANNEL_IMAGE).createImage();
		} else if(object instanceof AcquisitionDevice) {
			return Activator.getDefault().getImageDescriptor(ImagePath.ACQUISITION_DEVICE_IMAGE).createImage();
		} else if(object instanceof String) {
			return Activator.getDefault().getImageDescriptor(ImagePath.ACQUISITION_CHANNEL_INDEX_IMAGE).createImage();
		}
		return null;
	}

	@Override
	public String getText(Object object) {
		if (object instanceof String) {
			return (String) object;
		} else if (object instanceof AcquisitionChannel) {
			return ((AcquisitionChannel) object).getName() + ":"+ ((AcquisitionChannel) object).getIdx();
		} else if (object instanceof AcquisitionDevice) {
			return ((AcquisitionDevice) object).getName() +":"+ ((AcquisitionDevice) object).getAddress();
		}else if (object instanceof SensorDevice) {
			return ((SensorDevice) object).getName();
		}
		return null;
	}

}
