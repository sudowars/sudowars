/*******************************************************************************
 * Copyright (c) 2011 - 2012 Adrian Vielsack, Christof Urbaczek, Florian Rosenthal, Michael Hoff, Moritz Lüdecke, Philip Flohr.
 * 
 * This file is part of Sudowars.
 * 
 * Sudowars is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sudowars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sudowars.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * 
 * Diese Datei ist Teil von Sudowars.
 * 
 * Sudowars ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 * 
 * Sudowars wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 * 
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * initial API and implementation:
 * Adrian Vielsack
 * Christof Urbaczek
 * Florian Rosenthal
 * Michael Hoff
 * Moritz Lüdecke
 * Philip Flohr 
 ******************************************************************************/
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
