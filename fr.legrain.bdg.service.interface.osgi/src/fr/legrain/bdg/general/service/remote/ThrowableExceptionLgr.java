package fr.legrain.bdg.general.service.remote;

import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

public class ThrowableExceptionLgr extends Throwable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static RuntimeException renvoieCauseRuntimeException(RuntimeException exception){
		Throwable cause=exception.getCause();
		while (cause!=null && cause.getCause()!=null) {
				cause=cause.getCause();			
		}
		if(cause!=null && cause instanceof SQLException){
			return new RuntimeException(cause);
		}
//		if(exception.getCause()!=null){
//			if(exception.getCause().getCause()!=null){
//				if(exception.getCause().getCause() instanceof SQLException){
//					return new RuntimeException(exception.getCause().getCause());
//				}
//			}
//		}
		return exception;
	}

	public static Exception renvoieCauseRuntimeException(Exception exception){
		Throwable cause=exception.getCause();
		while (cause!=null && cause.getCause()!=null) {
				cause=cause.getCause();			
		}
		if(cause!=null && cause instanceof SQLException){
			return new Exception(cause);
		}
//		if(exception.getCause()!=null){
//			if(exception.getCause().getCause()!=null){
//				if(exception.getCause().getCause() instanceof SQLException){
//					return new RuntimeException(exception.getCause().getCause());
//				}
//			}
//		}
		return exception;
	}
//	public static BusinessValidationException renvoieMessageException(FinderException exception){
//		if(exception.getCause()!=null){
//				if(exception.getCause() instanceof FinderException){
//					return new BusinessValidationException(exception.getCause().getCause());
//				}
//		}
//		return  new BusinessValidationException(exception) ;
//	}
//	
//	public static BusinessValidationException renvoieMessageException(CreateException exception){
//		if(exception.getCause()!=null){
//				if(exception.getCause() instanceof CreateException){
//					return new BusinessValidationException(exception.getCause().getCause());
//				}
//		}
//		return  new BusinessValidationException(exception) ;
//	}
//	
//	public static BusinessValidationException renvoieMessageException(RemoveException exception){
//		if(exception.getCause()!=null){
//				if(exception.getCause() instanceof RemoveException){
//					return new BusinessValidationException(exception.getCause().getCause());
//				}
//		}
//		return  new BusinessValidationException(exception) ;
//	}
}
