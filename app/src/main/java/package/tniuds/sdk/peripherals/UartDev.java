
package com.tniuds.sdk.peripherals;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dpdtech.application.oper.FormatUtil;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class UartDev {

    private static final String TAG = "UartDev";

    // 串口通用配置
    private	String dev;   			// 设备文件    eg: "/dev/ttyS1"
    private int baudrate;			// 波特率 	eg:	9600、115200
    private int dataBits;			// 数据位 	eg:	8
    private int stopBits;			// 停止位        eg:	0--无停止位，1--1位停止位
    private char parity;			// 奇偶校验位 eg:'O'--奇校验，'E'--偶校验, 'N'--无校验

    // 串口RX特殊配置
    private boolean rxFormatHex ;	// 串口RX接收格式，文本格式、十六进制格式
    private Handler rxHandler;     	// 串口RX接收回调句柄


    // 串口
    private uartFile Fuart;
    private uartTx Tx;
    private uartRx Rx;

    public UartDev(String fdev, int band)
    {
        setUartConfig(fdev,band,false,null);
    }

    public UartDev(String fdev, int band, boolean _rxFormatHex, Handler _rxHandler)
    {
        setUartConfig(fdev,band,_rxFormatHex,_rxHandler);
    }

    private void setUartConfig(String fdev, int band,boolean _rxFormatHex,Handler _rxHandler)
    {
        dev = fdev;
        baudrate = band;
        rxFormatHex = _rxFormatHex;
        rxHandler = _rxHandler;
        setDataBits(8); // 8位数据位,默认
        setStopBits(1); // 1位停止位,默认
        setParity('N');  // 无校验位,默认
    }

    /*========================================================================================================
     *                          public
     ========================================================================================================*/
    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public char getParity() {
        return parity;
    }

    public void setParity(char parity) {
        this.parity = parity;
    }


    public void uartOpen()
    {
        Fuart = new uartFile(dev,baudrate,dataBits,stopBits,parity);

        Tx = new uartTx();

        Rx = new uartRx();
        Rx.open();

    }


    public void uartClose()
    {
        if(Rx != null)
        {
            Rx.close();
            Rx = null;
            rxHandler = null;
        }

        Tx = null;

        if(Fuart != null)
        {
            Fuart.fileClose();
            Fuart = null;

        }


    }


    public boolean uartSend(boolean txFormatHex,String buf)
    {
        if(Tx != null)
        {
            return Tx.send(txFormatHex, buf);
        }

        return false;
    }


    public  class uartRx
    {
        private ThreadRx threadRx;				// 串口RX线程
        private final  int delayMsRxLoop = 200;	// 串口RX 每x毫秒循环监听

        public void open()
        {
            if(rxHandler != null)
            {
                threadRx =  new ThreadRx();
                threadRx.start();
            }

        }

        private String receive()
        {
            String bufRxS =null;
            byte[] bufRxB =null;

            if( null ==Fuart )
            {
                return null;
            }

            bufRxB = Fuart.fileRead();
            if( null == bufRxB )
            {
                return null;
            }

            if(rxFormatHex)
            {
                bufRxS= FormatUtil.ByteArrToHex(bufRxB);
            }
            else
            {
                bufRxS =new String(bufRxB);
            }


            return bufRxS;
        }

        private void close()
        {
            if(threadRx != null)
            {
                threadRx.interrupt();
            }

        }

        private class ThreadRx extends Thread
        {
            @Override
            public void run()
            {
                // 轮询rx
                if(!isInterrupted())
                {
                    try
                    {
                        onRxEvent();

                        if(rxHandler !=null)
                        {
                            rxHandler.postDelayed(this,delayMsRxLoop);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

            }
        }

        private void onRxEvent()
        {
            String rbuf=null;

            rbuf = receive();
            if(null != rbuf)
            {
                doRxEventNotifyUser(rbuf);
            }

        }

        private void doRxEventNotifyUser(String rbuf)
        {
            /*--把接到的数据发送给处理句柄--*/
            if(rxHandler !=null)
            {
                Message Msg = rxHandler.obtainMessage();
                //Msg.what = dev_id;
                Msg.obj = rbuf.toString();
                rxHandler.sendMessage(Msg);
            }
        }


    }


    public  class uartTx
    {
        private final  int UART_HW_BUF_MAX_BYTES  = 4*1024-1; 	// 硬件串口发送缓冲区支持的最大buf，4095字节

        public  boolean send(boolean formatHex,String buf)
        {

            // check
            if(buf.length() > UART_HW_BUF_MAX_BYTES)
            {
                return false;
            }

            byte[] txData ;

            try
            {
                if(formatHex)
                {
                    txData = FormatUtil.HexToByteArr(buf);
                }
                else
                {
                    txData = buf.getBytes();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }

            return Fuart.fileWrite(txData);


        }


    }


    public class uartFile
    {
        //--uart file 属性--//
        private FileDescriptor mFd;
        private FileInputStream mFileInputStream;
        private FileOutputStream mFileOutputStream;


        public  uartFile(String fdev, int baudrate, int dataBits,int stopBits,char parity)
        {
            try
            {
                fileOpen(new File(fdev),baudrate,dataBits,stopBits,parity);
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
        }

        public boolean fileWrite(byte[] buf,int offset,int length)
        {
            if(null == mFileOutputStream)
            {
                return false;
            }

            try
            {
                mFileOutputStream.write(buf,offset,length);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        public boolean fileWrite(byte[] buf)
        {
            if(null == mFileOutputStream)
            {
                return false;
            }

            try
            {
                mFileOutputStream.write(buf);
                return true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }
        }

        public byte[]  fileRead()
        {
            if(null == mFileInputStream)
            {
                return null;
            }

            try
            {
                int available = mFileInputStream.available();

                byte[] buf_temp = new byte[available];

                int read_size = mFileInputStream.read(buf_temp);
                if(read_size>0)
                {
                    return buf_temp;
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }

            return null;
        }


        public void fileOpen(File device, int baudrate, int dataBits,int stopBits,char parity)throws SecurityException, IOException
        {
            /* Check access permission */
            if (!device.canRead() || !device.canWrite())
            {
                try
                {
                    /* Missing read/write permission, trying to chmod the file */
                    Process su;
                    su = Runtime.getRuntime().exec("/system/bin/su");
                    String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
                    su.getOutputStream().write(cmd.getBytes());
                    if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite())
                    {
                        throw new SecurityException();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    throw new SecurityException();
                }
            }

            mFd = open(device.getAbsolutePath(), baudrate, dataBits,stopBits,parity);
            if (mFd == null)
            {
                Log.e(TAG, "native open returns null");
                throw new IOException();
            }

            mFileInputStream = new FileInputStream(mFd);
            mFileOutputStream = new FileOutputStream(mFd);

        }

        public void fileClose()
        {
            if( null != mFd)
            {
                try
                {
                    mFileInputStream.close();
                    mFileInputStream = null;

                    mFileOutputStream.close();
                    mFileOutputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                close(mFd);

                mFd = null;
            }


        }

    }

    /*--声明jni方法--*/
    private native static FileDescriptor open(String path, int baudrate,int dataBits,int stopBits,char parity);
    private native void close(FileDescriptor fd);
    static {
        System.loadLibrary("uart_jni");
    }
}