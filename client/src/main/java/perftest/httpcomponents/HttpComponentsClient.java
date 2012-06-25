package perftest.httpcomponents;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.DefaultHttpAsyncClient;
import org.apache.http.impl.nio.conn.PoolingClientAsyncConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.client.HttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.StopWatch;

import perftest.resttemplate.RestTemplateClient.Product;

public class HttpComponentsClient {

	private static Logger logger = LoggerFactory.getLogger(HttpComponentsClient.class);

	public static void main(String[] args) throws InterruptedException, IOException {

		int count = 4000;

		DefaultConnectingIOReactor ioreactor = new DefaultConnectingIOReactor();
		PoolingClientAsyncConnectionManager connectionManager = new PoolingClientAsyncConnectionManager(ioreactor);
		connectionManager.setDefaultMaxPerRoute(count);
		connectionManager.setMaxTotal(count);
		HttpAsyncClient httpclient = new DefaultHttpAsyncClient(connectionManager);

		StopWatch watch = new StopWatch();
		watch.start();

		httpclient.start();
		try {
			final HttpGet[] requests = new HttpGet[count];
			for (int i = 0; i < requests.length; i++) {
				int port = 1330 + i / 1000;
				requests[i] = new HttpGet("http://127.0.0.1:" + port + "/products");
			}

			final CountDownLatch latch = new CountDownLatch(requests.length);

			final MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();

			for (int i = 0; i < requests.length; i++) {
				final int index = i;
				httpclient.execute(requests[i], new FutureCallback<HttpResponse>() {

					public void completed(final HttpResponse response) {
						HttpInputMessage inputMessage = new HttpComponentsClientHttpResponse(response);
						try {
							converter.read(Product.class, inputMessage);
							logger.debug("[{}]", index);
						}
						catch (Throwable t) {
							logger.error("exception: {} -> {}", requests[index].getRequestLine(), t.getMessage());
						}
						finally {
							latch.countDown();
						}
					}

					public void failed(final Exception ex) {
						logger.debug("failed: {}", index);
					}

					public void cancelled() {
						logger.debug(requests[index].getRequestLine() + " cancelled");
					}

				});
			}
			latch.await();
		}
		finally {
			httpclient.shutdown();
		}

		watch.stop();
		logger.debug("Time: " + watch.getTotalTimeMillis());

		System.in.read();
		System.exit(0);
	}

}
