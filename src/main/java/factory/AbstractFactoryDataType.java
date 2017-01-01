package factory;

import java.util.Random;

public abstract class AbstractFactoryDataType {

	public abstract Object getData(String format);
    
	//return an
	public static AbstractFactoryDataType getDataType(int type) {
		 
		if (type == 1)
			return new RandomString();
		if (type == 2)
			return new RandomBoolean();
		if (type == 3)
			return new RandomDate();

		return new RandomInt();
	}
}
