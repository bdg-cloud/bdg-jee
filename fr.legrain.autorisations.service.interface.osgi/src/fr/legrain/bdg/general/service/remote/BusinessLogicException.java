package fr.legrain.bdg.general.service.remote;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class BusinessLogicException extends Exception {

	private static final long serialVersionUID = 5903517820253312226L;

	public BusinessLogicException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BusinessLogicException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BusinessLogicException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BusinessLogicException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BusinessLogicException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
