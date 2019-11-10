package Elasticsearch.TestElasticSearch;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;

public class TestClusterHealth {
	private static final String HOST = "localhost";
	private static final int PORT_ONE = 9200;
	private static final int PORT_TWO = 9201;
	private static final String SCHEME = "http";
	private static RestHighLevelClient restHighLevelClient;
	private static final String INDEX = "persondata";
	private static final String TYPE = "person";

	/**
	 * Implemented Singleton pattern here so that there is just one connection at a
	 * time.
	 * 
	 * @return RestHighLevelClient
	 */
	private static synchronized RestHighLevelClient makeConnection() {

		if (restHighLevelClient == null) {
			restHighLevelClient = new RestHighLevelClient(
					RestClient.builder(new HttpHost(HOST, PORT_ONE, SCHEME), new HttpHost(HOST, PORT_TWO, SCHEME)));
		}

		return restHighLevelClient;
	}

	private static synchronized void closeConnection() throws IOException {
		restHighLevelClient.close();
		restHighLevelClient = null;
	}

	public static void main(String[] args) throws IOException {
		ClusterHealthRequest request = new ClusterHealthRequest();
		RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
		RequestOptions opt = builder.build();
		makeConnection();
		ClusterHealthResponse response = restHighLevelClient.cluster().health(request, opt);
		String clusterName = response.getClusterName();
		ClusterHealthStatus status = response.getStatus();
		System.out.println(clusterName);
		System.out.println(status);
		System.out.println("------------");
		closeConnection();

	}

}
