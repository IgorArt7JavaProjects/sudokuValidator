package org.javasoft;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * This program validates an input file if it contains valid Sudoku combination.
 * 
 * For such small project I don't create an extra class to execute.
 * 
 * @author ArtimenkoI
 * @param Full
 *            path of input file containing
 * 
 */
public class SudokuValidator {

	public static void main(String[] args) {
		SudokuValidator sudokuValidator = new SudokuValidator();

		if ( args == null || args.length == 0 || args[0].isEmpty()) {
			System.out.println("SudokuValidator expects first parameter as an input file.");
			return;
		}

		boolean isValid = sudokuValidator.performValidation(args[0]);
		System.out.println( isValid ? "It a valid sudoku" : "It's an invalid sudoku" );
	}

	public boolean performValidation(String sudokuDataFile) {
		boolean isValid = false;

		Byte[][] sudokuData = parseAndValidateInputFile( sudokuDataFile );
		if( sudokuData == null ) { return isValid; };
		
		// 2. Validate each row that there is no duplications among any line.
		for (int row = 0; row < sudokuData.length; row++) {
			// It should be memory efficient because 
			// it only creates a single object and collects references to other existing objects  
			Set<Byte> uniqueValues = new HashSet<Byte>();
			for (int col = 0; col < 9; col++) {
				uniqueValues.add( sudokuData[row][col] );
			}
			if( uniqueValues.size() < 9 ) {
				System.out.println( "Row " + row + " is not unique" );
				return isValid;
			}
		}
		
		// 3. Validate each column that there is no duplications among any column.
		for (int col = 0; col < 9; col++) {
			Set<Byte> uniqueValues = new HashSet<Byte>();
			for (int row = 0; row < sudokuData.length; row++) {
				uniqueValues.add( sudokuData[row][col] );
			}
			if( uniqueValues.size() < 9 ) {
				System.out.println( "Column " + col + " is not unique" );
				return isValid;
			}
		}
		
		// 4. Validate each 3X3 region that there are no duplications within the region.
		for (int regionRNumber = 0; regionRNumber <= 6; regionRNumber =  regionRNumber + 3 ) {
			
			for (int regionCNumber = 0; regionCNumber <= 6; regionCNumber =  regionCNumber + 3 ) {
							
				Set<Byte> uniqueValues = new HashSet<Byte>();
				for (int rowRegion = regionRNumber; rowRegion < regionRNumber + 3; rowRegion++) {
					for (int colRegion = regionCNumber; colRegion < regionCNumber + 3; colRegion++) {
						uniqueValues.add( sudokuData[rowRegion][colRegion] );
					}				
				}
				if( uniqueValues.size() < 9 ) {
					return isValid;
				}
				
			}			
			
		}
		
		// If all 4 steps were successful then we consider it's a valid sudoku.
		isValid = true;
		return isValid;
	}
	
	
	/**
	 * Parses and validates an input file
	 * 
	 * @param sudokuDataFile
	 * 
	 * @return null if sudokuDataFile was not parsed or it's invalid
	 *  
	 */
	private Byte[][] parseAndValidateInputFile( String sudokuDataFile ) {
		
		Byte[][] sudokuData = new Byte[9][9];
		
		Path path = Paths.get( sudokuDataFile );
		
		short rowCounter = 0;
		
		// Import data from specified file, validate it and populate 2D array of Integer values	
		try ( BufferedReader reader = Files.newBufferedReader( path, Charset.forName( "UTF-8" ) ) ) {
			String currentLine = null;
			while ( (currentLine = reader.readLine() ) != null) {// while there is content on the current line

				// By pass empty lines
				if( currentLine.isEmpty() ) {
					continue;
				}
				// If at least one row length is not 9 digits then it's an invalid one
				if( currentLine.length() != 9 ) { return null; };

				// Populate each row from currentLine
				for (int i = 0; i < currentLine.length(); i++) {
					sudokuData[rowCounter][i] = new Byte( currentLine.substring(i,i+1) );
				}
				
				rowCounter++;				

			}
			// 1. Validate that we have exactly 9X9 data matrix.
			if( rowCounter != 9 ) return null;
			
		} catch ( IOException | NumberFormatException ex ) {
			ex.printStackTrace(); // handle an exception here
			return null;
		}
		
		return sudokuData;
		
	}

}
