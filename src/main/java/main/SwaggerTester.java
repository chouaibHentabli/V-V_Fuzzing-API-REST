package main;

import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.apache.http.HttpResponse;

import java.util.Map;

/**
 * Created by annaig on 08/01/17.
 */
public class SwaggerTester {

    public static int getHttpResponseCodeFromRandomPath(Swagger swagger) {
        Map<String, Path> swaggerPaths = swagger.getPaths();
        String concatPaths = "/aleatoire";
        HttpRequest httpRequestTest404 = new HttpRequest();
        int returnCodeGET = 0;


        for (Map.Entry<String, Path> e : swaggerPaths.entrySet()){
            Path path = e.getValue();
            concatPaths += e.getKey();
        }

        //oracle = construction de l'url aléatoire dont on sait qu'ell renverra un 404 suite à un GET
        String urlAleatoire = "http://" + swagger.getHost() + swagger.getBasePath() + concatPaths.replaceAll("\\{","%7B").replaceAll("\\}","%7D");
        try {
            HttpResponse response= httpRequestTest404.sendGet(urlAleatoire);
            returnCodeGET= response.getStatusLine().getStatusCode();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //fin oracle

        return returnCodeGET;
    }

  



}
