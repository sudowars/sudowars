package org.sudowars.Controller.Local.BluetoothDeviceList;

import java.util.ArrayList;

import org.sudowars.R;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * List adapter feeds the ListView with data the Android way
 */
public class BluetoothDeviceListAdapter extends ArrayAdapter<BluetoothDeviceListItem> {
	/**
	 * the context
	 */
	private Context context;
	
	/**
	 * list of bluetooth devices
	 */
	private ArrayList<BluetoothDeviceListItem> devices;
	
	/**
	 * Constructor of {@link BluetoothDeviceListAdapter}
	 * 
	 * @param context the context
	 * @param textViewResourceId the resource id of text view
	 */
	public BluetoothDeviceListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	/**
	 * Constructs a new {@link BluetoothDeviceListAdapter}-Object.
	 * 
	 * @param context context of the activity
	 * @param textViewRessourceId the resource id of the TextView
	 * @param devices the bluetooth device
	 */
	public BluetoothDeviceListAdapter(Context context, int textViewRessourceId, ArrayList<BluetoothDeviceListItem> devices) {
		super(context, textViewRessourceId, devices);
		this.context = context;
		this.devices = devices;
	}

	/**
	 * This function get's called when a new part of the ListView is visible
	 * 
	 * @param postition the position
	 * @param convertView the part of the list view which get's casted in the function
	 * @param parent the ViewGroup
	 * 
	 * @return the view for the ListView item
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		if  (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.device_list_item, null);
		}
		
		BluetoothDeviceListItem device = devices.get(position);
		
		if (device != null) {
			TextView deviceName = (TextView) row.findViewById(R.id.lblDeviceName);
			TextView deviceMac = (TextView) row.findViewById(R.id.lblDeviceMac);
			ImageView deviceIcon = (ImageView) row.findViewById(R.id.imgDeviceIcon);
			
			if (device.isPaired()) {
				deviceIcon.setImageResource(R.drawable.stat_sys_tether_bluetooth);
			} else {
				deviceIcon.setImageResource(R.drawable.stat_sys_data_bluetooth);
			}

			deviceName.setText(device.getName());
			deviceMac.setText(device.getMac());
		}
		
		return row;
	}
}
