package javatemp;

public class main {
	
	public static void main(String[] args)
	{
		
		System.out.println(" started reading");
		
		objread obj = new objread();
		String as = obj.ReadObjectRepo("HPlogin");
		System.out.println(as);
		
		//objread2 obj = new objread2();
		//String as2 = obj.ReadObjectRepo2("datarepo", "email");
		//System.out.println(as2);
		
				
	}
	

}
