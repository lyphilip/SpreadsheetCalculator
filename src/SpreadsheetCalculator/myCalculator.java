package SpreadsheetCalculator;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Scanner;

public class myCalculator {
	
	public static void main(String[] args) throws Exception{
		
		List<String> in = new ArrayList<String>();
		Scanner stdin = new Scanner(System.in);
	    while(stdin.hasNextLine())
	    {
	        String line = stdin.nextLine();
	        in.add(line);
	    }
	    stdin.close();
		/*
	    in.add("3 2");
		in.add("A2");
		in.add("4 5 *");
		in.add("A1");
		in.add("A1 B2 / 2 +");
		in.add("3");		
		in.add("39 B1 B2 * /");
		*/
		Spreadsheet ss = new Spreadsheet(in);
		
		ss.calculateSpreadsheet();
		
	}
	
}
