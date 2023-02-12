package calendar;

import models.User;
import persistence.HibernateUtil;

public class Program {

	public static void main(String[] args) {
		System.out.println("Calendar App is running...");
		var sessionFactory = HibernateUtil.getSessionFactory();
		var session = sessionFactory.getCurrentSession();
		System.out.println("Session created...");
		System.out.println("Begin transaction...");	    
		var tx = session.beginTransaction();
		System.out.println("Start feeding data...");
		User user = new User("Dario Mariani");
		System.out.println("Persisting...");
		session.persist(user);
			    
		tx.commit();
		System.out.println("User ID=" + user.getId());
	}
	

}
