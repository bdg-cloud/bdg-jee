package fr.legrain.gestCom.librairiesEcran.swt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;

import fr.legrain.gestCom.librairiesEcran.LibrairiesEcranPlugin;
import fr.legrain.lib.gui.EJBBaseController;

public class BeanValidatorJFaceDatabinding implements IValidator {
	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	
	private Class<Object> c = null;
	private String field = null;
	
	private EJBBaseController controller;
	
	public BeanValidatorJFaceDatabinding(Class c, String field, EJBBaseController controller) {
		this.c = c;
		this.field = field;
		this.controller = controller;
	}
	
	public BeanValidatorJFaceDatabinding(Class c, String field) {
		this(c,field, null);
	}

	@Override
	public IStatus validate(Object value) {
		//Set<ConstraintViolation<Object>> violations = factory.getValidator().validate(value,new Class<?>[] { Default.class });
		Set<ConstraintViolation<Object>> violations = factory.getValidator().validateValue(c,field,value);
		
		if (violations.size() > 0) {
			String messageComplet = "Validation errors";
			List<IStatus> statusList = new ArrayList<IStatus>();
			for (ConstraintViolation<Object> cv : violations) {
				statusList.add(ValidationStatus.error(cv.getMessage()));
				messageComplet+=" "+cv.getMessage()+"\n";
			}

			return new MultiStatus(LibrairiesEcranPlugin.PLUGIN_ID, IStatus.ERROR,
					//statusList.toArray(new IStatus[statusList.size()]), "Validation errors", null);
					statusList.toArray(new IStatus[statusList.size()]), messageComplet, null);
		} else {
			//aucune erreur de validation "automatique", on déclanche les validations du controller
			if(controller!=null) {
				IStatus s = controller.validateUIField(field, value);
				if(!s.isOK()) {
					return s;
				}
			}
		}
		return ValidationStatus.ok();
	}
}

