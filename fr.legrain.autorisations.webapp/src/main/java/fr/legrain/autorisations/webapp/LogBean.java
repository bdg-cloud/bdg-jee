package fr.legrain.autorisations.webapp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

@ManagedBean
@ViewScoped
public class LogBean implements Serializable {

	private static final long serialVersionUID = 5060395909755114386L;

	private File logFile;

	@PostConstruct
	public void init() {
		File logDir = new File(System.getProperty("jboss.server.log.dir"));
		logDir.list();

		logFile = new File(System.getProperty("jboss.server.log.dir")+"/server.log");
	}

	String data;

	String id;

	private int offset = 0;
	private int pageSize = 30;
	// Path for Glassfish directory with log files
	//	String path = "/opt/glassfish3/glassfish/domains/domain1/logs/" + this.id;

	// Constructor
	//	public GlassfishLogFile() {
	//		// Get the ID value
	//		try {
	//			this.id = FacesContext.getCurrentInstance().getExternalContext()
	//					.getRequestParameterMap().get("id");
	//			// We load the first page initially!
	//			this.actionNextPage();
	//		} catch (Exception e) {
	//			this.id = null;
	//		}
	//	}

	public String actionClearOffset() {
		this.offset = 0;
		this.actionNextPage();
		return "SUCCESS";
	}

	public String actionNextPage() {
		StringBuilder page = new StringBuilder();

		for (int i = 0; i < this.pageSize; i++) {
			String line = this.readLine(this.offset);
			if (line == null) {
				break;
			}
			System.out.println(this.offset);
			this.offset += line.length() + 2;
			page.append(line).append(System.getProperty("line.separator"));
		}
		this.data = page.toString();
		return "SUCCESS";
	}

	public void actionAll(ActionEvent actionEvent) {
		data = "";
		byte[] encoded;
		try {
			
			System.err.println(logFile.getAbsolutePath().toString());
			encoded = Files.readAllBytes(Paths.get(logFile.getAbsolutePath()));
			data = new String(encoded, "UTF-8");
			
//			BufferedReader br = Files.newBufferedReader(Paths.get(logFile.getAbsolutePath()), StandardCharsets.UTF_8);
//			    for (String line = null; (line = br.readLine()) != null;) {
//			    	data += line;
//			    }
			
//	         InputStream is = new FileInputStream(logFile);
//	         InputStreamReader instrm = new InputStreamReader(is);
//	         BufferedReader br = new BufferedReader(instrm);
//	         String strLine;
//	         while((strLine = br.readLine()) != null) {    
//	            data += strLine;
//	         }
//	         br.close();


//			LineIterator it = FileUtils.lineIterator(logFile, "UTF-8");
//			try {
//			    while (it.hasNext()) {
//			        String line = it.nextLine();
//			        data += line;
//			        // do something with line
//			    }
//			} finally {
//			    LineIterator.closeQuietly(it);
//			}
			
//			RandomAccessFile aFile = new RandomAccessFile(logFile, "r");
//	        FileChannel inChannel = aFile.getChannel();
//	        ByteBuffer buffer = ByteBuffer.allocate(1024);
//	        while(inChannel.read(buffer) > 0){
//	            buffer.flip();
//	            for (int i = 0; i < buffer.limit(); i++) {
//	                System.out.print((char) buffer.get());
//	            }
//	            buffer.clear(); // do something with the data and clear/compact it.
//	        }
//	        inChannel.close();
//	        aFile.close();
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public String getData() {
		return this.data;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public String readLine(long offset) {
		String strLine = null;
		DataInputStream in = null;
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(logFile);
			// Get the object of DataInputStream
			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			br.skip(offset);

			strLine = br.readLine();
			// Close the input stream
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return strLine;
	}

	public void setData(String data) {

	}
}
