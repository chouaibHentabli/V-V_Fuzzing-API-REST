package factory;

 
public abstract class AbstractFactoryDataType {

	public abstract Object getData(String format, Boolean required);

	// return an
	public static AbstractFactoryDataType getDataType(String type) {

		if (type.equalsIgnoreCase("String"))
			return new RandomString();
		if (type.equalsIgnoreCase("Boolean"))
			return new RandomBoolean();
		if (type.equalsIgnoreCase("Date"))
			return new RandomDate();
		if (type.equalsIgnoreCase("Double"))
			return new RandomDouble();

		return new RandomInt();
	}
}
