package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;

import factory.AbstractFactoryDataType;
import io.swagger.models.HttpMethod;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.Xml;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import io.swagger.parser.SwaggerParser;

/**
 * Created by chouaib on 15/11/16.
 */
public class TestSwaggerApi {

	private Swagger swagger;

	private Map<String, Property> properties = new HashMap<String, Property>();

	private Map<String, Path> paths = new HashMap<String, Path>();

	// key: path
	private Map<String, Model> models = new HashMap<String, Model>();

	// save the list of operations of each path
	private Map<String, Map<HttpMethod, Operation>> pathOperations = new HashMap<String, Map<HttpMethod, Operation>>();

	public TestSwaggerApi() {

	}

	public Swagger getSwagger() {
		return swagger;
	}

	public void setSwagger(Swagger swagger) {
		this.swagger = swagger;
	}

	public Map<String, Property> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Property> properties) {
		this.properties = properties;
	}

	public Map<String, Path> getPaths() {
		return paths;
	}

	public void setPaths(Map<String, Path> paths) {
		this.paths = paths;
	}

	public Map<String, Map<HttpMethod, Operation>> getPathOperations() {
		return pathOperations;
	}

	public void setPathOperations(Map<String, Map<HttpMethod, Operation>> pathOperations) {
		this.pathOperations = pathOperations;
	}

	// extract the properties from the url
	public void init(String baseUrl) {
		this.swagger = new SwaggerParser().read(baseUrl + "/swagger.json");

		// List<Scheme> sc = this.swagger.getSchemes();

		// displaySchemes(sc);
		this.models = this.swagger.getDefinitions();

		// get all paths
		this.paths = swagger.getPaths();

		// save each path with his model
		for (Entry<String, Path> entry : this.paths.entrySet()) {
			// get the model for each path key
			Model model = searchForModel(entry.getKey());
			if (model != null)
				this.models.put(entry.getKey(), model);
		}

		// save each path with his operations
		for (Entry<String, Path> entry : this.paths.entrySet()) {
			pathOperations.put(entry.getKey(), entry.getValue().getOperationMap());
		}

		// Model model = models.get("Pet");
		// Map<String, Property> prop = model.getProperties();
		// displayPropperties(prop);
		// displayPaths();
		// displayPropperties(this.properties);
		// displayModels(this.models);
	}

	/*
	 * generate a json objct with random data
	 * 
	 * @Param properties
	 */

	public JSONObject affectValueForEachField(Map<String, Property> properties) {

		// create json object and fill all the fields
		JSONObject obj = new JSONObject();

		for (Entry<String, Property> entry : properties.entrySet()) {
			String type = entry.getValue().getType();

			// System.out.println(entry.getValue().getType() + "==============>"
			// + entry.getValue().getPosition());

			if ("string".equalsIgnoreCase(type)) {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType("String");
				obj.put(entry.getKey(), dataType.getData(entry.getValue().getFormat(), entry.getValue().getRequired()));
			}

			if ("integer".equals(type)) {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType("Integer");
				obj.put(entry.getKey(), dataType.getData(entry.getValue().getFormat(), entry.getValue().getRequired()));
			}

			if ("date".equals(type)) {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType("Date");
				obj.put(entry.getKey(), dataType.getData(entry.getValue().getFormat(), entry.getValue().getRequired()));
			}

			if ("boolean".equals(type)) {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType("Boolean");
				obj.put(entry.getKey(), dataType.getData(entry.getValue().getFormat(), entry.getValue().getRequired()));
			}

			if ("double".equals(type)) {
				AbstractFactoryDataType dataType = AbstractFactoryDataType.getDataType("Double");
				obj.put(entry.getKey(), dataType.getData(entry.getValue().getFormat(), entry.getValue().getRequired()));
			}
		}

		return obj;
	}

	// check types field
	public Boolean checkDataType(JSONObject obj) {
		for (Entry<String, Property> entry : properties.entrySet()) {
			// if(entry.getKey())
		}
		return true;
	}

	/*
	 * test the operation
	 * 
	 * @Param String path
	 * 
	 * @Param HttpMethod method
	 */
	public HttpResponse request(String pathKey, HttpMethod method) throws ClientProtocolException, IOException {

		HttpResponse response = null;
		// search for the model in order to get the properties

		// get the operation
		Map<HttpMethod, Operation> op = pathOperations.get(pathKey);
		Operation o = op.get(method);
		// get the properties of this operation via model
		if (o != null) {
			// displayOperation(o);
			// il faut avoir le type de op query path...

			Model model = this.models.get(pathKey);
			// displayParameters(o.getParameters());
			Map<String, Property> properties = getOperationProperties(model, o.getParameters());
			// System.out.println("=>=>=>=>=>=>=>=>" + properties);
			HttpClient httpclient = new DefaultHttpClient();
			switch (method) {
			case GET: {
				// in default we use the type query
				ParameterType type = ParameterType.QUERY;
				/*
				 * if the path contain parameters, get one parameter in order to
				 * detect the parameter type's
				 */
				if (o.getParameters() != null && o.getParameters().size() != 0) {
					Parameter p = o.getParameters().get(0);
					type = getEnumType(p.getIn());
				}
				/*
				 * generate data if there is parameters in the url String: name
				 * of the property, Object: value generated for this property
				 */
				Map<String, Object> listObj = null;
				if (properties != null && properties.size() != 0) {
					listObj = new HashMap<String, Object>();
					for (Entry<String, Property> entry : properties.entrySet()) {
						// get a factory of data according to data type
						if (entry.getValue() != null) {
							// System.out.println("Path => " + pathKey);
							AbstractFactoryDataType dataType = AbstractFactoryDataType
									.getDataType(entry.getValue().getType());
							Object oo = dataType.getData(entry.getValue().getFormat(), entry.getValue().getRequired());
							listObj.put(entry.getKey(), oo);
						}
					}
				}
				// displayPropperties(properties);
				switch (type) {
				case QUERY:
					response = httpclient.execute(new HttpGet(formatRequestUrl(ParameterType.QUERY, pathKey, listObj)));
					break;
				case PATH:
					response = httpclient.execute(new HttpGet(formatRequestUrl(ParameterType.PATH, pathKey, listObj)));
					break;
				default:
					break;
				}
			}
				break;

			case POST: {
				String url = "http://" + this.swagger.getHost() + this.swagger.getBasePath() + pathKey;
				HttpPost post = new HttpPost(url);
				// add header
				post.setHeader("Content-Type", "application/json");
				// get json object with random data
				JSONObject obj = affectValueForEachField(properties);
				StringEntity entity = new StringEntity(obj.toJSONString());
				post.setEntity(entity);
				response = httpclient.execute(post);
			}
				break;
			case PUT: {
				String url = "http://" + this.swagger.getHost() + this.swagger.getBasePath() + pathKey;
				HttpPost post = new HttpPost(url);
				// add header
				post.setHeader("Content-Type", "application/json");
				// get json object with random data
				JSONObject obj = affectValueForEachField(properties);
				StringEntity entity = new StringEntity(obj.toJSONString());
				post.setEntity(entity);
				response = httpclient.execute(post);
			}
				break;
			case DELETE: {
				// in default we use the type query
				ParameterType type = ParameterType.QUERY;
				/*
				 * if the path contain parameters, get one parameter in order to
				 * detect the parameter type's
				 */
				if (o.getParameters() != null && o.getParameters().size() != 0) {
					Parameter p = o.getParameters().get(0);
					type = getEnumType(p.getIn());
				}
				/*
				 * generate data if there is parameters in the url String: name
				 * of the property, Object: value generated for this property
				 */
				Map<String, Object> listObj = null;
				if (properties != null && properties.size() != 0) {
					listObj = new HashMap<String, Object>();
					for (Entry<String, Property> entry : properties.entrySet()) {
						// get a factory of data according to data type
						if (entry.getValue() != null) {
							// System.out.println("Path => " + pathKey);
							AbstractFactoryDataType dataType = AbstractFactoryDataType
									.getDataType(entry.getValue().getType());
							Object oo = dataType.getData(entry.getValue().getFormat(), entry.getValue().getRequired());
							listObj.put(entry.getKey(), oo);
						}
					}
				}

				// displayPropperties(properties);
				switch (type) {
				case QUERY:
					response = httpclient
							.execute(new HttpDelete(formatRequestUrl(ParameterType.QUERY, pathKey, listObj)));
					break;
				case PATH:
					response = httpclient
							.execute(new HttpDelete(formatRequestUrl(ParameterType.PATH, pathKey, listObj)));
					break;
				default:
					break;
				}

			}
				break;
			default:
				break;

			}

		}
		return response;

	}

	public ParameterType getEnumType(String type) {

		if (type.equalsIgnoreCase("query"))
			return ParameterType.QUERY;
		if (type.equalsIgnoreCase("path"))
			return ParameterType.PATH;

		return null;
	}

	/*
	 * allow to add parameters value in the url
	 * 
	 * @Param in, describe in which we can put the parameter's value: path,
	 * query, body
	 * 
	 * @Param path
	 * 
	 * @Param objs, list of values generated
	 */
	public String formatRequestUrl(ParameterType in, String path, Map<String, Object> objs) {
		String url = "http://" + this.swagger.getHost() + this.swagger.getBasePath();
		// System.err.println("path => " + path);

		// extract the base path form pathKey
		if (objs != null) {
			switch (in) {
			case QUERY: {
				url = url + path;
				for (Entry<String, Object> entry : objs.entrySet()) {
					// System.err.println("key : " + entry.getKey() + "=> " +
					// entry.getValue());
					url = url + "?" + entry.getKey() + "=" + entry.getValue();
				}
			}
				break;
			case PATH: {
				// System.err.println("path => " + path);
				Object[] tab = path.split("/");

				int i = 0;
				// look for the parameter in the path
				while (i < tab.length && !((String) tab[i]).contains("{")) {
					// if (tab[i].contains()) {
					// postions.add(i);
					// }
					i++;
				}
				if (i < tab.length) {
					// Remove the brackets
					String str = tab[i].toString();
					String paramKey = str.substring(1, str.length() - 1);

					tab[i] = objs.get(paramKey).toString();
				}
				// add the values to the url
				for (int k = 0; k < tab.length; k++) {
					// System.err.println("----After --" + tab[k]);
					url = url + tab[k] + "/";
				}
			}
				break;
			default:
				break;
			}

		}
		// System.out.println("url => " + url);
		return url;
	}

	public Model searchForModel(String path) {

		String[] tab = path.split("/");
		tab = capitaliseTableElements(tab);

		int i = 0;
		while (i < tab.length && !this.models.containsKey(tab[i])) {
			i++;
		}

		if (i < tab.length)
			return this.models.get(tab[i]);

		return null;
	}

	@SuppressWarnings("deprecation")
	public String[] capitaliseTableElements(String[] tab) {
		for (int i = 0; i < tab.length; i++) {
			tab[i] = StringUtils.capitalise(tab[i]);
		}
		return tab;
	}

	// get the correspondence properties of those parameters
	public Map<String, Property> getOperationProperties(Model model, List<Parameter> params) {
		Map<String, Property> properties = null;
		if (model != null && params != null) {
			properties = new HashMap<String, Property>();
			for (Parameter p : params) {

				String pName = getUpperString(p.getName());
				properties.put(p.getName(), model.getProperties().get(pName));
			}
		}
		// displayPropperties(properties);
		return properties;
	}

	public void displaySchemes(List<Scheme> sh) {
		for (Scheme s : sh) {
			System.out.println(" Tilte  =>   " + s.name());
			System.out.println(" Ordinal   =>   " + s.ordinal());
			System.out.println(" Value =>   " + s.toValue());
			System.out.println(" toString   =>   " + s.toString());
			System.out.println("_____________________________________________________");
		}
	}

	public void displayOperation(Operation o) {

		System.out.println(" Id  =>   " + o.getOperationId());
		System.out.println(" Desc   =>   " + o.getDescription());
		System.out.println(" Response =>   " + o.getResponses());
		System.out.println(" Tags   =>   " + o.getTags());
		System.out.println("_____________________________________________________");

	}

	public void displayModels(Map<String, Model> models) {
		for (Entry<String, Model> entry : models.entrySet()) {
			System.out.println("----------------" + entry.getKey() + "--------------------");
			System.out.println(" Tilte  =>   " + entry.getValue().getTitle());
			System.out.println(" Description   =>   " + entry.getValue().getDescription());
			System.out.println(" Reference =>   " + entry.getValue().getReference());
			System.out.println(" VendorExtensions   =>   " + entry.getValue().getVendorExtensions().size());
			System.out.println("_____________________________________________________");
		}
	}

	public void displayPaths() {
		for (Entry<String, Path> entry : paths.entrySet()) {
			System.out.println("path => " + entry.getKey());
			Operation o1 = entry.getValue().getGet();
			Operation o2 = entry.getValue().getPost();
			Operation o3 = entry.getValue().getPut();
			Operation o4 = entry.getValue().getDelete();
			System.out.println("GET  ----" + o1);
			System.out.println("POST ----" + o2);
			System.out.println("PUT  ----" + o3);
			System.out.println("DELETE ----" + o4);
			System.out.println("_____________________________________________________");
		}
	}

	public void displayXml(Xml xml) {
		System.err.println("-----------------------------------Xml---------------------------");
		System.out.println(" Name  =>   " + xml.getName());
		System.out.println(" NameSpace    =>   " + xml.getNamespace());
		System.out.println(" Prefix  =>   " + xml.getPrefix());

	}

	public void displayPropperties(Map<String, Property> properties) {
		System.err.println("-----------------------------------Properties---------------------------");
		for (Entry<String, Property> entry : properties.entrySet()) {

			System.out.println("----------------" + entry.getKey() + "--------------------");
			System.out.println(" Tilte  =>   " + entry.getValue().getTitle());
			System.out.println(" Name    =>   " + entry.getValue().getFormat());
			System.out.println(" Type  =>   " + entry.getValue().getType());
			System.out.println(" Description   =>   " + entry.getValue().getDescription());
			System.out.println(" access =>   " + entry.getValue().getAccess());
			System.out.println(" Required   =>   " + entry.getValue().getRequired());
			System.out.println(" Position   =>   " + entry.getValue().getPosition());
		}
	}

	public void displayParameters(List<Parameter> pList) {
		System.out.println("-----------------------------------Parameters---------------------------");
		if (pList != null) {
			for (Parameter p : pList) {
				System.out.println("in =>" + p.getIn());
				System.out.println("Name =>" + p.getName());
				System.out.println("Pattern =>" + p.getPattern());
				System.out.println("Required =>" + p.getRequired());
				System.out.println("Class =>" + p.getClass());
			}
		}
	}

	// example petId, we search for id
	public String getUpperString(String str) {
		int i = 0;
		while (i < str.length() && !Character.isUpperCase(str.charAt(i))) {
			i++;
		}
		if (i < str.length())
			return str.substring(i, str.length()).toLowerCase();

		return str;
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
