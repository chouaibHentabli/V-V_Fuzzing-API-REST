package factory;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

public class RandomString extends AbstractFactoryDataType {

	@Override
	public String getData(String format) {
		// TODO Auto-generated method stub
		int min = 1;
		int max = 200;

		Random random = new Random();
		int strLength = random.nextInt(max) + min;
		// System.out.println("--------------------------------------" +
		// String str = RandomStringUtils.randomAlphanumeric(strLength);
		// System.out.println("String = " + str);
		return RandomStringUtils.randomAlphanumeric(strLength);
	}
									
}
