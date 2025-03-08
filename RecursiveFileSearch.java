import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

// Base class (file system node)
abstract class FileSystemNode {
    protected Path path;
    
    public FileSystemNode(Path path) {
        this.path = path; }
    
    public Path getPath() {
        return path; }
    
    public abstract void process();
}

// Subclass for files
class FileNode extends FileSystemNode {
    public FileNode(Path path) {
        super(path);
    }
    
    @Override
    public void process() {
        System.out.println("File found: " + path);
    }
}

// Subclass for folders
class FolderNode extends FileSystemNode {
    public FolderNode(Path path) {
        super(path);
    }
    
    @Override
    public void process() {
        System.out.println("Entering Folder: " + path);
    }
}



public class RecursiveFileSearch {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String folderPath;
        File folder;
        
        
        // Validate the folder path 
        while (true) {
            System.out.print("\nEnter the starting folder path: ");
            folderPath = scan.nextLine();
            folder = new File(folderPath);
            if (folder.exists() && folder.isDirectory()) {
                break;
            } else {
                System.out.println("Invalid folder path. Please enter a valid directory.");
            }
        }
        
        System.out.print("Enter the file extension to search for (e.g., .java): ");
        String fileExtension = scan.nextLine();
        System.out.println("\nSearching...");
        

        // Start search and write results to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("search_results.txt"))) {
            if (searchFiles(folder, fileExtension, writer) == 0) {
                System.out.println("No files found with the given extension.");
            } else {
                System.out.println("\nSearch completed. Results saved to search_results.txt.");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }



    // Recursive search function
    public static int searchFiles(File folder, String extension, BufferedWriter writer) throws IOException {
        int count = 0;
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files == null) return count; 
    
            for (File file : files) {
                if (file.isDirectory()) {
                    count += searchFiles(file, extension, writer); // Recurse into subfolders
               
                } else if (file.getName().toLowerCase().endsWith(extension.toLowerCase())) { 
                    FileNode fileNode = new FileNode(file.toPath());
                    fileNode.process(); // Process file node
                    writer.write(file.getAbsolutePath()); // Write to file
                    writer.newLine(); 
                    count++;
                }
            }
        }
        return count;
    }
}

