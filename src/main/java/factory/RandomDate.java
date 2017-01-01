package factory;

import java.util.Date;
 

public class RandomDate extends AbstractFactoryDataType {

	@Override
	public Date getData(String format) {
		// TODO Auto-generated method stub
		return new Date();
	}

}
