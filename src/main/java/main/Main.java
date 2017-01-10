package main;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.HttpResponse;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;

public class Main {

	String baseUrlApi = "http://petstore.swagger.io/v2";

	public static void main(String args[]) throws ClientProtocolException, IOException {

		TestSwaggerApi test = new TestSwaggerApi();
		for (int i = 0; i < 1; i++) {
			// System.out.println(d.ranjava.lang.NoClassDefFoundError:
			// org/apache/commons/logging/LogFactorydomString());

			Main m = new Main();
			HttpResponse response = null;
			TestSwaggerApi swaggerApi = new TestSwaggerApi();
			swaggerApi.init(m.baseUrlApi);

			for (Entry<String, Path> entry : swaggerApi.getPaths().entrySet()) {
				// get the resonse returned by the operation
				Map<HttpMethod, Operation> op = swaggerApi.getPathOperations().get(entry.getKey());
				Operation o = op.get(HttpMethod.GET);
				Map<String, Response> resp = null;
				if (o != null) {
					resp = o.getResponses();
					for (Entry<String, Response> entr : resp.entrySet()) {
						System.out.println(entr.getKey() + "=>" + entr.getValue().getDescription());
					}
				}

			}

			for (Entry<String, Path> entry : swaggerApi.getPaths().entrySet()) {
				// response = swaggerApi.request(entry.getKey(),
				// HttpMethod.GET);
				// System.out.println("path : " + entry.getKey());
				// swaggerApi.formatRequestUrl(entry.getKey(), null);
			}
			// test.displayPropperties();
			// HttpResponse r = test.request(url, HttpMethod.POST);
			// System.out.println(r.getStatusLine().getStatusCode());
			// d.dis-play();
			// char a = 200;
			// System.out.println(a);
		}
	}
}
