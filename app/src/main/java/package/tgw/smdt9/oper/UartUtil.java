/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dpdtech.application.oper;

import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.Vector;

public class UartUtil {

	// uart name 
	public  final static String  UART_DEV0 = "/dev/ttyS0";
	public  final static String  UART_DEV1 = "/dev/ttyS1";
	public  final static String  UART_DEV2 = "/dev/ttyS2";
	public  final static String  UART_DEV3 = "/dev/ttyS3";
	public  final static String  UART_DEV4 = "/dev/ttyS4";
	public  final static String  UART_DEVXRM0 = "/dev/XRM0";
	public  final static String  UART_DEV21 = "/dev/ttyUSB21";
	public  final static String  UART_DEV22 = "/dev/ttyUSB22";
	public  final static String  UART_DEV23 = "/dev/ttyUSB23";
	public  final static String  UART_DEV24 = "/dev/ttyUSB24";

	public  final static String  UART_DEV30 = "/dev/ttyUSB30";
	public  final static String  UART_DEV40 = "/dev/ttyUSB40";
	public  final static String  UART_DEV41 = "/dev/ttyUSB41";
	public  final static String  UART_DEV42 = "/dev/ttyUSB42";
	public  final static String  UART_DEV43 = "/dev/ttyUSB43";
	public  final static String  UART_DEV44 = "/dev/ttyUSB44";



	public  final static String  UART_DEV51 = "/dev/ttyUSB51";
	public  final static String  UART_DEV52 = "/dev/ttyUSB52";
	public  final static String  UART_DEV53 = "/dev/ttyUSB53";
	public  final static String  UART_DEV54 = "/dev/ttyUSB54";
	public  final static String  UART_DEV55 = "/dev/ttyUSB55";
	public  final static String  UART_DEV57 = "/dev/ttyUSB57";

	// uart baudrate
	public final static int UART_B4800 		= 4800;
	public final static int UART_B9600 		= 9600;
	public final static int UART_B115200 	= 115200;
	public final static int UART_B19200 	= 19200;
	public final static int UART_B230400 	= 230400;
	public final static int UART_B921600 	= 921600;
	public final static int UART_B1152000 	= 1152000;
	public final static int UART_B1500000 	= 1500000;
	public final static int UART_B2000000 	= 2000000;
	public final static int UART_B3000000 	= 3000000;
	public final static int UART_B4000000 	= 4000000;

	// uart dataBits
	public final static int UART_DATA_BIT_7  = 7;
	public final static int UART_DATA_BIT_8  = 8;

	// uart stopBits
	public final static int UART_STOP_BIT_0  = 0;
	public final static int UART_STOP_BIT_1  = 1;

	// uart parity 
	public final static char UART_PARITY_O  = 'O'; // 奇校验
	public final static char UART_PARITY_E  = 'E'; // 偶校验
	public final static char UART_PARITY_N  = 'N'; // 无校验

	private static final String TAG = "SerialPort";
	private static Vector<Driver> mDrivers = null;

	static Vector<Driver> getDrivers() throws IOException
	{
		if (mDrivers == null)
		{
			mDrivers = new Vector<Driver>();
			LineNumberReader r = new LineNumberReader(new FileReader("/proc/tty/drivers"));
			String l;
			while((l = r.readLine()) != null)
			{
				String drivername = l.substring(0, 0x15).trim();
				String[] w = l.split(" +");
				if ((w.length >= 5) && (w[w.length-1].equals("serial")))
				{
					Log.d(TAG, "---串口设备=" + drivername + " on " + w[w.length-4]);

					Driver diver  = new Driver(drivername, w[w.length-4]);
					mDrivers.add(diver);
				}
			}
			r.close();
		}

		return mDrivers;
	}


	public static String[] getUartDevicesAll()
	{
		Vector<String> devices = new Vector<String>();
		Iterator<Driver> itdriv;
		try {
			itdriv = getDrivers().iterator();
			while(itdriv.hasNext()) {
				Driver driver = itdriv.next();
				Iterator<File> itdev = driver.getDevices().iterator();
				while(itdev.hasNext()) {
					String device = itdev.next().getAbsolutePath();
					devices.add(device);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return devices.toArray(new String[devices.size()]);
	}

	public static class Driver
	{
		private String mDriverName;
		private String mDeviceRoot;
		Vector<File> mDevices = null;

		public Driver(String name, String root)
		{
			mDriverName = name;
			mDeviceRoot = root;
		}

		public Vector<File> getDevices()
		{
			if (mDevices == null)
			{
				mDevices = new Vector<File>();
				File dev = new File("/dev");
				File[] files = dev.listFiles();
				if(null == files || files.length ==0)
				{
					return mDevices;
				}
				int i;
				for (i=0; i<files.length; i++)
				{
					if (files[i].getAbsolutePath().startsWith(mDeviceRoot))
					{
						Log.d(TAG, "查找的设备: " + files[i]);
						mDevices.add(files[i]);
					}
				}
			}
			return mDevices;
		}

		public String getName()
		{
			return mDriverName;
		}
	}
}
