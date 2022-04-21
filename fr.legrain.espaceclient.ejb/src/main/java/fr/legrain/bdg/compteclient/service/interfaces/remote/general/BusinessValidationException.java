package fr.legrain.bdg.compteclient.service.interfaces.remote.general;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class BusinessValidationException extends Exception {

	private static final long serialVersionUID = -9181255508732800666L;

	public BusinessValidationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BusinessValidationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BusinessValidationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BusinessValidationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BusinessValidationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
