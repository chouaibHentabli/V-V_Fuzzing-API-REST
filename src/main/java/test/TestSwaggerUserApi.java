package test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import factory.AbstractFactoryDataType;
import io.swagger.models.Model;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.parser.SwaggerParser;

/**
 * Created by chouaib on 15/11/16.
 */
public class TestSwaggerUserApi {

	public void parse(JSONObject obj) {
		Swagger swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
		Map<String, Model> map = swagger.getDefinitions();
		Map<String, Path> m = swagger.getPaths();
		// Iterator<Model> it = map.I;
		// Path path = m.get("/user");
		Model model = map.get("User");
		Map<String, Property> properties = model.getProperties();
		for (Entry<String, Property> entry : properties.entrySet()) {
			// System.out.println(entry.getKey() );
			AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType();
			obj.put(entry.getKey(), dataType.getDataType().getData());
		}
	}

	public void request(TypeRequest type) {

		// String message = null;
		JSONObject json_data = null;
		String url = null;
		HttpResponse response = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			JSONParser parser = new JSONParser();
			switch (type) {
			case POST: {
				url = "http://petstore.swagger.io/v2/user";
				HttpPost post = new HttpPost(url);

				// add header
				post.setHeader("Content-Type", "application/json");
				JSONObject obj = new JSONObject();
				// add data to user properties
				this.parse(obj);

				StringEntity entity = new StringEntity(obj.toJSONString());
				post.setEntity(entity);
				response = httpclient.execute(post);
				System.out.println(response.getStatusLine());
				break;
			}
			case PUT: {
				break;
			}
			case GETBYUSERNAME: {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType();

				url = "http://petstore.swagger.io/v2/user/user/" + dataType.getDataType().getData();
				response = httpclient.execute(new HttpGet(url));
				System.out.println(response.getStatusLine());
				break;
			}
			case DELETE: {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType();
				url = "http://petstore.swagger.io/v2/user/" + dataType.getDataType().getData();
				break;
			}

			}

			// json_data = (JSONObject)
			// parser.parse(EntityUtils.toString(response.getEntity()));

			/*
			 * JSONArray results = (JSONArray) json_data.get("result"); for
			 * (Object queid : results) { message = message.concat((String)
			 * ((JSONObject) queid).get("id")); message = message.concat("\t");
			 * message = message.concat((String) ((JSONObject)
			 * queid).get("owner")); message = message.concat("\n"); }
			 */
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	public static void main(String args[]) {

		TestSwaggerUserApi d = new TestSwaggerUserApi();
		for (int i = 0; i < 1000; i++) {
			// System.out.println(d.ranjava.lang.NoClassDefFoundError:
			// org/apache/commons/logging/LogFactorydomString());
			d.request(TypeRequest.POST);
			// d.parse();
			// char a = 200;
			// System.out.println(a);
		}
	}

}
