AndroidHttpTools
================

a simple HTTP request tools,support get,post,post with files



//------------------------Sample Code------------------------//


	String url = "http://www.baidu.com/";

	private void requestData() {
		ConnectionHelper conn = ConnectionHelper.obtainInstance();
		long requstHandler = conn.httpGet(url, 0, rr);
		// support cancel
		// conn.cancleRequest(requstHandler);
	}

	private void reqeustData2() {
		ConnectionHelper.obtainInstance().execute(
				RequestEntity.obtain().url(url).method(RequestMethod.GET)
						.receiver(rr));
	}

	private RequestReceiver rr = new RequestReceiver() {

		@Override
		public void onResult(int resultCode, int reqId, Object tag, String resp) {
			if (resultCode == RESULT_STATE_OK) {
				// reqeustion ok
			} else {
				// reqeust error
			}
		}

		@Override
		public void onRequestCanceled(int reqId, Object tag) {
			// on canceled
		}
	};
