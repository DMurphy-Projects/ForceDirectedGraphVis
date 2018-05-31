package Model;


public class GenericOption <E>{

	private E mOption;
	private String mName;
	
	public GenericOption(String name, E dec)
	{
		mOption = dec;
		mName = name;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public E getOption()
	{
		return mOption;
	}
}
