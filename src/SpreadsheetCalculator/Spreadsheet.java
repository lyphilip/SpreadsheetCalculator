package SpreadsheetCalculator;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Spreadsheet {
	private int width = 0; // width and height of the spreadsheet
	private int height = 0;
	private String[][] input = null;
	private double[][] resultSheet = null;

	private class coordinateWrapper {
		private char y;
		private int x;

		public coordinateWrapper(char i, int j){
			x = j;
			y = i;
		}
		public int getX(){
			return x;
		}
		public char getY(){
			return y;
		}

	}
	public Spreadsheet(List<String> in){
		String sheetinfo = in.remove(0);
		String[] parts = sheetinfo.split(" ");
		if(parts.length>2){
			throw new IllegalArgumentException("invalid sheet initiation info! "+sheetinfo);
		}
		int n = Integer.valueOf(parts[0]);
		int m = Integer.valueOf(parts[1]);
		if(n > 26 || n < 0){
			throw new IllegalArgumentException("invalid sheet initiation info! "+sheetinfo);
		}
		BuildSpreadsheet(n,m);

		//start get input data from stdin, store input data to input
		Iterator<String> itr = in.iterator();

		for(int j = 0; j< height; j++){
			for(int i = 0;i < width;i++){
				if(!itr.hasNext()){
					throw new IllegalArgumentException("invalid data input!");
				}
				input[i][j] = itr.next();
				resultSheet[i][j] = -1.0;
			}
		}

	}

	public void calculateSpreadsheet(){
		if(input==null || resultSheet==null){
			return;
		}
		//i for n, j for m
		for(int i = 0;i < width;i++){
			for(int j = 0; j< height; j++){
				if(resultSheet[i][j] == -1.0){
					resultSheet[i][j] = evalPostfixExpression(input[i][j],i,j);
				}
			}
		}

		for(int j = 0; j< height; j++){
			for(int i = 0;i < width;i++){
				double value = resultSheet[i][j];
				String str = String.format("%.5f",value);
				System.out.println(str);
			}
		}
	}

	private void BuildSpreadsheet(int n, int m){
		this.width = n;
		this.height = m;
		input = new String[n][m];
		resultSheet = new double[n][m];
	}

	private double evalPostfixExpression(String expr, int n, int m) {  //i row, j col
		Stack<Double> stack = new Stack<Double>(); // need to stack here
		if(isDigit(expr)){
			resultSheet[n][m] = Double.parseDouble(input[n][m]);
			return resultSheet[n][m];
		}
		String[] strs = expr.split(" ");
		for(int i = 0; i<strs.length;i++){
			String c= strs[i];
			if(isDigit(c)){
				//System.out.println(Double.parseDouble(c));
				stack.push(Double.parseDouble(c));
			}else if (isCellRef(c)) {
				coordinateWrapper crd = parseCellRef(c);
				int row = crd.getX() - 1;
				int col = crd.getY() - 'A';  //pay attend here!  A2 => [1][0]
				if(row>height || col>width){
					throw new IllegalArgumentException("wrong input! out of sheet index");
				}
				if(resultSheet[row][col]== (-1.0)){
					stack.push(evalPostfixExpression(input[row][col],row,col));
				}else{
					stack.push(resultSheet[row][col]);
				}
			} else if (isOperator(c)) {  
				double b = stack.pop(); // need to be careful of the order here!!!!!
				double a = stack.pop();  
				stack.push(execOp(c, a, b));  
			}else {
				throw new IllegalArgumentException("Wrong input: "+expr);  
			}  
		}
		double result = stack.pop();
		if(!stack.isEmpty()){
			throw new IllegalArgumentException("Check postfix expression!"+expr); 
		}
		resultSheet[n][m] = result;
		return result;  
	}  

	private double execOp(String op, double a, double b) {
		if(op.length()!=1){
			throw new IllegalArgumentException("Wrong input!");
		}
		switch (op.charAt(0)) {  
		case '+':  
			return (a + b);  
		case '-':  
			return (a - b);  
		case '*':  
			return (a * b); // assume that result < MAX_VALUE  
		case '/':  
			return (a / b); 
		}  
		throw new IllegalArgumentException("Wrong input!");  
	}  

	private boolean isDigit(String s) {
		for(int i = 0; i < s.length(); i++){
			char c= s.charAt(i);
			if (c < '0' || c > '9'){
				return false; 
			}
		}
		return true; 
	} 

	private boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		}
		return true;
	}

	private boolean isCellRef(String s){
		char row = s.charAt(0);
		String column = s.substring(1, s.length());
		if(row >= 'A' && row <= 'Z'){
			if(isInteger(column)){
				return true;
			}
		}
		return false;
	}

	private coordinateWrapper parseCellRef(String s){
		char col = s.charAt(0);
		String row = s.substring(1, s.length());
		int rowInt = 0;
		if(col >= 'A' && col <= 'Z'){ 
			rowInt = Integer.valueOf(row); 
			return new coordinateWrapper(col,rowInt);
		}else{
			return null; 
		}
	}

	private boolean isOperator(String c) {
		if(c.length()!=1){
			return false;
		}
		switch (c.charAt(0)) {  
		case '+':  
		case '-':  
		case '*':  
		case '/':   
			return true;  
		}  
		return false;  
	}


}
