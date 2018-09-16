package com.grandhyatt.snowbeer.network.request;

/**
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 11:27
 */
public class GetOrganizationListRequest {

    private int pageIndex;
    private int pageSize;

    public GetOrganizationListRequest() {

    }

    public GetOrganizationListRequest(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    /**
     * 获取 pageIndex
     *
     * @return 返回 pageIndex
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * 设置 pageIndex
     *
     * @return 返回 pageIndex
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 获取 pageSize
     *
     * @return 返回 pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置 pageSize
     *
     * @return 返回 pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "GetOrganizationListRequest{" +
                "pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }
}
