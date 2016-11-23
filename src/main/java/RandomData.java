
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

import io.swagger.models.Model;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.parser.SwaggerParser;

/**
 * Created by chouaib on 15/11/16.
 */
public class RandomData {

	public int randomInt() {
		Random rand = new Random();
		return rand.nextInt();
	}

	public String randomString() {
		int min = 33;
		int max = 200;

		Random random = new Random();
		int strLength = random.nextInt(max) + min;
		// System.out.println("--------------------------------------" +
		// RandomStringUtils.random(strLength));
		return RandomStringUtils.random(strLength);
	}

	public boolean randBoolean() {
		Random random = new Random();
		int r = random.nextInt(2);
		if (r == 1)
			return true;

		return false;
	}

	public void parse(JSONObject obj) {
		Swagger swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
		Map<String, Model> map = swagger.getDefinitions();
		Map<String, Path> m = swagger.getPaths();
		// Iterator<Model> it = map.I;
		// Path path = m.get("/user");
		Model model = map.get("User");
		Map<String, Property> properties = model.getProperties();
		for (Entry<String, Property> entry : properties.entrySet()) {
			//System.out.println(entry.getKey() );
			obj.put(entry.getKey(), randomString());
		}
	}

	public String request(TypeRequest type) {

		String message = null;
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
				url = "http://petstore.swagger.io/v2/user/user/" + randomInt();
				response = httpclient.execute(new HttpGet(url));
				System.out.println(response.getStatusLine());
				break;
			}
			case DELETE: {
				url = "http://petstore.swagger.io/v2/user/" + randomString();
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
			message = e.toString();
		}
		return message;
	}

	public static void main(String args[]) {

		RandomData d = new RandomData();
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
