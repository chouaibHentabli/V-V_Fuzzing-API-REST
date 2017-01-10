import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import main.SwaggerTester;
import main.TestSwaggerApi;
 

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

//d3js
public class TestSuite {

	// annaïg 08/01/2017
	static String baseUrlApi = "http://petstore.swagger.io/v2";
	static TestSwaggerApi swaggerApi = null;

	static {
		if (swaggerApi == null) {
			swaggerApi = new TestSwaggerApi();
			swaggerApi.init(baseUrlApi);
		}
	}

	@org.junit.Test
	public void testGet() throws ClientProtocolException, IOException {
		HttpResponse response = null;
		for (int i = 0; i < 1000; i++) {
			for (Entry<String, Path> entry : swaggerApi.getPaths().entrySet()) {
				// get the resonse returned by the operation
				Map<HttpMethod, Operation> op = swaggerApi.getPathOperations().get(entry.getKey());
				Operation o = op.get(HttpMethod.GET);

				Map<String, Response> resp = null;
				if (o != null) {
					resp = o.getResponses();
					/*
					 * for (Entry<String, Response> entr : resp.entrySet()) {
					 * System.out.println(entr.getKey() + "=>" +
					 * entr.getValue().getDescription()); }
					 */
				}
				response = swaggerApi.request(entry.getKey(), HttpMethod.GET);

				if (response != null) {
					System.out.println(response.getStatusLine().getStatusCode());
					assertEquals(response.getStatusLine().getStatusCode(), 404);
				}
			}
		}
	}
	// fin annaïg 08/01/2017

	@org.junit.Test
	public void testPost() throws ClientProtocolException, IOException {
		HttpResponse response = null;
		for (Entry<String, Path> entry : swaggerApi.getPaths().entrySet()) {
			// response = swaggerApi.request(entry.getKey(), HttpMethod.POST);
		}
	}

	@org.junit.Test
	public void testPut() throws ClientProtocolException, IOException {
		HttpResponse response = null;
		for (Entry<String, Path> entry : swaggerApi.getPaths().entrySet()) {
			// response = swaggerApi.request(entry.getKey(), HttpMethod.PUT);
		}
	}

	@org.junit.Test
	public void testDelete() throws ClientProtocolException, IOException {
		HttpResponse response = null;
		for (Entry<String, Path> entry : swaggerApi.getPaths().entrySet()) {
			// response = swaggerApi.request(entry.getKey(), HttpMethod.DELETE);
		}
	}
}
// chaque instructiion dans le prg quel cas de test est passé dessus
