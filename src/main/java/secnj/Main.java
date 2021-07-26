package secnj;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.json.JSONObject;
import java.util.Properties;


public class Main {
	public static void main(String[] args) throws Exception {
		Config config = ConfigFactory.load();
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.enableCheckpointing(1000);
		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", config.getString("input.kafka.server"));
		properties.setProperty("group.id", config.getString("input.kafka.group_id"));
		FlinkKafkaConsumer<String> myConsumer = new FlinkKafkaConsumer<>(config.getString("input.kafka.topic"), new SimpleStringSchema(),
				properties);
		SingleOutputStreamOperator<JSONObject> StreamRecord = env.addSource(myConsumer)
				.map(string -> new JSONObject(string));
        DataStream<String> result = StreamRecord.map(new JsonSting());
		result.addSink(new FlinkKafkaProducer<>(config.getString("output.kafka.server"), config.getString("output.kafka.topic"), new SimpleStringSchema()));
		env.execute("Rule-engine for NIDS");
	}

}


class JsonSting implements MapFunction<JSONObject,String>{
	public String map(JSONObject j) {
		return j.toString();
	}
}