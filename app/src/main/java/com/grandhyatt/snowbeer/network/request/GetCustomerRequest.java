package com.grandhyatt.snowbeer.network.request;

/**
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 11:27
 */
public class GetCustomerRequest {

    private String customerCode;

    public GetCustomerRequest() {
    }

    public GetCustomerRequest(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * 获取 customerCode
     *
     * @return 返回 customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * 设置 customerCode
     *
     * @return 返回 customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    @Override
    public String toString() {
        return "GetCustomerRequest{" +
                "customerCode='" + customerCode + '\'' +
                '}';
    }
}
