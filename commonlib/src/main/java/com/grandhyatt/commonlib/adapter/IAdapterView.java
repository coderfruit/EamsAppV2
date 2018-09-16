package com.grandhyatt.commonlib.adapter;


/**    
 * 绑定控件接口 
 * @author 老吴   
 * @date 2015-06-23 09:56   
 */  
@SuppressWarnings("hiding")
public interface IAdapterView<T> {
	public void bind(int position, T... t);
}
