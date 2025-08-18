package com.dpdtech.application.oper;

import android.annotation.SuppressLint;

/**
 * @author cdw 数据转换工具
 */
@SuppressLint("DefaultLocale")
public class FormatUtil {
	// -------------------------------------------------------
	// 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
	static public int isOdd(int num) {
		return num & 0x1;
	}

	// -------------------------------------------------------
	static public int HexToInt(String inHex)// Hex字符串转int
	{
		return Integer.parseInt(inHex, 16);
	}

	// -------------------------------------------------------
	static public byte HexToByte(String inHex)// Hex字符串转byte
	{
		return (byte) Integer.parseInt(inHex, 16);
	}

	// -------------------------------------------------------
	static public String Byte2Hex(Byte inByte)// 1字节转2个Hex字符
	{
		return String.format("%02x", inByte).toUpperCase();
	}

	// -------------------------------------------------------
	// 字节数组转转hex字符串,return：01 23 45 67 89
	static public String ByteArrToHex(byte[] inBytArr)
	{
		StringBuilder strBuilder = new StringBuilder();
		int j = inBytArr.length;
		for (int i = 0; i < j; i++) {
			strBuilder.append(Byte2Hex(inBytArr[i]));
			strBuilder.append(" ");
		}
		return strBuilder.toString();
	}

	// -------------------------------------------------------
	static public String ByteArrToHex(byte[] inBytArr, int offset, int byteCount)// 字节数组转转hex字符串，可选长度
	{
		StringBuilder strBuilder = new StringBuilder();
		int j = byteCount;
		for (int i = offset; i < j; i++) {
			strBuilder.append(Byte2Hex(inBytArr[i]));
		}
		return strBuilder.toString();
	}

	// -------------------------------------------------------
	// hex字符串转字节数组
	static public byte[] HexToByteArr(String inHex)// hex字符串转字节数组
	{
		int hexlen = inHex.length();
		byte[] result;
		if (isOdd(hexlen) == 1) {// 奇数
			hexlen++;
			result = new byte[(hexlen / 2)];
			inHex = "0" + inHex;
		} else {// 偶数
			result = new byte[(hexlen / 2)];
		}
		int j = 0;
		for (int i = 0; i < hexlen; i += 2) {
			result[j] = HexToByte(inHex.substring(i, i + 2));
			j++;
		}
		return result;
	}


	// 字节数组异或
	public static byte getXor(byte a[],int start_id,int end_id)
	{
		byte result=0;
		for(int i=start_id;i<=end_id;i++)
		{
			result^=a[i];
		}
		return result;
	}



	public static String sHexToString(String src)
	{
		return src.replace(" ", "");
	}

	public static char[] StringToChar(String src)
	{
		char[] temp = null;

		if(null != src)
		{
			temp = src.toCharArray();
		}


		return temp;
	}


}