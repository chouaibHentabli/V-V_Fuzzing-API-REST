package factory;

import java.util.Random;

 
public class RandomBoolean extends AbstractFactoryDataType {

	@Override
	public Boolean getData(String format, Boolean required) {
		// TODO Auto-generated method stub
		Random random = new Random();
		int r = random.nextInt(2);
		if (r == 1)
			return true;

		return false;

	}

}
