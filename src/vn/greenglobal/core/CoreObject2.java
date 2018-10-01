package vn.greenglobal.core;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Transient;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.WebApps;

interface ModelIntf2 {
	Object getId(); // Hàm dựng sẵn Object và đặt tên hàm getId();

	Boolean noId();
}

public class CoreObject2<T> implements ApplicationContextAware, ModelIntf2 {

	private static ApplicationContext appContext;

	@Override
	@Transient
	public Object getId() { // Nó sẽ tự động override lại các hàm của interface trên
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean noId() {
		// TODO Auto-generated method stub
		// return tru or false
		return (getId() == null) || getId().equals(1);
		// Nếu getId == null thì return true hoặc getId() == 1 =>true => Chưa có id nào
		// trc đó
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		if (appContext == null) {
			appContext = applicationContext;
		}

	}

	public ApplicationContext ctx() {
		if (appContext == null) {
			appContext = WebApplicationContextUtils.getWebApplicationContext(WebApps.getCurrent().getServletContext());
		}
		return appContext;
	}

	public final PlatformTransactionManager transactionManager() {
		return ctx().getBean(PlatformTransactionManager.class);
	}

	public final TransactionTemplate transactioner() {
		return new TransactionTemplate(transactionManager());
	}

	public EntityManager em() {
		return ctx().getBean(EntityManager.class);
	}

	public void saveModel(ModelIntf2 obj) { // Tạo đối tượng interface trên

		if(obj.getClass().isAnnotationPresent(Entity.class)) {
			transactioner().execute(new TransactionCallbackWithoutResult() {
				
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					// TODO Auto-generated method stub
					if(obj.noId()) {
						em().persist(obj);
					}
					else {
						em().merge(obj);
					}
					
				}
			});
		}
		
	}

	public void doSave() {
		saveModel(this);
	}

	public static ApplicationContext getAppContext() {
		return appContext;
	}

	public static void setAppContext(ApplicationContext appContext) {
		CoreObject2.appContext = appContext;
	}

}
