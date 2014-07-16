package com.phodev.http.tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.phodev.http.tools.ConnectionHelper.RequestMethod;
import com.phodev.http.tools.ConnectionHelper.RequestReceiver;

/**
 * 请求实例
 * 
 * @author sky
 */
public class RequestEntity {
	private String url;
	private HttpEntity postEntity;
	private RequestReceiver requestReceiver;
	private String rawResponse;
	private RequestMethod requestMethod;
	private int resultCode;
	private int requestId;
	private String defCharset;
	private Object mTag;

	private RequestEntity() {

	}

	private RequestEntity(String url, RequestMethod method) {
		url(url);
		method(method);
	}

	public String getUrl() {
		return url;
	}

	public RequestEntity url(String url) {
		this.url = url;
		return this;
	}

	/**
	 * Post需要发送的数据
	 * 
	 * @return
	 */
	public HttpEntity getPostEntitiy() {
		return postEntity;
	}

	/**
	 * Post需要发送的数据
	 * 
	 * @return
	 */
	public RequestEntity setPostEntitiy(HttpEntity entity) {
		postEntity = entity;
		return this;
	}

	public RequestEntity setPostEntitiy(List<NameValuePair> postValues) {
		setPostEntitiy(postValues, defCharset);
		return this;
	}

	public RequestEntity setPostEntitiy(List<NameValuePair> postValues,
			String charset) {
		try {
			postEntity = new UrlEncodedFormEntity(postValues, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this;
	}

	public RequestEntity setPostEntitiy(List<NameValuePair> postValues,
			Map<String, File> files) {
		setPostEntitiy(postValues, defCharset, files);
		return this;
	}

	/**
	 * 带文件上传的POST
	 * 
	 * <pre>
	 * RequestMethod必须是{@link ConnectionHelper.RequestMethod#POST_WITH_FILE}}模式
	 * </pre>
	 * 
	 * @param postValues
	 * @param files
	 * @param charset
	 */
	public RequestEntity setPostEntitiy(List<NameValuePair> postValues,
			String charset, Map<String, File> files) {
		Charset c = null;
		try {
			c = Charset.forName(charset);
		} catch (Exception e) {
			c = null;
		}
		MultipartEntity entity;
		HttpMultipartMode mode = HttpMultipartMode.BROWSER_COMPATIBLE;
		if (c == null) {
			entity = new MultipartEntity(mode);
		} else {
			entity = new MultipartEntity(mode, null, c);
		}
		postEntity = entity;
		if (postValues != null) {
			for (NameValuePair v : postValues) {
				try {
					entity.addPart(v.getName(), new StringBody(v.getValue()));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		if (files != null) {
			Iterator<Entry<String, File>> iterator = files.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, File> entry = iterator.next();
				entity.addPart(entry.getKey(), new FileBody(entry.getValue()));
			}
		}
		return this;
	}

	public RequestEntity setPostEntitiy(String querryString, String charset) {
		try {
			postEntity = new StringEntity(querryString, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this;
	}

	protected RequestReceiver getRequestReceiver() {
		return requestReceiver;
	}

	/**
	 * Http Reqeust Receiver
	 * 
	 * @param receiver
	 * @return
	 */
	protected RequestEntity requestReceiver(RequestReceiver receiver) {
		this.requestReceiver = receiver;
		return this;
	}

	public String getRawResponse() {
		return rawResponse;
	}

	protected RequestEntity setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
		return this;
	}

	/**
	 * Get,Post
	 * 
	 * @return
	 */
	public RequestMethod getMethod() {
		return requestMethod;
	}

	/**
	 * Get,Post
	 * 
	 * @param method
	 *            {@link RequestMethod}
	 * @return
	 */
	public RequestEntity method(RequestMethod method) {
		this.requestMethod = method;
		return this;
	}

	public int getResultCode() {
		return resultCode;
	}

	protected RequestEntity setResultCode(int resultCode) {
		this.resultCode = resultCode;
		return this;
	}

	public int getRequestId() {
		return requestId;
	}

	public RequestEntity requestId(int requestId) {
		this.requestId = requestId;
		return this;
	}

	public String getDefaultCharset() {
		return defCharset;
	}

	public RequestEntity setDefaultCharset(String charset) {
		this.defCharset = charset;
		return this;
	}

	public RequestEntity setTag(Object tag) {
		mTag = tag;
		return this;
	}

	public Object getTag() {
		return mTag;
	}

	private boolean isCanceled = false;
	private boolean isCancelStateSend = false;
	private long requestHandler = makeNextRequestIndex();

	/**
	 * 获取请求句柄
	 * 
	 * @return
	 */
	public long getRequestHandler() {
		return requestHandler;
	}

	protected boolean isCancelStateSend() {
		return isCancelStateSend;
	}

	protected void setCancelStateSend(boolean send) {
		isCancelStateSend = send;
	}

	protected void setCanceled(boolean canceled) {
		this.isCanceled = canceled;
	}

	protected boolean isCanceled() {
		return isCanceled;
	}

	private Future<?> requestTaskFuture;

	protected void setRequestTaskFuture(Future<?> future) {
		requestTaskFuture = future;
	}

	protected Future<?> getRequestTaskFuture() {
		return requestTaskFuture;
	}

	private final static List<RequestEntity> recyleList = new ArrayList<RequestEntity>();

	public static RequestEntity obtain() {
		if (recyleList.size() <= 0) {
			return new RequestEntity();
		} else {
			return recyleList.remove(0);
		}
	}

	private static long last_reqeust_index;

	/**
	 * 生成下一个ReqeustEntitiy对应的id
	 * 
	 * @return
	 */
	private static long makeNextRequestIndex() {
		synchronized (RequestEntity.class) {
			long id = System.currentTimeMillis();
			if (id == last_reqeust_index) {
				id++;
			}
			last_reqeust_index = id;
			return last_reqeust_index;
		}
	}

	public synchronized void recycle() {
		url = null;
		postEntity = null;
		requestReceiver = null;
		rawResponse = null;
		requestMethod = null;
		resultCode = 0;
		defCharset = null;
		isCanceled = false;
		isCancelStateSend = false;
		requestHandler = makeNextRequestIndex();
		setRequestTaskFuture(null);
		if (recyleList.size() < 6) {
			recyleList.add(this);
		}
	}
	// support upload progress track
	// public class CustormMultipartEntity extends MultipartEntity {
	// private RequestEntity requestEntity;
	// public CustormMultipartEntity(RequestEntity re) {
	// requestEntity = re;
	// }
	//
	// @Override
	// public void writeTo(OutputStream outstream) throws IOException {
	// super.writeTo(new ProgressOutputStream(outstream));
	// }
	//
	// class ProgressOutputStream extends OutputStream {
	// OutputStream targetOutputStream;
	//
	// ProgressOutputStream(OutputStream target) {
	// targetOutputStream = target;
	// }
	//
	// @Override
	// public void write(byte[] buffer) throws IOException {
	// targetOutputStream.write(buffer);
	// }
	//
	// @Override
	// public void write(byte[] buffer, int offset, int count)
	// throws IOException {
	// targetOutputStream.write(buffer, offset, count);
	// }
	//
	// @Override
	// public void write(int oneByte) throws IOException {
	// targetOutputStream.write(oneByte);
	// }
	//
	// }
	// }
}