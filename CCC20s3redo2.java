package SeniorCCCRedo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CCC20s3redo2 {
	static HashMap<Character, Integer> freqMapNeedle = new HashMap<Character, Integer>();
	static HashMap<Character, Integer> freqMapHaystack = new HashMap<Character, Integer>();
	static HashMap<Long, Boolean> hashOccured = new HashMap<Long, Boolean>();
	static HashMap<String, Boolean> stringOccured = new HashMap<String, Boolean>();
	static ArrayList<Character> charsInNeedle = new ArrayList<Character>();

	public static void main(String [] args) throws IOException{
			
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		
		
				
		String needle = read.readLine();
		String haystack = read.readLine();
		
		if(needle.length() > haystack.length()) {
			System.out.println(0);
			System.exit(0);
		}
		
		//perumtations of needle in haystack
		long needleHash = hash(needle);
		
		for(int i = 0; i < needle.length(); i++) {
			char c = needle.charAt(i);
			charsInNeedle.add(c);
			increment(c, freqMapNeedle);
		}
		
		String eval = haystack.substring(0, needle.length());
		long haystackHash = hash(eval);
		long posHaystackHash = hashPos(eval);
		
		for(int i = 0; i < needle.length(); i++) {
			char c = haystack.charAt(i);
			increment(c, freqMapHaystack);
		}
		
		int count = 0;
		
		if(haystackHash == needleHash && compareFrequency()) {
			count += 1;
			stringOccured.put(eval, true);
			hashOccured.put(posHaystackHash, true);
		}
		
		char prev = haystack.charAt(0);
		for(int i = 1; i < haystack.length()-needle.length()+1; i++) {
			//get last char
			decrement(prev, freqMapHaystack);
			char last = haystack.charAt(i+needle.length()-1);
			increment(last, freqMapHaystack);
			String sub = haystack.substring(i, i+needle.length());
			//adjust pos hash
			posHaystackHash -= hashCharPos(prev, needle.length()-1);
			posHaystackHash *= 5;
			posHaystackHash += hashCharPos(last, 0);
			
			boolean occured = hashOccured.get(posHaystackHash) != null 
					&& hashOccured.get(posHaystackHash)
					&& stringOccured.get(sub) != null 
					&& stringOccured.get(sub);
			
			// adjust hash
			if(!occured) {
				haystackHash -= hashChar(prev);
				haystackHash += hashChar(last);
				if(haystackHash == needleHash) {
					if(compareFrequency()) {
						count += 1;
					
						hashOccured.put(posHaystackHash, true);
						stringOccured.put(sub, true);
					}
				}
			}
			else {
				haystackHash -= hashChar(prev);
				haystackHash += hashChar(last);
			}
			prev = haystack.charAt(i);
		}
		
		System.out.println(count);
	}
	
	/*abc
	 * a:0, b:1, c:1 d:1
	 * abcde
	 * a:0 b:1 c:1 d:1 length of n
	 * b:1 c:1 d:1 length n
	 * 

	1*23^2 + 2*23^1 + 3*23^0
	23^ascii

	a: 97*/
	
	
	private static long hash(String s) {
		int prime = 5; // prime 
		long sum = 0; // keeps track 
		for(int i = 0; i < s.length(); i++) {
		
			int ascii = getAscii(s.charAt(i)); //get ascii value for char 
			long power = (int) Math.pow(prime, ascii); 
			
			sum += power;
		}
		return sum;
	}
	
	private static long hashPos(String s) {
		int prime = 5; // prime 
		long sum = 0; // keeps track 
		for(int i = 0; i < s.length(); i++) {
			int pos = s.length() - i - 1;
			int ascii = getAscii(s.charAt(i)); //get ascii value for char 
			long power = (int) Math.pow(prime, pos); 
			long toAdd = ascii*power;
			sum += toAdd;
		}
		return sum;
	}
	
	private static int getAscii(char c) {
		return (int)(c) - 96;
	}
	//get value of individual a b c
	private static long hashChar(char c) {
		int ascii = getAscii(c); //get ascii value for char 
		long power = (int) Math.pow(5, ascii); 
		return power;
	}
	
	private static long hashCharPos(char c, int pos) {
		int ascii = getAscii(c); //get ascii value for char 
		long power = (int) Math.pow(5, pos); 
		return ascii*power;
	}
	
	private static int increment(char c, HashMap<Character, Integer> map) {
		int value = (map.get(c) == null) ? 0 : map.get(c);
		value += 1;
		map.put(c, value);
		return value;
	}
	
	private static int decrement(char c, HashMap<Character, Integer> map) {
		int value =  map.get(c);
		value -= 1;
		
		map.put(c, value);
		return value;
	}
	
	private static boolean compareFrequency() {
		for(char c : charsInNeedle) {
			int ni = freqMapNeedle.get(c);
			int hi = freqMapHaystack.get(c);
			if(ni != hi) {
				return false;
			}
		}
		return true;
	}
	
}
