package com.phodev.http.tools.receivers;

import com.phodev.http.tools.RequestReceiver;

public abstract class RichRequestReceiver implements RequestReceiver {
	@Override
	public final void onResult(int resultCode, int reqId, Object tag,
			String resp) {
		onBeforeDispatch(reqId, resultCode, resp);
		if (!doResult(reqId, resultCode, resp)) {
			if (resultCode == RequestReceiver.RESULT_STATE_OK) {
				doSuccess(reqId, resultCode, resp);
			} else {
				doFailed(reqId, resultCode, resp);
			}
		}
		onEndDispatch(reqId, resultCode, resp);
	}

	/**
	 * 在Result开始分发之前
	 */
	public void onBeforeDispatch(int reqId, int resultCode, String resp) {

	}

	/**
	 * 在Result开始分发之后
	 */
	public void onEndDispatch(int reqId, int resultCode, String resp) {

	}

	public boolean doResult(int reqId, int resultCode, String resp) {
		return false;
	}

	public void doSuccess(int reqId, int resultCode, String resp) {
	}

	public void doFailed(int reqId, int resultCode, String errorResp) {
	}

}
