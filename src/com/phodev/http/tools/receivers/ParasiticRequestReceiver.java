package com.phodev.http.tools.receivers;

import com.phodev.http.tools.RequestReceiver;

public abstract class ParasiticRequestReceiver implements RequestReceiver {
	public abstract void parasiticHandler(int resultCode, int reqId,
			Object tag, String resp);

	@Override
	public void onResult(int resultCode, int reqId, Object tag, String resp) {

	}
}