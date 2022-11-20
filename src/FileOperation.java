import java.io.File;

public class FileOperation {
	@SuppressWarnings("unused")
	public static File OpenFile() {
		
		File file = new File("./Repo 2.gviweb");
		
		if(file == null) {
			System.out.println("Open File failed");
			return null;
		}
		else{
			return file;
		}
	}
}