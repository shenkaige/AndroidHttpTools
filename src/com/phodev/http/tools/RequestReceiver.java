package com.phodev.http.tools;

public interface RequestReceiver {
	public static final int RESULT_STATE_OK = 0;
	public static final int RESULT_STATE_SERVER_ERROR = -1;
	public static final int RESULT_STATE_NETWORK_ERROR = -2;
	public static final int RESULT_STATE_TIME_OUT = -3;

	public void onResult(int resultCode, int reqId, Object tag, String resp);

	public void onRequestCanceled(int reqId, Object tag);
}