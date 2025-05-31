import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.util.regex.*;

public class LineBreaker3000 {
    //rhymes with grug
    public static void main(String[] args) {
		System.out.println("LineBreaker3000 by Jordon \"Humanish\" Olson. Some rights reserved.\n");
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        
        // Generate random output filename
        String outputFilePath = "unlined_" + Math.abs(random.nextInt(100000)) + ".txt";
        
        int mode = 0;
        boolean validInput = false;
        
        // Keep asking until we get valid input
       while (!validInput) {
    System.out.println("Choose mode: (1) File mode, (2) DEF Folder mode, (3) Custom Extension Folder mode, " +
                       "(4) Strict Mode, (5) Profanity Filter Mode, (6) CSV to ASCII Table Mode, " +
                       "(7) Fancy for Words.csv Mode, (8) CSV Column Replacement Mode, "
					   + " or (9) for CSVFixer Mode.");
    try {
        mode = scanner.nextInt();
        if (mode >= 1 && mode <= 9) {
            validInput = true;
        } else {
            System.out.println("Invalid selection. Please enter a number between 1 and 9.");
        }
    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a number between 1 and 9.");
        scanner.nextLine(); // Clear the invalid input
    }
}

        
        scanner.nextLine(); // consume newline after the valid integer input
        
        try {
            if (mode == 1) {
                // File mode
                List<String> inputFiles = new ArrayList<>();
                System.out.println("Enter file paths (one per line, empty line to finish):");
                System.out.println("You can include paths with quotes, e.g., \"C:\\folder\\file.txt\"");
                
                String filePath;
                while (!(filePath = scanner.nextLine()).isEmpty()) {
                    // Remove surrounding quotes if present
                    filePath = removeQuotes(filePath);
                    inputFiles.add(filePath);
                }
                
                processFiles(inputFiles, outputFilePath, false, false);
            } else if (mode == 2) {
                // DEF Folder mode
                System.out.println("Enter folder path (you can include quotes if needed):");
                String folderPath = removeQuotes(scanner.nextLine());
                
                List<String> defFiles = Files.list(Paths.get(folderPath))
                    .filter(path -> path.toString().toLowerCase().endsWith(".def"))
                    .map(Path::toString)
                    .sorted()
                    .collect(Collectors.toList());
                
                processFiles(defFiles, outputFilePath, false, false);
            } else if (mode == 3) {
                // Custom Extension Folder mode
                System.out.println("Enter folder path (you can include quotes if needed):");
                String folderPath = removeQuotes(scanner.nextLine());
                
                System.out.println("Enter file extension (without dot, e.g. 'txt'):");
                String extension = scanner.nextLine();
                
                // Make sure extension doesn't start with a dot
                if (extension.startsWith(".")) {
                    extension = extension.substring(1);
                }
                
                final String fileExtension = extension;
                List<String> customFiles = Files.list(Paths.get(folderPath))
                    .filter(path -> path.toString().toLowerCase().endsWith("." + fileExtension.toLowerCase()))
                    .map(Path::toString)
                    .sorted()
                    .collect(Collectors.toList());
                
                if (customFiles.isEmpty()) {
                    System.out.println("No files with extension '" + extension + "' found in the specified folder.");
                } else {
                    System.out.println("Found " + customFiles.size() + " files with extension '" + extension + "'");
                    processFiles(customFiles, outputFilePath, false, false);
                }
            } else if (mode == 4) {
                // Strict Mode
                System.out.println("Do you want to process multiple files? (y/n):");
                String multipleFilesChoice = scanner.nextLine().trim().toLowerCase();
                boolean multipleFiles = multipleFilesChoice.equals("y") || multipleFilesChoice.equals("yes");
                
                List<String> inputFiles = new ArrayList<>();
                
                if (multipleFiles) {
                    System.out.println("Enter file paths (one per line, empty line to finish):");
                    System.out.println("You can include paths with quotes, e.g., \"C:\\folder\\file.txt\"");
                    
                    String filePath;
                    while (!(filePath = scanner.nextLine()).isEmpty()) {
                        // Remove surrounding quotes if present
                        filePath = removeQuotes(filePath);
                        inputFiles.add(filePath);
                    }
                } else {
                    System.out.println("Enter file path (you can include quotes if needed):");
                    String filePath = removeQuotes(scanner.nextLine());
                    inputFiles.add(filePath);
                }
                
                System.out.println("Running in Strict Mode - only a-z, A-Z, periods, and hyphens will be kept");
                processFiles(inputFiles, outputFilePath, true, false);
            } else if (mode == 5) {
                // Profanity Filter Mode
                System.out.println("Do you want to process multiple files? (y/n):");
                String multipleFilesChoice = scanner.nextLine().trim().toLowerCase();
                boolean multipleFiles = multipleFilesChoice.equals("y") || multipleFilesChoice.equals("yes");
                
                List<String> inputFiles = new ArrayList<>();
                
                if (multipleFiles) {
                    System.out.println("Enter file paths (one per line, empty line to finish):");
                    System.out.println("You can include paths with quotes, e.g., \"C:\\folder\\file.txt\"");
                    
                    String filePath;
                    while (!(filePath = scanner.nextLine()).isEmpty()) {
                        // Remove surrounding quotes if present
                        filePath = removeQuotes(filePath);
                        inputFiles.add(filePath);
                    }
                } else {
                    System.out.println("Enter file path (you can include quotes if needed):");
                    String filePath = removeQuotes(scanner.nextLine());
                    inputFiles.add(filePath);
                }
                
                System.out.println("Running in Profanity Filter Mode - removing lines with offensive content");
                processFiles(inputFiles, outputFilePath, false, true);
            } else if (mode == 6) {
    // CSV to ASCII Table Mode
    System.out.println("Enter CSV file path (you can include quotes if needed):");
    String filePath = removeQuotes(scanner.nextLine());
    
    System.out.println("Choose output format: (1) ASCII, (2) HTML");
    int outputFormat = 1;
    try {
        outputFormat = scanner.nextInt();
        if (outputFormat < 1 || outputFormat > 2) {
            System.out.println("Invalid selection. Using ASCII format (1) as default.");
            outputFormat = 1;
        }
    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Using ASCII format (1) as default.");
    }
    scanner.nextLine(); // consume newline
    
    if (outputFormat == 1) {
        // Original ASCII table functionality
        System.out.println("Choose table style: (1) Simple, (2) Classic, (3) Fancy");
        int tableStyle = 1;
        try {
            tableStyle = scanner.nextInt();
            if (tableStyle < 1 || tableStyle > 3) {
                System.out.println("Invalid selection. Using Simple style (1) as default.");
                tableStyle = 1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Using Simple style (1) as default.");
        }
        scanner.nextLine(); // consume newline
        
        System.out.println("Does the CSV have a header row? (y/n):");
        String headerChoice = scanner.nextLine().trim().toLowerCase();
        boolean hasHeader = headerChoice.equals("y") || headerChoice.equals("yes");
        
        System.out.println("Running in CSV to ASCII Table Mode");
        convertCsvToAsciiTable(filePath, outputFilePath, tableStyle, hasHeader);
    } else {
        // HTML table functionality
        System.out.println("Does the CSV have a header row? (y/n):");
        String headerChoice = scanner.nextLine().trim().toLowerCase();
        boolean hasHeader = headerChoice.equals("y") || headerChoice.equals("yes");
        
        System.out.println("Running in CSV to HTML Table Mode");
        convertCsvToHtmlTable(filePath, hasHeader);
    }
            } else if (mode == 7) {
                // Fancy for Words.csv Mode
                System.out.println("Enter CSV file path (you can include quotes if needed):");
                String filePath = removeQuotes(scanner.nextLine());
                
                System.out.println("Does the CSV have a header row? (y/n):");
                String headerChoice = scanner.nextLine().trim().toLowerCase();
                boolean hasHeader = headerChoice.equals("y") || headerChoice.equals("yes");
                
                System.out.println("Running in Fancy for Words.csv Mode");
                convertCsvToWordsTable(filePath, outputFilePath, hasHeader);
			}
			else if (mode == 8) {
    // CSV Column Replacement Mode
    System.out.println("Enter CSV file path (you can include quotes if needed):");
    String filePath = removeQuotes(scanner.nextLine());
    
    System.out.println("Enter replacement mapping CSV file path (you can include quotes if needed):");
    String mappingFilePath = removeQuotes(scanner.nextLine());
    
    System.out.println("Running in CSV Column Replacement Mode");
    replaceCsvColumnValues(filePath, mappingFilePath, outputFilePath);
}
else if (mode == 9) {
    // CSVFixer Mode
    System.out.println("Enter CSV file path (you can include quotes if needed):");
    String filePath = removeQuotes(scanner.nextLine());
    
    System.out.println("Running in CSVFixer Mode");
    fixCsvFile(filePath, outputFilePath);
}

            
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
 
 /**
 * Fixes a CSV file by ensuring all rows have the same number of columns as the header.
 * - Rows with fewer columns are merged with rows above them
 * - Rows with more columns are trimmed
 */
private static void fixCsvFile(String csvFilePath, String outputFilePath) throws IOException {
    // Read all lines from the CSV file
    List<String> csvLines = Files.readAllLines(Paths.get(csvFilePath));
    
    if (csvLines.isEmpty()) {
        System.out.println("CSV file is empty!");
        return;
    }
    
    // Detect separator and parse the header row
    String separator = detectSeparator(csvLines.get(0));
    List<String> headerColumns = parseCsvLine(csvLines.get(0), separator);
    int expectedColumnCount = headerColumns.size();
    
    System.out.println("CSV header has " + expectedColumnCount + " columns");
    
    List<String> fixedLines = new ArrayList<>();
    fixedLines.add(csvLines.get(0)); // Add header line unchanged
    
    List<String> lastRowData = new ArrayList<>(headerColumns); // Initialize with header for first row comparison
    int mergedRows = 0;
    int trimmedRows = 0;
    
    // Process each data row (skip header)
    for (int i = 1; i < csvLines.size(); i++) {
        String currentLine = csvLines.get(i);
        List<String> currentColumns = parseCsvLine(currentLine, separator);
        
        if (currentColumns.size() < expectedColumnCount) {
            // This row has fewer columns than expected - merge with previous row
            mergedRows++;
            
            // Get the columns from the previous fixed row
            List<String> mergedColumns = new ArrayList<>(lastRowData);
            
            // Add content from current row to appropriate columns of the previous row
            for (int j = 0; j < currentColumns.size(); j++) {
                String existingContent = mergedColumns.get(j);
                String additionalContent = currentColumns.get(j);
                
                if (!additionalContent.trim().isEmpty()) {
                    if (!existingContent.trim().isEmpty()) {
                        // Add space between content for readability
                        mergedColumns.set(j, existingContent + " " + additionalContent);
                    } else {
                        mergedColumns.set(j, additionalContent);
                    }
                }
            }
            
            // Update the last line in the fixed lines list
            String mergedLine = convertColumnsToLine(mergedColumns, separator);
            fixedLines.set(fixedLines.size() - 1, mergedLine);
            
            // Update the last row data for next comparison
            lastRowData = mergedColumns;
        } else if (currentColumns.size() > expectedColumnCount) {
            // This row has more columns than expected - trim extra columns
            trimmedRows++;
            List<String> trimmedColumns = currentColumns.subList(0, expectedColumnCount);
            String fixedLine = convertColumnsToLine(trimmedColumns, separator);
            fixedLines.add(fixedLine);
            
            // Update the last row data for next comparison
            lastRowData = trimmedColumns;
        } else {
            // This row has the correct number of columns - keep as is
            fixedLines.add(currentLine);
            
            // Update the last row data for next comparison
            lastRowData = currentColumns;
        }
    }
    
    // Write fixed lines to output file
    Files.write(Paths.get(outputFilePath), fixedLines);
    
    System.out.println("CSV fixing completed successfully!");
    System.out.println("Merged " + mergedRows + " rows with too few columns");
    System.out.println("Trimmed " + trimmedRows + " rows with too many columns");
    System.out.println("Output file: " + new File(outputFilePath).getAbsolutePath());
}

/**
 * Converts a list of column values back to a CSV line
 */
private static String convertColumnsToLine(List<String> columns, String separator) {
    StringBuilder line = new StringBuilder();
    
    for (int i = 0; i < columns.size(); i++) {
        String value = columns.get(i);
        
        // Check if we need to quote this value
        boolean needsQuotes = value.contains(separator) || 
                             value.contains("\"") || 
                             value.contains("\n") || 
                             value.contains(",");
        
        if (needsQuotes) {
            // Escape any quotes in the value
            value = value.replace("\"", "\"\"");
            line.append("\"").append(value).append("\"");
        } else {
            line.append(value);
        }
        
        // Add separator between columns, but not after the last column
        if (i < columns.size() - 1) {
            line.append(separator);
        }
    }
    
    return line.toString();
}

 /**
 * Converts a CSV file to a specialized ASCII table for Words.csv
 * - First and third columns get 8 characters each
 * - Second column gets 16 characters
 * - Fourth column gets the rest (43 characters)
 * - Total width is exactly 80 characters
 * - All `b and `n instances are removed
 */
private static void convertCsvToWordsTable(String csvFilePath, String outputFilePath, 
                                           boolean hasHeader) throws IOException {
    // Read all lines from the CSV file
    List<String> csvLines = Files.readAllLines(Paths.get(csvFilePath));
    
    if (csvLines.isEmpty()) {
        System.out.println("CSV file is empty!");
        return;
    }
    
    // Parse CSV data
    List<List<String>> data = new ArrayList<>();
    String separator = detectSeparator(csvLines.get(0));
    
    for (String line : csvLines) {
        List<String> row = parseCsvLine(line, separator);
        
        // Ensure row has exactly 4 columns, pad with empty strings if needed
        while (row.size() < 4) {
            row.add("");
        }
        
        // Remove `b and `n from all cells
        for (int i = 0; i < row.size(); i++) {
            String cleaned = row.get(i).replace("`b", "").replace("`n", "");
            row.set(i, cleaned);
        }
        
        data.add(row);
    }
    
    // Fixed column widths for Words.csv format
    int[] columnWidths = {7, 16, 7, 37};
    
    // Generate ASCII table
    StringBuilder asciiTable = new StringBuilder();
    
    // Get fancy table symbols
    TableSymbols symbols = getTableSymbols(3); // Use Fancy style (3)
    
    // Top border - ensure exactly 80 characters
    String topBorder = generateWordsTableBorder(columnWidths, symbols.topLeft, 
                                              symbols.topMiddle, symbols.topRight, symbols.horizontal);
    asciiTable.append(topBorder).append('\n');
    
    // Initialize list to store the last non-empty value for each column
    List<String> lastNonEmptyValues = new ArrayList<>(Collections.nCopies(4, null));
    
    // Header row
    if (hasHeader && !data.isEmpty()) {
        List<String> headerRow = data.get(0);
        appendWordsTableRow(asciiTable, headerRow, columnWidths, symbols.vertical);
        
        // Update lastNonEmptyValues with header values
        for (int i = 0; i < headerRow.size() && i < 4; i++) {
            if (!headerRow.get(i).trim().isEmpty()) {
                lastNonEmptyValues.set(i, headerRow.get(i));
            }
        }
        
        // Header separator - ensure exactly 80 characters
        String headerSep = generateWordsTableBorder(columnWidths, symbols.leftMiddle, 
                                                  symbols.middle, symbols.rightMiddle, symbols.horizontal);
        asciiTable.append(headerSep).append('\n');
        
        // Data rows (skip header)
        for (int i = 1; i < data.size(); i++) {
            appendWordsTableRowWithWrapping(asciiTable, data.get(i), columnWidths, symbols.vertical, lastNonEmptyValues);
            
            // Add separator between rows
            if (i < data.size() - 1) {
                asciiTable.append(generateWordsTableBorder(columnWidths, symbols.leftMiddle, 
                                                        symbols.middle, symbols.rightMiddle, symbols.horizontal))
                         .append('\n');
            }
        }
    } else {
        // No header, just data rows
        for (int i = 0; i < data.size(); i++) {
            appendWordsTableRowWithWrapping(asciiTable, data.get(i), columnWidths, symbols.vertical, lastNonEmptyValues);
            
            // Add separator between rows, except after the last row
            if (i < data.size() - 1) {
                asciiTable.append(generateWordsTableBorder(columnWidths, symbols.leftMiddle, 
                                                        symbols.middle, symbols.rightMiddle, symbols.horizontal))
                         .append('\n');
            }
        }
    }
    
    // Bottom border - ensure exactly 80 characters
    String bottomBorder = generateWordsTableBorder(columnWidths, symbols.bottomLeft, 
                                                 symbols.bottomMiddle, symbols.bottomRight, symbols.horizontal);
    asciiTable.append(bottomBorder).append('\n');
    
    // Add a title showing the source file
    String title = "ASCII Table generated from: " + new File(csvFilePath).getName();
    asciiTable.insert(0, title + "\n\n");
    
    // Write to output file
    Files.write(Paths.get(outputFilePath), asciiTable.toString().getBytes());
    
    System.out.println("CSV successfully converted to Words.csv ASCII table!");
    System.out.println("Output file: " + new File(outputFilePath).getAbsolutePath());
}

/**
 * Generates a table border with exact width of 80 characters for Words.csv format
 */
/**
 * Generates a table border with exact width of 80 characters for Words.csv format
 */
private static String generateWordsTableBorder(int[] columnWidths, char leftChar, 
                                             char middleChar, char rightChar, char horizontalChar) {
    StringBuilder sb = new StringBuilder();
    sb.append(leftChar);
    
    // Calculate border width:
    // 1 character for left border + 
    // (sum of column widths + 2 spaces per column) +
    // 3 characters for column separators +
    // 1 character for right border
    // Total: should be 80 characters
    
    for (int i = 0; i < columnWidths.length; i++) {
        // Add horizontal characters for each column width + 2 spaces
        for (int j = 0; j < columnWidths[i] + 2; j++) {
            sb.append(horizontalChar);
        }
        
        if (i < columnWidths.length - 1) {
            sb.append(middleChar);
        }
    }
    
    sb.append(rightChar);
    
    // If the length isn't exactly 80, adjust it
    if (sb.length() > 80) {
        // Trim some characters from the last column
        sb.setLength(80);
        sb.setCharAt(79, rightChar);
    } else if (sb.length() < 80) {
        // Add more characters to the last column
        int toAdd = 80 - sb.length();
        int insertPos = sb.length() - 1;
        for (int i = 0; i < toAdd; i++) {
            sb.insert(insertPos, horizontalChar);
        }
    }
    
    return sb.toString();
}


/**
 * Append a fixed-width table row for Words.csv format (used for header rows)
 */
private static void appendWordsTableRow(StringBuilder sb, List<String> rowData, 
                                       int[] columnWidths, char verticalChar) {
    StringBuilder rowBuilder = new StringBuilder();
    rowBuilder.append(verticalChar);
    
    for (int i = 0; i < columnWidths.length; i++) {
        String cellValue = i < rowData.size() ? rowData.get(i) : "";
        
        // If cell value is too long, truncate it
        if (cellValue.length() > columnWidths[i]) {
            cellValue = cellValue.substring(0, columnWidths[i]);
        }
        
        rowBuilder.append(' ').append(cellValue);
        
        // Pad with spaces to match column width
        int padding = columnWidths[i] - cellValue.length();
        if (padding > 0) {
            rowBuilder.append(" ".repeat(padding));
        }
        
        rowBuilder.append(' ').append(verticalChar);
    }
    
    // Verify we have exactly 80 characters
    if (rowBuilder.length() != 80) {
        throw new IllegalStateException("Row length is " + rowBuilder.length() + " instead of 80");
    }
    
    sb.append(rowBuilder).append('\n');
}

/**
 * Append a data row to the table with text wrapping for Words.csv format
 * Empty cells will repeat the most recent non-empty text in their column
 */
private static void appendWordsTableRowWithWrapping(StringBuilder sb, List<String> rowData, 
                                                  int[] columnWidths, char verticalChar,
                                                  List<String> lastNonEmptyValues) {
    // Split cell content into multiple lines if needed
    List<String[]> wrappedRowContent = new ArrayList<>();
    int maxWrappedLines = 1;
    
    for (int i = 0; i < columnWidths.length; i++) {
        String cellValue = i < rowData.size() ? rowData.get(i) : "";
        
        // If cell is empty, use the last non-empty value for this column
        if (cellValue.trim().isEmpty() && i < lastNonEmptyValues.size() && lastNonEmptyValues.get(i) != null) {
            cellValue = lastNonEmptyValues.get(i);
        } else if (!cellValue.trim().isEmpty()) {
            // Update the last non-empty value for this column
            if (i < lastNonEmptyValues.size()) {
                lastNonEmptyValues.set(i, cellValue);
            } else {
                // Add to the list if needed
                lastNonEmptyValues.add(cellValue);
            }
        }
        
        String[] wrappedContent;
        // For the fourth column (index 3), we use text wrapping to ensure nothing is lost
        if (i == 3) {
            wrappedContent = wrapText(cellValue, columnWidths[i]);
        } else {
            // For other columns, just truncate if necessary
            if (cellValue.length() <= columnWidths[i]) {
                wrappedContent = new String[]{cellValue};
            } else {
                wrappedContent = new String[]{cellValue.substring(0, columnWidths[i])};
            }
        }
        
        wrappedRowContent.add(wrappedContent);
        maxWrappedLines = Math.max(maxWrappedLines, wrappedContent.length);
    }
    
    // Output each wrapped line
    for (int lineIdx = 0; lineIdx < maxWrappedLines; lineIdx++) {
        StringBuilder rowBuilder = new StringBuilder();
        rowBuilder.append(verticalChar);
        
        for (int colIdx = 0; colIdx < columnWidths.length; colIdx++) {
            String[] colContent = wrappedRowContent.get(colIdx);
            String line = lineIdx < colContent.length ? colContent[lineIdx] : "";
            
            rowBuilder.append(' ').append(line);
            
            // Pad with spaces to match column width
            int padding = columnWidths[colIdx] - line.length();
            if (padding > 0) {
                rowBuilder.append(" ".repeat(padding));
            }
            
            rowBuilder.append(' ').append(verticalChar);
        }
        
        // Verify we have exactly 80 characters
        if (rowBuilder.length() != 80) {
            throw new IllegalStateException("Row length is " + rowBuilder.length() + " instead of 80");
        }
        
        sb.append(rowBuilder).append('\n');
    }
}
/**
 * Replaces values in the first column of a CSV file based on a mapping from another CSV
 */
/**
 * Replaces values in the first column of a CSV file based on a mapping from another CSV
 */
/**
 * Replaces values in the first column of a CSV file based on a mapping from another CSV
 */
private static void replaceCsvColumnValues(String csvFilePath, String mappingFilePath, 
                                          String outputFilePath) throws IOException {
    // Read the mapping file
    List<String> mappingLines = Files.readAllLines(Paths.get(mappingFilePath));
    
    if (mappingLines.isEmpty()) {
        System.out.println("Mapping file is empty!");
        return;
    }
    
    // Parse mapping data - assume first line is header
    Map<String, String> replacementMap = new HashMap<>();
    String mapSeparator = detectSeparator(mappingLines.get(0));
    
    // Skip header row
    for (int i = 1; i < mappingLines.size(); i++) {
        List<String> row = parseCsvLine(mappingLines.get(i), mapSeparator);
        if (row.size() >= 2) {
            // Map from second column (word) to first column (ID)
            replacementMap.put(row.get(1).trim(), row.get(0).trim());
        }
    }
    
    System.out.println("Loaded " + replacementMap.size() + " replacement mappings");
    
    // Now process the target CSV file
    List<String> csvLines = Files.readAllLines(Paths.get(csvFilePath));
    
    if (csvLines.isEmpty()) {
        System.out.println("Target CSV file is empty!");
        return;
    }
    
    String separator = detectSeparator(csvLines.get(0));
    List<String> outputLines = new ArrayList<>();
    int replacementCount = 0;
    
    // Sort the words by length (descending) to prevent partial replacements
    List<Map.Entry<String, String>> sortedEntries = new ArrayList<>(replacementMap.entrySet());
    sortedEntries.sort((e1, e2) -> Integer.compare(e2.getKey().length(), e1.getKey().length()));
    
    // Process each line
    for (String line : csvLines) {
        // Check if this is a header line
        if (outputLines.isEmpty() && line.contains("ID") && line.contains("Category")) {
            // Keep header as is
            outputLines.add(line);
            continue;
        }
        
        // Get the first column value before any separator
        int separatorIndex = line.indexOf(separator);
        if (separatorIndex <= 0) {
            // No separator or empty first column, keep line as is
            outputLines.add(line);
            continue;
        }
        
        String firstColumn = line.substring(0, separatorIndex);
        String restOfLine = line.substring(separatorIndex);
        
        // Track if we made any replacements in this cell
        boolean madeReplacement = false;
        
        // Try to replace each word in the first column
        for (Map.Entry<String, String> entry : sortedEntries) {
            String word = entry.getKey();
            String id = entry.getValue();
            
            // Case-insensitive check if the word exists in the first column
            int wordIndex = firstColumn.toLowerCase().indexOf(word.toLowerCase());
            if (wordIndex >= 0) {
                // Get the actual case as it appears in the text
                String actualWord = firstColumn.substring(wordIndex, wordIndex + word.length());
                // Replace that exact instance with the ID
                firstColumn = firstColumn.replace(actualWord, id);
                madeReplacement = true;
                replacementCount++;
            }
        }
        
        if (madeReplacement) {
            // Reconstruct the line with the modified first column
            outputLines.add(firstColumn + restOfLine);
        } else {
            // No replacements needed
            outputLines.add(line);
        }
    }
    
    // Write to output file
    Files.write(Paths.get(outputFilePath), outputLines);
    
    System.out.println("CSV column replacement completed successfully!");
    System.out.println("Made " + replacementCount + " replacements");
    System.out.println("Output file: " + new File(outputFilePath).getAbsolutePath());
}

   /*
 * Converts a CSV file to a nicely formatted ASCII table with fixed 79-character width
 */
private static void convertCsvToAsciiTable(String csvFilePath, String outputFilePath, 
                                          int tableStyle, boolean hasHeader) throws IOException {
    // Read all lines from the CSV file
    List<String> csvLines = Files.readAllLines(Paths.get(csvFilePath));
    
    if (csvLines.isEmpty()) {
        System.out.println("CSV file is empty!");
        return;
    }
    
    // Parse CSV data
    List<List<String>> data = new ArrayList<>();
    String separator = detectSeparator(csvLines.get(0));
    
    for (String line : csvLines) {
        List<String> row = parseCsvLine(line, separator);
        data.add(row);
    }
    
    int columnCount = data.get(0).size();
    
    // Fixed line width - exactly 79 characters
    final int FIXED_WIDTH = 79;
    
    // Calculate available width for content after accounting for table borders
    int borderCharsPerRow = columnCount + 1; // vertical separators
    int availableWidth = FIXED_WIDTH - borderCharsPerRow;
    
    // First pass: get initial column widths based on content
    int[] initialWidths = new int[columnCount];
    for (List<String> row : data) {
        for (int i = 0; i < row.size() && i < columnCount; i++) {
            initialWidths[i] = Math.max(initialWidths[i], row.get(i).length());
        }
    }
    
    // Adjust column widths to fit exactly within FIXED_WIDTH
    int[] columnWidths = adjustColumnWidthsToExactWidth(initialWidths, availableWidth, columnCount);
    
    // Generate ASCII table
    StringBuilder asciiTable = new StringBuilder();
    
    // Get table symbols based on style
    TableSymbols symbols = getTableSymbols(tableStyle);
    
    // Top border - ensure exactly 79 characters
    String topBorder = generateExactWidthTableBorder(columnWidths, symbols.topLeft, 
                                                    symbols.topMiddle, symbols.topRight, symbols.horizontal);
    asciiTable.append(topBorder).append('\n');
    
    // Initialize list to store the last non-empty value for each column
    List<String> lastNonEmptyValues = new ArrayList<>(Collections.nCopies(columnCount, null));
    
    // Header row
    if (hasHeader && !data.isEmpty()) {
        List<String> headerRow = data.get(0);
        appendFixedWidthTableRow(asciiTable, headerRow, columnWidths, symbols.vertical);
        
        // Update lastNonEmptyValues with header values
        for (int i = 0; i < headerRow.size() && i < columnCount; i++) {
            if (!headerRow.get(i).trim().isEmpty()) {
                lastNonEmptyValues.set(i, headerRow.get(i));
            }
        }
        
        // Header separator - ensure exactly 79 characters
        String headerSep = generateExactWidthTableBorder(columnWidths, symbols.leftMiddle, 
                                                       symbols.middle, symbols.rightMiddle, symbols.horizontal);
        asciiTable.append(headerSep).append('\n');
        
        // Data rows (skip header)
        for (int i = 1; i < data.size(); i++) {
            appendTableRowWithWrapping(asciiTable, data.get(i), columnWidths, symbols.vertical, lastNonEmptyValues);
            
            // Add separator between rows for fancy style
            if (tableStyle == 3 && i < data.size() - 1) {
                asciiTable.append(generateExactWidthTableBorder(columnWidths, symbols.leftMiddle, 
                                                             symbols.middle, symbols.rightMiddle, symbols.horizontal))
                         .append('\n');
            }
        }
    } else {
        // No header, just data rows
        for (int i = 0; i < data.size(); i++) {
            appendTableRowWithWrapping(asciiTable, data.get(i), columnWidths, symbols.vertical, lastNonEmptyValues);
            
            // Add separator between rows for fancy style
            if (tableStyle == 3 && i < data.size() - 1) {
                asciiTable.append(generateExactWidthTableBorder(columnWidths, symbols.leftMiddle, 
                                                             symbols.middle, symbols.rightMiddle, symbols.horizontal))
                         .append('\n');
            }
        }
    }
    
    // Bottom border - ensure exactly 79 characters
    String bottomBorder = generateExactWidthTableBorder(columnWidths, symbols.bottomLeft, 
                                                      symbols.bottomMiddle, symbols.bottomRight, symbols.horizontal);
    asciiTable.append(bottomBorder).append('\n');
    
    // Add a title showing the source file
    String title = "ASCII Table generated from: " + new File(csvFilePath).getName();
    asciiTable.insert(0, title + "\n\n");
    
    // Write to output file
    Files.write(Paths.get(outputFilePath), asciiTable.toString().getBytes());
    
    System.out.println("CSV successfully converted to ASCII table!");
    System.out.println("Output file: " + new File(outputFilePath).getAbsolutePath());
}
/**
 * Adjusts column widths to fit exactly within the maximum width constraint
 * with column width limits (min 8, max 40 characters)
 */
private static int[] adjustColumnWidthsToExactWidth(int[] initialWidths, int availableWidth, int columnCount) {
    int[] adjustedWidths = initialWidths.clone();
    
    // Apply initial constraints - min 8, max 40 characters per column
    int minColWidth = 8;
    int maxColWidth = 40;
    
    // First pass: Apply min/max constraints to get a baseline
    for (int i = 0; i < adjustedWidths.length; i++) {
        adjustedWidths[i] = Math.max(minColWidth, Math.min(maxColWidth, adjustedWidths[i]));
    }
    
    // Calculate total width after constraints
    int totalWidth = Arrays.stream(adjustedWidths).sum();
    
    // If width matches exactly, we're done
    if (totalWidth == availableWidth) {
        return adjustedWidths;
    }
    
    // Handle case where we can't fit minimum widths
    int minimumPossibleWidth = minColWidth * columnCount;
    if (availableWidth < minimumPossibleWidth) {
        // Distribute available width as evenly as possible
        Arrays.fill(adjustedWidths, availableWidth / columnCount);
        // Distribute remainder to the last columns (they need it most)
        int remainder = availableWidth % columnCount;
        for (int i = columnCount - 1; i >= columnCount - remainder; i--) {
            adjustedWidths[i]++;
        }
        return adjustedWidths;
    }
    
    // Second pass: Smart redistribution to prioritize content needs
    if (totalWidth > availableWidth) {
        // Need to shrink - start by identifying columns that have extra space
        int excessWidth = totalWidth - availableWidth;
        
        // First, try to take from columns with the most unused space
        // (difference between actual content and current allocation)
        while (excessWidth > 0) {
            int maxExcessIdx = -1;
            int maxExcess = -1;
            
            // Find column with most excess space (current width - actual content width)
            for (int i = 0; i < columnCount; i++) {
                int contentWidth = initialWidths[i];
                int currentWidth = adjustedWidths[i];
                int excessSpace = currentWidth - Math.max(contentWidth, minColWidth);
                
                if (excessSpace > 0 && excessSpace > maxExcess) {
                    maxExcess = excessSpace;
                    maxExcessIdx = i;
                }
            }
            
            // If we found a column with excess, reduce it
            if (maxExcessIdx >= 0) {
                int reduction = Math.min(maxExcess, excessWidth);
                adjustedWidths[maxExcessIdx] -= reduction;
                excessWidth -= reduction;
            } else {
                // No more excess found, need to reduce proportionally
                break;
            }
        }
        
        // If we still have excess width, take proportionally from all columns above minimum
        if (excessWidth > 0) {
            // Calculate how much space is available to take (space above minimum width)
            int shrinkableSpace = totalWidth - minimumPossibleWidth;
            
            // Take from each column proportionally
            for (int i = 0; i < columnCount && excessWidth > 0; i++) {
                int extraSpace = adjustedWidths[i] - minColWidth;
                if (extraSpace > 0) {
                    double proportion = (double) extraSpace / shrinkableSpace;
                    int reduction = (int) Math.ceil(proportion * excessWidth);
                    reduction = Math.min(reduction, extraSpace);
                    reduction = Math.min(reduction, excessWidth);
                    
                    adjustedWidths[i] -= reduction;
                    excessWidth -= reduction;
                }
            }
            
            // Final pass: if we still have excess, take 1 from largest columns
            while (excessWidth > 0) {
                int largestColIdx = 0;
                for (int i = 1; i < columnCount; i++) {
                    if (adjustedWidths[i] > adjustedWidths[largestColIdx]) {
                        largestColIdx = i;
                    }
                }
                
                if (adjustedWidths[largestColIdx] > minColWidth) {
                    adjustedWidths[largestColIdx]--;
                    excessWidth--;
                } else {
                    // Can't reduce further without going below minimum
                    break;
                }
            }
        }
    } else if (totalWidth < availableWidth) {
        // Need to expand - prioritize columns that need it most
        int extraWidth = availableWidth - totalWidth;
        
        // First, calculate how much each column needs based on content
        int[] additionalNeeded = new int[columnCount];
        int totalNeeded = 0;
        
        for (int i = 0; i < columnCount; i++) {
            int contentWidth = initialWidths[i];
            int currentWidth = adjustedWidths[i];
            
            // How much more this column would ideally want (up to max)
            int needed = Math.min(maxColWidth, contentWidth) - currentWidth;
            needed = Math.max(0, needed);  // Can't be negative
            
            additionalNeeded[i] = needed;
            totalNeeded += needed;
        }
        
        // Distribute based on need first
        if (totalNeeded > 0) {
            for (int i = 0; i < columnCount && extraWidth > 0; i++) {
                if (additionalNeeded[i] > 0) {
                    double proportion = (double) additionalNeeded[i] / totalNeeded;
                    int addition = (int) Math.floor(proportion * extraWidth);
                    addition = Math.min(addition, additionalNeeded[i]);
                    addition = Math.min(addition, extraWidth);
                    
                    adjustedWidths[i] += addition;
                    extraWidth -= addition;
                }
            }
        }
        
        // Distribute any remaining space evenly, prioritizing the last columns
        if (extraWidth > 0) {
            // Give extra space to last columns first, as they're most likely to need it
            for (int i = columnCount - 1; i >= 0 && extraWidth > 0; i--) {
                if (adjustedWidths[i] < maxColWidth) {
                    int addition = Math.min(extraWidth, maxColWidth - adjustedWidths[i]);
                    adjustedWidths[i] += addition;
                    extraWidth -= addition;
                }
            }
        }
    }
    
    // Final check to ensure total width is exactly as specified
    int finalWidth = Arrays.stream(adjustedWidths).sum();
    int diff = availableWidth - finalWidth;
    
    if (diff > 0) {
        // Need to add more width - give to the last columns first
        for (int i = columnCount - 1; i >= 0 && diff > 0; i--) {
            if (adjustedWidths[i] < maxColWidth) {
                adjustedWidths[i]++;
                diff--;
            }
        }
    } else if (diff < 0) {
        // Need to remove width - take from first columns that can spare it
        for (int i = 0; i < columnCount && diff < 0; i++) {
            if (adjustedWidths[i] > minColWidth) {
                adjustedWidths[i]--;
                diff++;
            }
        }
    }
    
    return adjustedWidths;
}

/**
 * Generates a table border with exact width of 79 characters
 */
private static String generateExactWidthTableBorder(int[] columnWidths, char leftChar, 
                                                  char middleChar, char rightChar, char horizontalChar) {
    StringBuilder sb = new StringBuilder();
    sb.append(leftChar);
    
    for (int i = 0; i < columnWidths.length; i++) {
        // Add horizontal characters for each column width + 2 spaces
        for (int j = 0; j < columnWidths[i] + 2; j++) {
            sb.append(horizontalChar);
        }
        
        if (i < columnWidths.length - 1) {
            sb.append(middleChar);
        }
    }
    
    sb.append(rightChar);
    
    // Verify we have exactly 79 characters
    if (sb.length() != 79) {
        // Adjust if needed to ensure exactly 79 characters
        if (sb.length() > 79) {
            sb.setLength(79);
            sb.setCharAt(78, rightChar);
        } else {
            while (sb.length() < 78) {
                sb.insert(sb.length() - 1, horizontalChar);
            }
        }
    }
    
    return sb.toString();
}

/**
 * Append a data row to the table with text wrapping and exact width of 79 characters
 * Empty cells will repeat the most recent non-empty text in their column
 */
private static void appendTableRowWithWrapping(StringBuilder sb, List<String> rowData, 
                                             int[] columnWidths, char verticalChar,
                                             List<String> lastNonEmptyValues) {
    // Split cell content into multiple lines if needed
    List<String[]> wrappedRowContent = new ArrayList<>();
    int maxWrappedLines = 1;
    
    for (int i = 0; i < columnWidths.length; i++) {
        String cellValue = i < rowData.size() ? rowData.get(i) : "";
        
        // If cell is empty, use the last non-empty value for this column
        if (cellValue.trim().isEmpty() && i < lastNonEmptyValues.size() && lastNonEmptyValues.get(i) != null) {
            cellValue = lastNonEmptyValues.get(i);
        } else if (!cellValue.trim().isEmpty()) {
            // Update the last non-empty value for this column
            if (i < lastNonEmptyValues.size()) {
                lastNonEmptyValues.set(i, cellValue);
            } else {
                // Add to the list if needed
                lastNonEmptyValues.add(cellValue);
            }
        }
        
        String[] wrappedContent = wrapText(cellValue, columnWidths[i]);
        wrappedRowContent.add(wrappedContent);
        maxWrappedLines = Math.max(maxWrappedLines, wrappedContent.length);
    }
    
    // Output each wrapped line
    for (int lineIdx = 0; lineIdx < maxWrappedLines; lineIdx++) {
        StringBuilder rowBuilder = new StringBuilder();
        rowBuilder.append(verticalChar);
        
        for (int colIdx = 0; colIdx < columnWidths.length; colIdx++) {
            String[] colContent = wrappedRowContent.get(colIdx);
            String line = lineIdx < colContent.length ? colContent[lineIdx] : "";
            
            rowBuilder.append(' ').append(line);
            
            // Pad with spaces to match column width
            int padding = columnWidths[colIdx] - line.length();
            if (padding > 0) {
                rowBuilder.append(" ".repeat(padding));
            }
            
            rowBuilder.append(' ').append(verticalChar);
        }
        
        // Ensure we have exactly 79 characters
        if (rowBuilder.length() != 79) {
            if (rowBuilder.length() > 79) {
                rowBuilder.setLength(79);
                rowBuilder.setCharAt(78, verticalChar);
            } else {
                while (rowBuilder.length() < 79) {
                    rowBuilder.insert(rowBuilder.length() - 1, ' ');
                }
            }
        }
        
        sb.append(rowBuilder).append('\n');
    }
}

/**
 * Append a fixed-width table row (used for header rows)
 */
private static void appendFixedWidthTableRow(StringBuilder sb, List<String> rowData, 
                                            int[] columnWidths, char verticalChar) {
    StringBuilder rowBuilder = new StringBuilder();
    rowBuilder.append(verticalChar);
    
    for (int i = 0; i < columnWidths.length; i++) {
        String cellValue = i < rowData.size() ? rowData.get(i) : "";
        
        // If cell value is too long, truncate it and add ellipsis
        if (cellValue.length() > columnWidths[i]) {
            cellValue = cellValue.substring(0, Math.max(0, columnWidths[i] - 3)) + "...";
        }
        
        rowBuilder.append(' ').append(cellValue);
        
        // Pad with spaces to match column width
        int padding = columnWidths[i] - cellValue.length();
        if (padding > 0) {
            rowBuilder.append(" ".repeat(padding));
        }
        
        rowBuilder.append(' ').append(verticalChar);
    }
    
    // Ensure we have exactly 79 characters
    if (rowBuilder.length() != 79) {
        if (rowBuilder.length() > 79) {
            rowBuilder.setLength(79);
            rowBuilder.setCharAt(78, verticalChar);
        } else {
            while (rowBuilder.length() < 79) {
                rowBuilder.insert(rowBuilder.length() - 1, ' ');
            }
        }
    }
    
    sb.append(rowBuilder).append('\n');
}

/**
 * Wraps text to fit within a specified width
 */
private static String[] wrapText(String text, int width) {
    if (text == null || text.isEmpty()) {
        return new String[]{""}; // Return empty string for empty input
    }
    
    if (text.length() <= width) {
        return new String[]{text}; // No wrapping needed
    }
    
    List<String> lines = new ArrayList<>();
    int start = 0;
    
    while (start < text.length()) {
        int end = Math.min(start + width, text.length());
        
        // Check if we can break at a space
        if (end < text.length()) {
            int spacePos = text.lastIndexOf(' ', end);
            if (spacePos > start) {
                end = spacePos;
            }
        }
        
        lines.add(text.substring(start, end).trim());
        start = end;
    }
    
    return lines.toArray(new String[0]);
}

    /**
     * Detects the separator used in CSV (comma, semicolon, tab)
     */
    private static String detectSeparator(String firstLine) {
        if (firstLine.contains("\t")) {
            return "\t";
        } else if (firstLine.contains(";")) {
            return ";";
        } else {
            return ",";  // Default to comma
        }
    }
    
    /**
     * Parse a CSV line respecting quoted values
     */
    private static List<String> parseCsvLine(String line, String separator) {
        List<String> result = new ArrayList<>();
        
        // Handle quoted values with potential commas inside
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                // Toggle the inQuotes flag
                inQuotes = !inQuotes;
                
                // Add the quote if it's an escaped quote (i.e., "")
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++; // Skip the next quote
                }
            } else if (c == separator.charAt(0) && !inQuotes) {
                // End of field
                result.add(currentField.toString().trim());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        // Add the last field
        result.add(currentField.toString().trim());
        
        return result;
    }
    
    /**
     * Table symbols class to hold different style characters
     */
    private static class TableSymbols {
        char topLeft, topMiddle, topRight;
        char leftMiddle, middle, rightMiddle;
        char bottomLeft, bottomMiddle, bottomRight;
        char horizontal, vertical;
    }
    
    /**
     * Get table symbols based on selected style
     */
    private static TableSymbols getTableSymbols(int tableStyle) {
        TableSymbols symbols = new TableSymbols();
        
        switch (tableStyle) {
            case 1: // Simple style
                symbols.topLeft = '+'; symbols.topMiddle = '+'; symbols.topRight = '+';
                symbols.leftMiddle = '+'; symbols.middle = '+'; symbols.rightMiddle = '+';
                symbols.bottomLeft = '+'; symbols.bottomMiddle = '+'; symbols.bottomRight = '+';
                symbols.horizontal = '-'; symbols.vertical = '|';
                break;
                
            case 2: // Classic style
                symbols.topLeft = '.'; symbols.topMiddle = '.'; symbols.topRight = '.';
                symbols.leftMiddle = ':'; symbols.middle = '+'; symbols.rightMiddle = ':';
                symbols.bottomLeft = '\''; symbols.bottomMiddle = '\''; symbols.bottomRight = '\'';
                symbols.horizontal = '-'; symbols.vertical = '|';
                break;
                
            case 3: // Fancy style
                symbols.topLeft = '╔'; symbols.topMiddle = '╦'; symbols.topRight = '╗';
                symbols.leftMiddle = '╠'; symbols.middle = '╬'; symbols.rightMiddle = '╣';
                symbols.bottomLeft = '╚'; symbols.bottomMiddle = '╩'; symbols.bottomRight = '╝';
                symbols.horizontal = '═'; symbols.vertical = '║';
                break;
                
            default: // Default to simple style
                return getTableSymbols(1);
        }
        
        return symbols;
    }
    
    // Rest of your existing methods remain unchanged
    /**
     * Removes surrounding quotes from a path string if present
     */
    private static String removeQuotes(String path) {
        if (path.startsWith("\"") && path.endsWith("\"")) {
            return path.substring(1, path.length() - 1);
        }
        return path;
    }
    
    private static void processFiles(List<String> inputFiles, String outputFilePath, boolean strictMode, 
                                    boolean profanityFilterMode) throws IOException {
        // Create output stream
        try (FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
            StringBuilder builder = new StringBuilder();
            
            for (String inputFile : inputFiles) {
                System.out.println("Processing file: " + inputFile);
                byte[] fileBytes = Files.readAllBytes(Paths.get(inputFile));
                
                // Process each byte
                for (byte b : fileBytes) {
                    if (strictMode ? isStrictPrintable(b) : isPrintable(b)) {
                        // Only append printable characters
                        builder.append((char) b);
                    } else if (b != 13) { // Skip carriage return (CR) without adding newline
                        // Replace any non-printable with newline
                        builder.append('\n');
                    }
                }
                
                // Add a separator between files
                builder.append('\n');
            }
            
            // Initial conversion to a string
            String content = builder.toString();
            
            // In Strict Mode, apply additional filtering
            if (strictMode) {
                // Split the content into lines for line-by-line processing
                String[] lines = content.split("\n");
                StringBuilder filteredContent = new StringBuilder();
                
                // First pass - mark lines to remove (shorter lines between equal-length lines)
                boolean[] removeLines = new boolean[lines.length];
                
                for (int i = 0; i < lines.length; i++) {
                    // Skip empty lines
                    if (lines[i].trim().isEmpty()) {
                        removeLines[i] = true;
                        continue;
                    }
                    
                    // Skip lines with a single character and a space
                    if (lines[i].trim().length() == 2 && lines[i].trim().matches(".\\s")) {
                        removeLines[i] = true;
                        continue;
                    }
                    
                    // Skip lines with only one character
                    if (lines[i].trim().length() == 1) {
                        removeLines[i] = true;
                        continue;
                    }
                    
                    // Check if a hyphen follows a printed character or if a capital follows a lowercase
                    // Only apply to lines shorter than 6 characters
                    if (lines[i].trim().length() < 6) {
                        boolean shouldSkip = false;
                        
                        for (int j = 1; j < lines[i].length(); j++) {
                            char current = lines[i].charAt(j);
                            char previous = lines[i].charAt(j - 1);
                            
                            // Check for hyphen after a printed character
                            if (current == '-' && previous != ' ' && previous != '\t') {
                                shouldSkip = true;
                                break;
                            }
                            
                            // Check for capital after lowercase
                            if (Character.isLowerCase(previous) && Character.isUpperCase(current)) {
                                shouldSkip = true;
                                break;
                            }
                        }
                        
                        if (shouldSkip) {
                            removeLines[i] = true;
                            continue;
                        }
                    }
                    
                    // Check surrounding lines for equal lengths where current line is shorter
                    // If line above and below are same length but current line is shorter, mark for removal
                    if (i > 0 && i < lines.length - 1) {
                        String prevLine = lines[i - 1].trim();
                        String nextLine = lines[i + 1].trim();
                        String currentLine = lines[i].trim();
                        
                        if (!prevLine.isEmpty() && !nextLine.isEmpty() && 
                            prevLine.length() == nextLine.length() && 
                            currentLine.length() < prevLine.length()) {
                            
                            // Mark shorter line for removal
                            removeLines[i] = true;
                            continue;
                        }
                        
                        // Check for the original case: current line is longer than surrounding equal-length lines
                        if (!prevLine.isEmpty() && !nextLine.isEmpty() && 
                            prevLine.length() == nextLine.length() && 
                            currentLine.length() > prevLine.length() && 
                            currentLine.length() <= prevLine.length() + 3) {
                            
                            // Trim the current line to match the length of surrounding lines
                            lines[i] = currentLine.substring(0, prevLine.length());
                        }
                    }
                }
                
                // Second pass - process and apply other filters (uppercase check)
                for (int i = 0; i < lines.length; i++) {
                    // Skip lines marked for removal
                    if (removeLines[i]) {
                        continue;
                    }
                    
                    String line = lines[i];
                    
                    // Count uppercase letters in the line
                    long uppercaseCount = line.chars().filter(c -> c >= 'A' && c <= 'Z').count();
                    
                    // If multiple uppercase letters, keep only uppercase
                    if (uppercaseCount > 1) {
                        // Check for common prefixes in consecutive uppercase lines
                        String currentUppercaseOnly = line.chars()
                            .filter(c -> c >= 'A' && c <= 'Z')
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                            
                        // Look at surrounding lines to find common prefix
                        if (i > 0 && i < lines.length - 1) {
                            String prevLine = lines[i - 1].trim();
                            String nextLine = lines[i + 1].trim();
                            
                            // Extract uppercase portions of surrounding lines
                            String prevUppercase = prevLine.chars()
                                .filter(c -> c >= 'A' && c <= 'Z')
                                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                .toString();
                                
                            /*String nextUppercase = nextLine.chars()
                                .filter(c -> c >= 'A' && c <= 'Z')
                                .collect(StringBuilder::new, StringBuilder::*/

                            String nextUppercase = nextLine.chars()
                                .filter(c -> c >= 'A' && c <= 'Z')
                                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                .toString();
                                                        // If surrounding lines contain only uppercase letters
                            if (!prevUppercase.isEmpty() && !nextUppercase.isEmpty()) {
                                // Find the minimum length of the patterns
                                int minLength = Math.min(prevUppercase.length(), 
                                                Math.min(currentUppercaseOnly.length(), nextUppercase.length()));
                                
                                // Check if the current line has extra characters at the end
                                if (minLength > 0 && currentUppercaseOnly.length() > minLength) {
                                    // Trim to match the pattern of surrounding lines
                                    currentUppercaseOnly = currentUppercaseOnly.substring(0, minLength);
                                }
                            }
                        }
                        
                        // Only add the line if it's not empty after filtering
                        if (currentUppercaseOnly.length() > 0) {
                            filteredContent.append(currentUppercaseOnly).append('\n');
                        }
                    } else {
                        // Otherwise keep the original line
                        filteredContent.append(line).append('\n');
                    }
                }
                
                content = filteredContent.toString();
            }
            
            // In Profanity Filter Mode, remove lines containing offensive words
            if (profanityFilterMode) {
                // Split the content into lines for line-by-line processing
                String[] lines = content.split("\n");
                StringBuilder filteredContent = new StringBuilder();
                
                // Get a set of offensive words
                Set<String> offensiveWords = getProfanityList();
                System.out.println("Loaded " + offensiveWords.size() + " offensive terms to filter");
                
                List<String> removedLines = new ArrayList<>();
                for (String line : lines) {
                    // Skip empty lines
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    
                    // Check if line contains any offensive word
                    boolean containsOffensiveWord = false;
                    String matchedWord = "";
                    
                    // Prepare the line - convert to lowercase 
                    String lowerLine = line.toLowerCase();
                    
                    // Create enhanced patterns for each word that handle punctuation
                    for (String word : offensiveWords) {
                        // Create patterns that match:
                        // 1. Word with word boundaries
                        // 2. Word followed by common punctuation
                        // 3. Word at the end of the line
                        Pattern pattern = Pattern.compile(
                            "\\b" + Pattern.quote(word) + "\\b|" +               // Standard word boundary
                            "\\b" + Pattern.quote(word) + "[,.!?:;'\")]|" +      // Word followed by punctuation
                            "\\b" + Pattern.quote(word) + "$",                    // Word at end of line
                            Pattern.CASE_INSENSITIVE
                        );
                        
                        Matcher matcher = pattern.matcher(lowerLine);
                        if (matcher.find()) {
                            containsOffensiveWord = true;
                            matchedWord = word;
                            break;
                        }
                    }
                    
                    if (!containsOffensiveWord) {
                        filteredContent.append(line).append('\n');
                    } else {
                        // Store the removed line with the matched word
                        removedLines.add("\"" + line + "\" - matched term: \"" + matchedWord + "\"");
                    }
                }
                
                content = filteredContent.toString();
                
                // Print summary of removed lines
                System.out.println("Removed " + removedLines.size() + " lines containing offensive content:");
                
                // Show all removed lines
                for (String removedLine : removedLines) {
                    System.out.println(" - " + removedLine);
                }
            }
            
            // Remove consecutive newlines/blank lines as a final step (for both modes)
            content = content.replaceAll("(?m)^\\s*$", "").replaceAll("\n{2,}", "\n");
            
            // Write to output file
            outputStream.write(content.getBytes());
            
            System.out.println("Conversion completed successfully!");
            System.out.println("Output file: " + new File(outputFilePath).getAbsolutePath());
        }
    }
    
/**
 * Converts a CSV file to HTML tables, splitting into multiple files if needed
 * - Each output file will have maximum 5000 data rows
 * - Headers are repeated in each file
 * - Files are named based on source file with a sequence number
 */
private static void convertCsvToHtmlTable(String csvFilePath, boolean hasHeader) throws IOException {
    // Read all lines from the CSV file
    List<String> csvLines = Files.readAllLines(Paths.get(csvFilePath));
    
    if (csvLines.isEmpty()) {
        System.out.println("CSV file is empty!");
        return;
    }
    
    // Parse CSV data
    List<List<String>> data = new ArrayList<>();
    String separator = detectSeparator(csvLines.get(0));
    
    for (String line : csvLines) {
        List<String> row = parseCsvLine(line, separator);
        data.add(row);
    }
    
    // Extract header row if exists
    List<String> headerRow = null;
    int dataStartIndex = 0;
    
    if (hasHeader && !data.isEmpty()) {
        headerRow = data.get(0);
        dataStartIndex = 1;
    }
    
    // Prepare base filename without extension
    String baseFileName = new File(csvFilePath).getName();
    if (baseFileName.contains(".")) {
        baseFileName = baseFileName.substring(0, baseFileName.lastIndexOf('.'));
    }
    
    // Set maximum rows per file (5000 data rows, not including header)
    int maxRowsPerFile = 5000;
    
    System.out.println("Maximum rows per file: " + maxRowsPerFile);
    
    // Generate HTML tables with a maximum of 5000 rows per file
    int fileCounter = 1;
    int processedRows = dataStartIndex;
    List<String> generatedFiles = new ArrayList<>();
    
    // Track last non-empty values for each column
    List<String> lastNonEmptyValues = new ArrayList<>();
    if (headerRow != null) {
        // Initialize with header values (or null if empty)
        for (String header : headerRow) {
            lastNonEmptyValues.add(header.trim().isEmpty() ? null : header);
        }
    }
    
    while (processedRows < data.size()) {
        // Calculate end index for this file
        int endIndex = Math.min(processedRows + maxRowsPerFile, data.size());
        
        // Create a list of rows for this file
        List<List<String>> fileData = new ArrayList<>();
        
        // Always add header if it exists
        if (headerRow != null) {
            // Process header: remove special characters
            List<String> processedHeader = new ArrayList<>();
            for (String cell : headerRow) {
                processedHeader.add(processSpecialCharacters(cell));
            }
            fileData.add(processedHeader);
        }
        
        // Reset last non-empty values for data rows if header exists
        if (headerRow != null) {
            lastNonEmptyValues = new ArrayList<>();
            for (String header : headerRow) {
                lastNonEmptyValues.add(header.trim().isEmpty() ? null : header);
            }
        } else if (lastNonEmptyValues.isEmpty()) {
            // Initialize empty values if no header
            for (int i = 0; i < data.get(0).size(); i++) {
                lastNonEmptyValues.add(null);
            }
        }
        
        // Add data rows with processed content and duplicated values for empty cells
        for (int i = processedRows; i < endIndex; i++) {
            List<String> originalRow = data.get(i);
            List<String> processedRow = new ArrayList<>();
            
            // Process each cell, duplicate content for empty cells
            for (int j = 0; j < originalRow.size(); j++) {
                String cellValue = originalRow.get(j);
                
                // Check if cell is empty
                if (cellValue.trim().isEmpty() && j < lastNonEmptyValues.size() && lastNonEmptyValues.get(j) != null) {
                    // Use the last non-empty value for this column
                    cellValue = lastNonEmptyValues.get(j);
                } else if (!cellValue.trim().isEmpty()) {
                    // Update the last non-empty value for this column
                    if (j < lastNonEmptyValues.size()) {
                        lastNonEmptyValues.set(j, cellValue);
                    } else {
                        // Extend the list if needed
                        while (lastNonEmptyValues.size() <= j) {
                            lastNonEmptyValues.add(null);
                        }
                        lastNonEmptyValues.set(j, cellValue);
                    }
                }
                
                // Process special characters in the cell
                processedRow.add(processSpecialCharacters(cellValue));
            }
            
            fileData.add(processedRow);
        }
        
        // Generate output filename
        String outputFileName = baseFileName + "_html_" + fileCounter + ".htm";
        String outputPath = new File(csvFilePath).getParent() + File.separator + outputFileName;
        
        // Generate HTML content
        String htmlContent = generateHtmlTable(fileData, hasHeader);
        
        // Write HTML to file
        Files.write(Paths.get(outputPath), htmlContent.getBytes());
        generatedFiles.add(outputPath);
        
        // Update counters
        processedRows = endIndex;
        fileCounter++;
    }
    
    System.out.println("CSV successfully converted to HTML tables!");
    System.out.println("Generated " + (fileCounter - 1) + " HTML files:");
    for (String file : generatedFiles) {
        System.out.println(" - " + new File(file).getAbsolutePath());
    }
}

/**
 * Process special characters in cell content:
 * - Remove `b and `i
 * - Replace `n with line breaks
 */
private static String processSpecialCharacters(String content) {
    if (content == null) return "";
    
    // Remove `b and `i tags
    String processed = content.replace("`b", "").replace("`i", "");
    
    // Removed 'n too
    processed = processed.replace("`n", " ");
    
    return processed;
}

/**
 * Generates HTML table content from the given data
 */
private static String generateHtmlTable(List<List<String>> data, boolean hasHeader) {
    StringBuilder html = new StringBuilder();
    
    // HTML header (without title)
    html.append("<!DOCTYPE html>\n");
    html.append("<html>\n");
    html.append("<head>\n");
    html.append("  <meta charset=\"UTF-8\">\n");
    html.append("  <style>\n");
    html.append("    table { border-collapse: collapse; width: 100%; }\n");
    html.append("    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
    html.append("    th { background-color: #f2f2f2; }\n");
    html.append("    tr:nth-child(even) { background-color: #f9f9f9; }\n");
    html.append("  </style>\n");
    html.append("</head>\n");
    html.append("<body>\n");
    
    // Table
    html.append("  <table>\n");
    
    // Table header
    if (hasHeader && !data.isEmpty()) {
        html.append("    <thead>\n");
        html.append("      <tr>\n");
        
        for (String header : data.get(0)) {
            html.append("        <th>").append(escapeHtml(header)).append("</th>\n");
        }
        
        html.append("      </tr>\n");
        html.append("    </thead>\n");
    }
    
    // Table body
    html.append("    <tbody>\n");
    
    int startRow = hasHeader ? 1 : 0;
    for (int i = startRow; i < data.size(); i++) {
        html.append("      <tr>\n");
        
        for (String cell : data.get(i)) {
            html.append("        <td>").append(escapeHtml(cell)).append("</td>\n");
        }
        
        html.append("      </tr>\n");
    }
    
    html.append("    </tbody>\n");
    html.append("  </table>\n");
    
    // HTML footer
    html.append("</body>\n");
    html.append("</html>");
    
    return html.toString();
}

/**
 * Escapes HTML special characters in text
 */
private static String escapeHtml(String text) {
    return text.replace("&", "&amp;")
               .replace("<", "&lt;")
               .replace(">", "&gt;")
               .replace("\"", "&quot;")
               .replace("'", "&#39;");
}
/**
 * Estimates the average size of a row in bytes for HTML output
 */
private static int estimateRowSize(List<List<String>> data) {
    if (data.isEmpty() || data.get(0).isEmpty()) {
        return 100; // Default estimation if data is empty
    }
    
    // Sample up to 100 rows to estimate size
    int sampleSize = Math.min(100, data.size());
    int totalSize = 0;
    
    for (int i = 0; i < sampleSize; i++) {
        List<String> row = data.get(i);
        int rowSize = 0;
        
        // Each cell has <td></td> tags (9 chars) plus content
        for (String cell : row) {
            rowSize += 9 + cell.length();
        }
        
        // Add overhead for <tr></tr> tags (9 chars)
        rowSize += 9;
        
        totalSize += rowSize;
    }
    
    // Return average row size plus 20% overhead for HTML formatting
    return (int)(totalSize / sampleSize * 1.2);
}

	
    /**
     * Returns a set of offensive words for filtering
     */
    private static Set<String> getProfanityList() {
        Set<String> profanityList = new HashSet<>();
        
        // Basic profanity - common swear words & body parts 'n shit
        profanityList.add("ass");
        profanityList.add("asshole");
        profanityList.add("bastard");
        profanityList.add("bitch");
        profanityList.add("bullshit");
        profanityList.add("cock");
        profanityList.add("crap");
        profanityList.add("cunt");
        profanityList.add("damn");
        profanityList.add("dick");
        profanityList.add("douche");
        profanityList.add("fag");
        profanityList.add("faggot");
        profanityList.add("niglet");
        profanityList.add("fuck");
        profanityList.add("piss");
        profanityList.add("pussy");
        profanityList.add("shit");
        profanityList.add("slut");
        profanityList.add("whore");
        profanityList.add("boob");
        profanityList.add("titty");
        profanityList.add("titties");
        profanityList.add("boobies");
        profanityList.add("vagina");
        profanityList.add("penis");
        profanityList.add("whore");
        profanityList.add("brian");
        
        // Racial/ethnic slurs
        profanityList.add("nigger");
        profanityList.add("nigga");
        profanityList.add("spic");
        profanityList.add("wetback");
        profanityList.add("kike");
        profanityList.add("chink");
        profanityList.add("gook");
        profanityList.add("paki");
        profanityList.add("raghead");
        profanityList.add("towelhead");
        
        // fun shit
        profanityList.add("retard");
        profanityList.add("spaz");
        profanityList.add("dyke");
        profanityList.add("tranny");
        profanityList.add("homo");
        
        // Common variations/derivatives
        profanityList.add("fucking");
        profanityList.add("fucked");
        profanityList.add("fucker");
        profanityList.add("fucks");
        profanityList.add("motherfucker");
        profanityList.add("motherfucking");
        profanityList.add("shitting");
        profanityList.add("shitted");
        profanityList.add("shitty");
        profanityList.add("shithead");
        profanityList.add("bullshitting");
        profanityList.add("asswipe");
        profanityList.add("asshat");
        profanityList.add("assfuck");
        profanityList.add("assholes");
        profanityList.add("bitches");
        profanityList.add("bitching");
        profanityList.add("bitched");
        profanityList.add("cunts");
        profanityList.add("dumbass");
        profanityList.add("dumbfuck");
        profanityList.add("dickhead");
        profanityList.add("cocksucker");
        
        return profanityList;
    }
    
    /**
     * Determines if a byte represents a printable ASCII character
     */
    private static boolean isPrintable(byte b) {
        // Printable ASCII characters are between 32 (space) and 126 (~)
        // Also include LF (10) as printable since we want to keep explicit newlines
        return (b >= 32 && b <= 126) || b == 10;
    }
    
    /**
     * Strict mode: only allows a-z, A-Z, periods, and hyphens
     */
    private static boolean isStrictPrintable(byte b) {
        // Allow a-z (97-122), A-Z (65-90), space (32), period (46), and hyphen (45)
        // Also include LF (10) as printable since we want to keep explicit newlines
        return ((b >= 'a' && b <= 'z') || 
                (b >= 'A' && b <= 'Z') || 
                b == '.' || 
                b == '-' ||
                b == 32 ||
                b == 10);  // Line feed
    }
}
