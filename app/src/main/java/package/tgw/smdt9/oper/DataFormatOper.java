package com.dpdtech.application.oper;

import android.annotation.SuppressLint;

/**
 * @author cdw 数据转换工具
 */
@SuppressLint("DefaultLocale")
public class DataFormatOper {
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
	static public String ByteArrToHex(byte[] inBytArr)// 字节数组转转hex字符串
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
	public static byte getXor(byte a[],int start,int end)
	{
		byte result=0;
		for(int i=start;i<=end;i++) {
			result^=a[i-1];
		}
		return result;
	}



	String str2HexStr(String str)
	{
		char[] chars ="0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++)
		{
			bit = (bs[i] &0x0f0) >>4;
			sb.append(chars[bit]);
			bit = bs[i] &0x0f;
			sb.append(chars[bit]);
			// sb.append(' ');
		}

		return sb.toString().trim();
	}

	//字符串转 16 进制 （支持中文）
	public String toChineseHex(String s)
	{
		String ss = s;
		byte[] bt = new byte[0];

		try {
			bt = ss.getBytes("gbk");
		}catch (Exception e){
			e.printStackTrace();
		}
		String s1 = "";
		for (int i = 0; i < bt.length; i++)
		{
			String tempStr = Integer.toHexString(bt[i]);
			if (tempStr.length() > 2)
				tempStr = tempStr.substring(tempStr.length() - 2);
			s1 = s1 + tempStr + "";
		}
		return s1.toUpperCase();
	}

//	public String toHexString(String str)
//	{
//		String hexString="";
//		String[] escapeArray={"\b","\t","\n","\f","\r"};
//		boolean flag=false;
//		for(String esacapeStr:escapeArray){
//			if(str.contains(esacapeStr)){
//				flag = true;
//				break;
//			}
//		}
//		if(flag) throw new Exception("参数字符串不能包含转义字符！");
//
//		char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
//
//		StringBuilder sb=new StringBuilder();
//		byte[] bs=str.getBytes();
//		int bit;
//		for (int i=0;i<bs.length;i++){
//			bit=(bs[i]& 0x0f0)>>4;
//			sb.append(hexArray[bit]);
//			bit = bs[i] & 0x0f;
//			sb.append(hexArray[bit]);
//		}
//		hexString = sb.toString();
//		return hexString;
//
//	}


	public String hexToStr(String hexStr)
	{
		String str ="0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes =new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++)
		{
			n = str.indexOf(hexs[2 * i]) *16;
			n += str.indexOf(hexs[2* i +1]);
			bytes[i] = (byte) (n &0xff);
		}

		return new String(bytes);
	}



}