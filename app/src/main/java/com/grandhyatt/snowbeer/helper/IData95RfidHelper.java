package com.grandhyatt.snowbeer.helper;

import com.android.RfidControll;
import com.grandhyatt.commonlib.utils.StringUtils;

/**
 * iData95 RFID Helper
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/19 22:59
 */
public class IData95RfidHelper implements IRfidReader {

    private RfidControll mRFID;

    public IData95RfidHelper() {
        mRFID = new RfidControll();
    }

    @Override
    public int initDevice() {
        try {
            return mRFID.API_OpenComm();
        } catch (Exception ex) {
            return -1;
        }
    }

    @Override
    public void closeDevice() {
        try {
            mRFID.API_CloseComm();
        } catch (Exception ex) {

        }
    }

    @Override
    public String readRfidCardData() {
        initDevice();
        String strRfidData = "";
        try {
            int res = 0;
            byte[] uid = new byte[4];
            byte[] pdata = new byte[1];
            pdata[0] = 0x00;
            byte buffer[] = new byte[256];
            res = mRFID.API_MF_Request(0, 0x26, buffer);
            if (res == 0) {
                res = mRFID.API_MF_Anticoll(0, pdata, buffer);
                if (res == 0) {
                    System.arraycopy(buffer, 0, uid, 0, 4);
                    strRfidData = StringUtils.bytesToHexString(uid, 4);
                    return strRfidData;
                }else{
                    return "";
                }
//                if (res == 0) {
//                    res = mRFID.API_MF_Select(0, uid, buffer);
//                }
            }else{
                return "";
            }
        } catch (Exception e) {
            return "";
        }finally {
            closeDevice();
        }
    }


}
