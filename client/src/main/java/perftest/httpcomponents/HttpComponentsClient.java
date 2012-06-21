package perftest.httpcomponents;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.DefaultHttpAsyncClient;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.StopWatch;

import perftest.resttemplate.RestTemplateClient.Product;

public class HttpComponentsClient {

	private static Logger logger = LoggerFactory.getLogger(HttpComponentsClient.class);

	public static void main(String[] args) throws InterruptedException, IOException {

		IOReactorConfig ioReactorConfig = new IOReactorConfig();
		ioReactorConfig.setIoThreadCount(20);
		HttpAsyncClient httpclient = new DefaultHttpAsyncClient(ioReactorConfig);

//		httpclient.getParams()
//			.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 9000)
//			.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000)
//			.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 4000 * 1024)
//			.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);

//		System.in.read();
		StopWatch watch = new StopWatch();
		watch.start();

		httpclient.start();
		try {
			final HttpGet[] requests = new HttpGet[20];
			for (int i = 0; i < requests.length; i++) {
				requests[i] = new HttpGet("http://127.0.0.1:1337/products");
			}

			final CountDownLatch latch = new CountDownLatch(requests.length);

			final MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();

			for (int i = 0; i < requests.length; i++) {
				final int index = i;
				httpclient.execute(requests[i], new FutureCallback<HttpResponse>() {

					public void completed(final HttpResponse response) {
						HttpInputMessage inputMessage = new HttpComponentsClientHttpResponse(response);
						Product product;
						try {
							product = (Product) converter.read(Product.class, inputMessage);
							logger.debug("[{}] {}", index, product.toString());
						}
						catch (IOException ex) {
							logger.debug(requests[index].getRequestLine() + "->" + ex);
						}
						latch.countDown();
					}

					public void failed(final Exception ex) {
						logger.debug(requests[index].getRequestLine() + "->" + ex);
						latch.countDown();
					}

					public void cancelled() {
						logger.debug(requests[index].getRequestLine() + " cancelled");
						latch.countDown();
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

//		System.in.read();
		System.exit(0);
	}

}
