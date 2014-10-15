package javatemp;

import java.io.BufferedReader;
import java.io.FileReader;

public class objwrite {
	
	public String ObjRepoFolder = "";
	public String BasePath = "";
	
	public String WriteObjectRepo(String ObjName)
	{
		final String PageName = "objrepo";
		BasePath = System.getProperty("user.dir") + "\\";
		ObjRepoFolder = BasePath + "Resource\\repository\\";
		
		String ReturnValue = "";
		try
		{
			//System.out.println(PageName);
			String ObjRepoPath = ObjRepoFolder + PageName ;
			BufferedReader br = new BufferedReader(new FileReader(ObjRepoPath));
			String Line = "";
	       
			while ((Line = br.readLine()) != null)
			{
				String[] LineDetails = Line.split(",",3);
				
				//System.out.println(LineDetails[0]);
				//System.out.println(LineDetails[1]);
				//System.out.println(LineDetails[2]);
				
				if(LineDetails[0].toLowerCase().trim().equals(ObjName.toLowerCase().trim()))
					
				
				{
					
					ReturnValue = LineDetails[2];
					break;
				}
			}  
							
			br.close();
		}
		catch(Exception ex)
		{
		System.out.println(ex.getMessage());	
		}
		return ReturnValue;
	}
	
	}
