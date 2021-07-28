package secnj;

import com.alibaba.fastjson.parser.ParserConfig;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import java.util.Map;
import java.util.Properties;



public class Main {
	public static void main(String[] args) throws Exception {
		Config config = ConfigFactory.load();
		ParserConfig.getGlobalInstance().setSafeMode(true);

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.enableCheckpointing(1000);
		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", config.getString("input.kafka.server"));
		properties.setProperty("group.id", config.getString("input.kafka.group_id"));
		FlinkKafkaConsumer<String> secConsumer = new FlinkKafkaConsumer<>(config.getString("input.kafka.topic"), new SimpleStringSchema(),
				properties);

		SingleOutputStreamOperator<Map<String, Object>> StreamRecord = env.addSource(secConsumer)
				.map(string -> JsonFlattener.flattenAsMap(string)).returns(TypeInformation.of(new TypeHint<Map<String, Object>>(){}));

		DataStream<String> result = StreamRecord.map(new RuleE());
		result.addSink(new FlinkKafkaProducer<>(config.getString("output.kafka.server"), config.getString("output.kafka.topic"), new SimpleStringSchema()));
		env.execute("Rule-engine for NIDS");

	}

}


class RuleE implements MapFunction<Map<String, Object>,String>{

	public String map(Map<String, Object> j) {
		return RuleEngine.ResCheck(j);
	}
}