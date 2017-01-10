package factory;

import java.util.Date;
 

public class RandomDate extends AbstractFactoryDataType {

	@Override
	public Date getData(String format, Boolean required) {
		// TODO Auto-generated method stub
		return new Date();
	}

}
