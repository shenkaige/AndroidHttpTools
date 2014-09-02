package com.phodev.http.tools.receivers;

import com.phodev.http.tools.RequestReceiver;

public abstract class SimpleReqeustReceiver implements RequestReceiver {
	@Override
	public void onRequestCanceled(int reqId, Object tag) {

	}
}