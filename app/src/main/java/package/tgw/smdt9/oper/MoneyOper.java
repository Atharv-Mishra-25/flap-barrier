package com.dpdtech.application.oper;


public class MoneyOper {
    //private String TAG = "MoneyOper";

    /*--纸币机故障代码：20H(32) 马达故障--*/
    /*--纸币机故障代码：21H(33) 校验码故障--*/
    /*--纸币机故障代码：22H(34) 卡币--*/
    /*--纸币机故障代码：23H(35) 纸币移开--*/
    /*--纸币机故障代码：24H(36) 钱箱移开--*/
    /*--纸币机故障代码：25H(37) 电眼故障--*/
    /*--纸币机故障代码：27H(39) 钓鱼--*/

    /*--纸币机命令--*/
    public final static int cmd_handshake = 0x00;
    public final static int cmd_money_true= 0x01;
    public final static int cmd_respond   = 0x02;        //纸币机与主板握手命令
    public final static int cmd_money_accept      =0x02; //接收纸币
    public final static int cmd_money_status      =0x0c; //请求纸币机状态
    public final static int cmd_money_rfused      =0x0f;//拒收
    public final static int cmd_money_staging     =0x18;//暂存
    public final static int cmd_money_false       =0x29; //纸币机拒绝， 退币
    public final static int cmd_money_enalbe      =0x3e;  //使能纸币机
    public final static int cmd_money_disabled    =0x5e; //禁能纸币机
    public final static int cmd_moeny_first_type  = 0x40;//面额 1元
    public final static int cmd_moeny_second_type = 0x41;//面额 5元
    public final static int cmd_moeny_third_type  = 0x42;//面额 10元
    public final static int cmd_moeny_fourth_type = 0x43;//面额 20元
    public final static int cmd_moeny_fifth_type  = 0x44;//面额 50元


    /*--找币机故障代码：0501000304000D-------All status is fine  一切状况良好*/
    /*--找币机故障代码：0501000304010E------- Motor problem  电动机问题*/
    /*--找币机故障代码：0501000304020F-------  Insufficient Coins 硬币不足*/
    /*--找币机故障代码：05010003040310-------  Reserve  保留*/
    /*--找币机故障代码：05010003040411-------  Prism Sensor Failure  棱镜传感器故障*/
    /*--找币机故障代码：05010003040512-------  Shaft Sensor Failure  轴传感器故障*/
    /*--找币机故障代码：05010003040613-------  Dispenser is busy  分配器占线障*/

    /*--找币机命令--*/
    public final static String cmd_money_rest ="05100312002A";//重置找币机
    public final static String cmd_money_status2 ="051003110029";//料斗转态查询
    public final static String cmd_money_payoutOneCoin = "05100314012D"; //支付一枚硬币，有回复
    public final static String cmd_money_payoutOneCoin2 = "051003100129"; //支付一枚硬币，无回复
    public final static String cmd_money_payoutSixCoin = "051003140632"; //支付六枚硬币，有回复

    public final static String cmd_money_payoutCoin2 = "05100314022E"; //支付 2 枚硬币，有回复
    public final static String cmd_money_payoutCoin3 = "05100314032F"; //支付 3 枚硬币，有回复
    public final static String cmd_money_payoutCoin4 = "051003140430"; //支付 4 枚硬币，有回复
    public final static String cmd_money_payoutCoin5 = "051003140531"; //支付 5 枚硬币，有回复
    public final static String cmd_money_payoutCoin6 = "051003140632"; //支付 6 枚硬币，有回复
    public final static String cmd_money_payoutCoin7 = "051003140733"; //支付 7 枚硬币，有回复
    public final static String cmd_money_payoutCoin8 = "051003140834"; //支付 8 枚硬币，有回复
    public final static String cmd_money_payoutCoin9 = "051003140935"; //支付 9 枚硬币，有回复
    public final static String cmd_money_payoutCoin10 = "051003140A36"; //支付 10 枚硬币，有回复
    public final static String cmd_money_payoutCoin11 = "051003140B37"; //支付 11 枚硬币，有回复
    public final static String cmd_money_payoutCoin12 = "051003140C38"; //支付 12 枚硬币，有回复
    public final static String cmd_money_payoutCoin13 = "051003140D39"; //支付 13 枚硬币，有回复
    public final static String cmd_money_payoutCoin14 = "051003140E3A"; //支付 14 枚硬币，有回复
    public final static String cmd_money_payoutCoin15 = "051003140F33B"; //支付 15 枚硬币，有回复
    public final static String cmd_money_payoutCoin16 = "05100314103C"; //支付 16 枚硬币，有回复
    public final static String cmd_money_payoutCoin17 = "05100314113D"; //支付 17 枚硬币，有回复
    public final static String cmd_money_payoutCoin18 = "05100314123E"; //支付 18 枚硬币，有回复
    public final static String cmd_money_payoutCoin19 = "05100314133F"; //支付 19 枚硬币，有回复
    public final static String cmd_money_payoutCoin20 = "051003141440"; //支付 20 枚硬币，有回复

    /*--请求付一枚硬币，有回复，收到三条回复--*/
    //  TX   05100314012D   payout active reply  支付一枚硬币，有回复
    //  RX   050103AA01B4   response ack or remain coin  响应ACK或保持硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103080011   payout finished    支付完成

    /*--请求付一枚硬币，无回复 ，收到一条回复--*/
    //  TX   051003100129   payout NO active reply  支付一枚硬币，无回复
    //  RX   050103AA01B4   response ack or remain coin  响应ACK或保持硬币

    /*--转态查询 ，收到一条回复--*/
    //  TX   051003110029   status query  状态查询
    //  RX   05010304000D   no error  无错误

    /*--硬币不足回复命令--*/
    //  RX   05010304020F  insufficient coins 硬币不足


    /*--请求付六枚硬币，有回复，收到八条回复--*/  //硬币充足的情况
    //  TX   051003140632   payout active reply  支付六枚硬币，有回复
    //  RX   050103AA06B9   response ack or remain coin  响应ACK或保持硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103080011   payout finished    支付完成


    /*--请求付六枚硬币，有回复，收到九条回复--*/  //硬币可能不足的情况
    //  TX   051003140632   payout active reply  支付六枚硬币，有回复
    //  RX   050103AA06B9   response ack or remain coin  响应ACK或保持硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103070010   payout one coin   支付一枚硬币
    //  RX   050103080011   payout finished    支付完成
    //  RX   05010304020F  insufficient coins 硬币不足（本次交易完成，请停止下次交易）


    /*--请求付六枚硬币，无回复，收到一条回复--*/  //硬币充足的情况
    //  TX   05100310062E   payout NO active reply  支付六枚硬币，无回复
    //  RX   050103AA06B9   response ack or remain coin  响应ACK或保持硬币

    /*--请求付六枚硬币，无回复，收到一条回复--*/  //硬币不足的情况
    //  TX   05100310062E   payout NO active reply  支付六枚硬币，无回复
    //  RX   050103AA06B9   response ack or remain coin  响应ACK或保持硬币



    /*--打印机命令--*/
    public final static String cmd_print_test = "1D284102000002";  //测试打印机
    public final static String cmd_print_status="100404";//状态查询

    public final static String cmd_print_status1="1D7201";  //状态查询

    public final static String cmd_print_initi="1B40"; //初始化
    public final static String cmd_print_enter="0A";//换行
    public final static String cmd_print_content_header="1C26";//内容头
    public final static String cmd_print_content="3132"; // 12
    public final static String cmd_print_cutpaper="1B69";//全切

    public static String GetMoney(int cmd)
    {
        switch(cmd)
        {
            case cmd_moeny_first_type: return "1";
            case cmd_moeny_second_type: return "5";
            case cmd_moeny_third_type: return "10";
            case cmd_moeny_fourth_type: return "20";
            case cmd_moeny_fifth_type: return "50";
        }
        return "0";
    }






}
