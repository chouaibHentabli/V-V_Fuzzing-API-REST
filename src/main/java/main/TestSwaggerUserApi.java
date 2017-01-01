package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import factory.AbstractFactoryDataType;
import io.swagger.models.Info;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.parameters.*;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.parser.SwaggerParser;

/**
 * Created by chouaib on 15/11/16.
 */
public class TestSwaggerUserApi {

	public static final int STRING = 1;
	public static final int BOOLEAN = 2;
	public static final int DATE = 3;
	public static final int INT = 4;

	// private Path path;
	private Swagger swagger;
	// String : name of property, Property: an object contain the format,
	// type..., of the property
	private Map<String, Property> properties = new HashMap<String, Property>();

	private Map<String, Path> paths = new HashMap<String, Path>();
	//save the list of operations of each path
	private Map<String, List<Operation>> oPath = new HashMap<String, List<Operation>>();

	public TestSwaggerUserApi() {

	}

	// extract the properties of user
	public void init() {
		this.swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
   
		Map<String, Model> map = this.swagger.getDefinitions();

		Model model = map.get("User");
		Map<String, Property> properties = model.getProperties();

		Map<String, Path> paths = swagger.getPaths();

		for (Entry<String, Path> entry : paths.entrySet()) {
			// add each property to our properties list
			this.paths.put(entry.getKey(), entry.getValue());
			System.out.println(entry.getKey() + " => " + entry.getValue());
 		}
		System.out.println("\n\n\n\n");

		/*
		 * for (Entry<String, Property> entry : properties.entrySet()) { // add
		 * each property to our properties list
		 * this.properties.put(entry.getKey(), entry.getValue()); }
		 */

		for (Entry<String, Path> entry : paths.entrySet()) {
			// add each property to our properties list
			List<Operation> op = entry.getValue().getOperations();

			for (Operation o : op) {
				// add each property to our properties list
				List<Parameter> pr = o.getParameters();
				for (Parameter pp : pr) {
					//System.out.println(pp.);
				}
			}

		}

	}

	public void affectValueForEachField(JSONObject obj) {

		if (obj == null)
			return;

		for (Entry<String, Property> entry : this.properties.entrySet()) {
			String type = entry.getValue().getType();

			System.out.println(entry.getValue().getType() + "==============>" + entry.getValue().getPosition());

			if ("string".equalsIgnoreCase(type)) {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType(STRING);
				obj.put(entry.getKey(), dataType.getData(entry.getValue().getFormat()));
			}

			if ("integer".equals(type)) {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType(INT);
				obj.put(entry.getKey(), dataType.getData(entry.getValue().getFormat()));
			}

			if ("date".equals(type)) {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType(DATE);
				obj.put(entry.getKey(), dataType.getData(entry.getValue().getFormat()));
			}

			if ("boolean".equals(type)) {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType(BOOLEAN);
				obj.put(entry.getKey(), dataType.getData(entry.getValue().getFormat()));
			}
		}
	}

	// check types field
	public Boolean checkDataType(JSONObject obj) {

		for (Entry<String, Property> entry : properties.entrySet()) {
			// if(entry.getKey())

		}
		return true;
	}

	@SuppressWarnings("finally")
	public int request(TypeRequest type) {

		// String message = null;
		JSONObject json_data = null;
		String url = null;
		HttpResponse response = null;
		int code = 0;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			JSONParser parser = new JSONParser();
			switch (type) {
			case POST: {
				url = "http://petstore.swagger.io/v2/user";
				HttpPost post = new HttpPost(url);

				// add header
				post.setHeader("Content-Type", "application/json");
				// parse the json object
				init();
				// create json object and fill all the fields
				JSONObject obj = new JSONObject();
				affectValueForEachField(obj);

				StringEntity entity = new StringEntity(obj.toJSONString());
				post.setEntity(entity);
				response = httpclient.execute(post);
				code = response.getStatusLine().getStatusCode();
				break;
			}
			case PUT: {
				break;
			}
			case GETBYUSERNAME: {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType(STRING);

				url = "http://petstore.swagger.io/v2/user/" + dataType.getData("");

				response = httpclient.execute(new HttpGet(url));
				//
				code = response.getStatusLine().getStatusCode();

				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}

				// create the json object
				try {
					Object o = JSONValue.parse(result.toString());
					if (o instanceof JSONObject) {
						JSONObject j = (JSONObject) JSONValue.parse(result.toString());
						// checkDataType(j);
						// System.out.println(j.toJSONString());
					} else {
						JSONArray j = (JSONArray) JSONValue.parse(result.toString());
					}
				} catch (Exception e) {
					// Si une exception est levée la
				}
				// représentation n'est pas // valide

				break;
			}
			case DELETE: {
				// bstractFactoryDataType dataType =
				// AbstractFactoryDataType.getDataType();
				// url = "http://petstore.swagger.io/v2/user/" +
				// dataType.getDataType().getData();
				//
				code = response.getStatusLine().getStatusCode();
				break;
			}

			}
		} catch (

		Exception e) {
			System.out.println(e.toString());
		} finally {
			return code;
		}
	}

	public void display() {
		for (Entry<String, Property> entry : properties.entrySet()) {
			// add each property to our properties list
			System.out.println(entry.getKey() + " => " + entry.getValue().getType());

		}
	}

}

// tester tout les opération de l'api get, put, delete
// generation de requète
// 404 qu'on le chemin n'est pas définie
// le type retourner par get correspond
// tester si la reponce au type définide la doc valdiation
// comparer les outputs avec les types d'entrés
// generer des URI invlaides
// des fois des requetes valide et il retourne une erreur
