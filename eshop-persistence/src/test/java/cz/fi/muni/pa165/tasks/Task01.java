package cz.fi.muni.pa165.tasks;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;

import java.util.List;

@ContextConfiguration(classes=PersistenceSampleApplicationContext.class)
public class Task01 extends AbstractTestNGSpringContextTests {

	
	@PersistenceUnit
	private EntityManagerFactory emf;


	@Test
	public void categoryTest() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Category cat = new Category();
		cat.setName("Test");
		em.persist(cat);
		em.getTransaction().commit();
		em.close();


		//TODO under this line: create a second entity manager in categoryTest, use find method to find the category and assert its name.

		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();

		Category category = entityManager.find(Category.class, cat.getId());

		if (category == null)
			throw new RuntimeException("No category present in database!");

		assertEq(category.getName(), "Test");

		entityManager.getTransaction().commit();
		entityManager.close();

		System.out.println("Successfully found category Test!");

	}

	private static void assertEq(Object obj1, Object obj2) {
		if (!obj1.equals(obj2)) {
			throw new RuntimeException(
					"Expected these two objects to be identical: " + obj1
							+ ", " + obj2);
		} else {
			System.out.println("OK objects are identical");
		}
	}

}
