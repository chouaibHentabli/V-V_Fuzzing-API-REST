package factory;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

 
public class RandomString extends AbstractFactoryDataType {

    @Override
	public String getData() {
		// TODO Auto-generated method stub
		int min = 33;
		int max = 200;

		Random random = new Random();
		int strLength = random.nextInt(max) + min;
		// System.out.println("--------------------------------------" +
		// RandomStringUtils.random(strLength));
		return RandomStringUtils.random(strLength);
	}

}
