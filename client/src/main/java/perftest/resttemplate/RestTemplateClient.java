package perftest.resttemplate;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

public class RestTemplateClient {

	private static Logger logger = LoggerFactory.getLogger(RestTemplateClient.class);

	public static void main(String[] args) throws InterruptedException, IOException {

		final RestTemplate restTemplate = new RestTemplate();

		StopWatch watch = new StopWatch();
		watch.start();

		final int count = 10000;

		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(2000);
		taskExecutor.setMaxPoolSize(2000);
		taskExecutor.setQueueCapacity(10000);
		taskExecutor.afterPropertiesSet();

		final CountDownLatch latch = new CountDownLatch(count);

		for (int i=0; i < count; i++) {
			final int index = i;
			taskExecutor.submit(new Runnable() {
				@Override
				public void run() {
					Integer port = 1330 + index / 1000;
					StopWatch w = new StopWatch();
					w.start();
					try {
						restTemplate.getForObject("http://127.0.0.1:" + port + "/products", Product.class);
						w.stop();
						logger.debug("[{}] {} millis", index, w.getTotalTimeMillis());
					}
					catch (Throwable t) {
						logger.error("index: {}, port: {}, message: {} ", new Object[] {index, port, t.getMessage()});
					}
					finally {
						latch.countDown();
					}
				}
			});
		}

		latch.await();

		watch.stop();
		logger.debug("Time: " + watch.getTotalTimeMillis());

		System.in.read();
		System.exit(0);
	}

	public static class Product {

		private String name;

		private List<String> colors;

		private List<Double> dimensions;

		private String designer;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<String> getColors() {
			return colors;
		}

		public void setColors(List<String> colors) {
			this.colors = colors;
		}

		public List<Double> getDimensions() {
			return dimensions;
		}

		public void setDimensions(List<Double> dimensions) {
			this.dimensions = dimensions;
		}

		public String getDesigner() {
			return designer;
		}

		public void setDesigner(String designer) {
			this.designer = designer;
		}

		@Override
		public String toString() {
			return "Product [name=" + name + ", colors=" + colors
					+ ", dimensions=" + dimensions + ", designer=" + designer
					+ "]";
		}

	}

}
