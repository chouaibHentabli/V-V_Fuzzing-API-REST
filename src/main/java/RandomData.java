
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

/**
 * Created by chouaib on 15/11/16.
 */
public class RandomData {

	public int randomInt() {
		Random rand = new Random();
		return rand.nextInt();
	}

	public String randomString() {
		int min = 65;
		int max = 100;

		Random random = new Random();
		int strLength = random.nextInt(max) + min;
		return RandomStringUtils.random(strLength);
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
				obj.put("id", randomInt());
				obj.put("username", randomString());
				obj.put("firstName", randomString());
				obj.put("lastName", randomString());
				obj.put("email", randomString());
				obj.put("password", randomString());
				obj.put("phone", randomInt());
				obj.put("userStatus", randomInt());

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
				url = "http://petstore.swagger.io/v2/user/user/" + randomString();
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
		for (int i = 0; i < 200; i++) {
			// System.out.println(d.ranjava.lang.NoClassDefFoundError:
			// org/apache/commons/logging/LogFactorydomString());

			d.request(TypeRequest.POST);
		}
	}

}
