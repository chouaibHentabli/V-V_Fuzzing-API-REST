

import static org.junit.Assert.*;

import main.TestSwaggerUserApi;
import main.TypeRequest;

//d3js
public class TestSuite {

	@org.junit.Test
	public void testGetByUsername() {

		TestSwaggerUserApi t = new TestSwaggerUserApi();
		int tab[] = new int[100];
		int tt[] = new int[100];

		for (int i = 0; i < 100; i++) { // tt[i] = 404; tab[i] =
			tab[i] = t.request(TypeRequest.GETBYUSERNAME);
			System.out.println("code GET    = " + tab[i]);
			assertEquals(404, tab[i]);
		}
	}

	@org.junit.Test
	public void testPost() {
		TestSwaggerUserApi t = new TestSwaggerUserApi();
		int tab[] = new int[100];
		int tt[] = new int[100];

		for (int i = 0; i < 100; i++) {
			// tt[i] = 404;
			tab[i] = t.request(TypeRequest.POST);
			System.out.println("code POST    = " + tab[i]);
			assertEquals(200, tab[i]);
		}
	}
}
// chaque instructiion dans le prg quel cas de test est passé dessus
