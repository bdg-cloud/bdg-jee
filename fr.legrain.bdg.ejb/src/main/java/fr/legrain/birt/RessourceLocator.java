package fr.legrain.birt;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Collections;
import java.util.Map;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;

public class RessourceLocator implements IResourceLocator {

	public RessourceLocator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public URL findResource(ModuleHandle module, String filename, int type) {
		return findResource(module, filename, type, Collections.emptyMap());

	}

	@Override
	public URL findResource(final ModuleHandle moduleHandle, final String fileName, final int type, @SuppressWarnings("rawtypes") final Map appContext) {
		try {
			// The actual URL is not important, it just needs to be valid.
			// We will later use the parameters to this method to resolve the InputStream.
			return new URL(null, "birtres://"+fileName, new URLStreamHandler() {
				
				@Override
				protected URLConnection openConnection(URL url) throws IOException {
					return new URLConnection(url) {

						@Override
						public InputStream getInputStream() throws IOException {
							// TODO Return your stream here.
							// Do something with fileName for example.
							System.out.println("getInputStream called. fileName is: " + fileName);
							InputStream stream = this.getClass().getResourceAsStream("/gizmo/resources/"+fileName);
							return stream;
						}

						@Override
						public void connect() throws IOException {
							// TODO Auto-generated method stub
						}
					};
				}
			});
		} catch (MalformedURLException e) {
			// Since it is our own URL, this should not happen.
			throw new IllegalStateException("Hardcoded URL malformed. Please revisit this class.");
		}

	}

}