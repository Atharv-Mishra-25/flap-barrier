package com.dpdtech.application.oper;

import com.google.seria.port.SerialPort;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class UartOper {
	FileOutputStream mOutputStream;
	FileInputStream mInputStream;
	SerialPort com;

	boolean isInit=false;

	/*--串口初始化--*/
	public void UartInit(String patch,int baudrate, int dataBits,int stopBits,char parity)
	{
		try {
			com=new SerialPort(new File(patch), baudrate, dataBits,stopBits,parity);
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		isInit=true;
		mInputStream=(FileInputStream) com.getInputStream();
		mOutputStream=(FileOutputStream) com.getOutputStream();

	}

	/*--串口发送数据，可将数据转成十六进制
	 *--若要发十六进制，可ComSend("010300000002C40B",false);
	 */
	public void UartSend(String wbuf,boolean IsTxt)
	{
		byte[] bOutArray;

		if(IsTxt)
		{
			bOutArray= wbuf.getBytes();
		}
		else
		{

			bOutArray= DataFormatOper.HexToByteArr(wbuf);
		}

		try {


			mOutputStream.write(bOutArray);
			//mOutputStream.write('\n');
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}


	/*--串口接收数据，可接收十六进制并转成字符串--*/
	public String UartReceive(boolean IsTxt,int length)
	{
		int size;
		//String rbuf="00 00 00 00 00 00 00 00 00";
		String rbuf =null;
		try
		{
			byte[] buffer = new byte[length];
			if (mInputStream == null) return rbuf;
			size = mInputStream.read(buffer);
			if (size > 0)
			{
				if(IsTxt)
				{
					rbuf=new String(buffer, 0, size);
				}
				else
				{
					rbuf=DataFormatOper.ByteArrToHex(buffer);
				}

			}
		} catch (IOException e)
		{
			e.printStackTrace();
			return rbuf;
		}

		return rbuf;
	}

	/*--延时--*/
	public void delay(int ms)
	{

	}


	/*--是否初始化串口--*/
	public boolean IsInit()
	{
		return this.isInit;
	}

}
