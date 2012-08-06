package com.sopovs.moradanen.hb;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FetchTest {
	private SessionFactory sessionFactory;

	@Before
	public void setUp() throws Exception {
		// A SessionFactory is set up once for an application
		sessionFactory = new Configuration()
				.configure() // configures settings from hibernate.cfg.xml
				.buildSessionFactory();
	}

	@After
	public void tearDown() throws Exception {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

	@Test
	public void testFetchExisting() {
		{

			Session session = sessionFactory.openSession();
			session.beginTransaction();

			Parent parent = new Parent();
			parent.setName("Parent");

			Child child = new Child();
			child.setId(1L);
			child.setName("Child");
			child.setParent(parent);

			parent.setChildren(Arrays.asList(child));

			session.save(parent);

			Assert.assertNotNull(parent.getId());
			Assert.assertNotNull(child.getId());

			session.getTransaction().commit();
			session.close();
		}
		{
			Session session = sessionFactory.openSession();
			@SuppressWarnings("unused")
			List<Parent> parents = session.createCriteria(Parent.class)
					.setFetchMode("children", FetchMode.JOIN)
					.list();
			//here we will fail with StackOverflow
			session.close();
//			for (Parent parent : parents) {
//				System.out.println(parent.getName());
//				for (Child child : parent.getChildren()) {
//					System.out.printf("%s(%d) -> %s(%d)", parent.getName(), parent.getId(), child.getName(),
//							child.getId());
//					System.out.println();
////					System.out.println(parent.getName() + " -> " + child.getName());
//				}
//			}
		}

	}

}
