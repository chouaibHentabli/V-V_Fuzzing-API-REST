package factory;

import java.util.Random;

public class RandomInt extends AbstractFactoryDataType {

	@Override
	public Integer getData(String format, Boolean required) {
		// TODO Auto-generated method stub
		Random rand = new Random();
 		return rand.nextInt();
	}
}
