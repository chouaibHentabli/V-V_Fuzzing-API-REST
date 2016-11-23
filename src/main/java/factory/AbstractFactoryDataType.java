package factory;

import java.util.Random;

public abstract class AbstractFactoryDataType {

	public abstract Object getData();

	public static AbstractFactoryDataType getDataType() {
		Random rand = new Random();
		int type = rand.nextInt(3);

		if (type == 1)
			return new RandomString();
		if (type == 2)
			return new RandomBoolean();

		return new RandomInt();
	}
}
