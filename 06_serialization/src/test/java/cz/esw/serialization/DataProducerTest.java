package cz.esw.serialization;

import cz.esw.serialization.handler.JsonDataHandler;
import cz.esw.serialization.json.Dataset;
import cz.esw.serialization.json.MeasurementInfo;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

/**
 * @author Marek Cuchý (CVUT)
 */
class DataProducerTest {

	@Test
	void generateDataAndCheckResults() throws IOException {
		DataProducer producer = new DataProducer(new Random(0), 100, 10000);
		producer.generateDataAndCheckResults(new TestHandler());

	}

	private static class TestHandler extends JsonDataHandler {
		public TestHandler() {
			super(null, null);
		}

		@Override
		public void getResults(ResultConsumer consumer) {
			for (Dataset dataset : datasets.values()) {
				MeasurementInfo info = dataset.getInfo();
				consumer.acceptMeasurementInfo(info.id(), info.timestamp(), info.measurerName());
				dataset.getRecords().forEach((type, values) -> consumer.acceptResult(type,values.stream().mapToDouble(Double::doubleValue).average().orElseThrow() ));
			}
		}

	}
}