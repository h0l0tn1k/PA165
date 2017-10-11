package cz.fi.muni.pa165.tasks;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolationException;

import cz.fi.muni.pa165.InMemoryDatabaseSpring;
import cz.fi.muni.pa165.dto.Color;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;

 
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
public class Task02 extends AbstractTestNGSpringContextTests {

	@PersistenceUnit
	private EntityManagerFactory emf;


	@BeforeClass
	public void init() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Category electro = new Category(), kitchen = new Category();
		electro.setName("Electro");
		kitchen.setName("Kitchen");

		Product light = new Product(), robot = new Product(), plate = new Product();

		light.setName("Flashlight");
		//light.setAddedDate();
		light.addCategory(electro);
		light.setColor(Color.BLACK);

		robot.setName("Kitchen robot");
		//robot.setAddedDate();
		robot.addCategory(kitchen);
		robot.addCategory(electro);
		robot.setColor(Color.WHITE);

		plate.setName("Plate");
		//plate.setAddedDate();
		plate.addCategory(kitchen);
		plate.setColor(Color.WHITE);


		em.persist(electro);
		em.persist(kitchen);

		em.persist(light);
		em.persist(robot);
		em.persist(plate);

		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void test()
	{
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		List<Category> categories = em.createQuery("select c from Category c order by c.name", Category.class)
				.getResultList();

		if (categories.size() != 2)
			throw new RuntimeException("Expected two categories!");

		assertContainsCategoryWithName(new HashSet<>(categories),"Electro");
		assertContainsCategoryWithName(new HashSet<>(categories),"Kitchen");

		em.getTransaction().commit();
		em.close();
	}

	private static void assertContainsCategoryWithName(Set<Category> categories,
			String expectedCategoryName) {
		for(Category cat: categories){
			if (cat.getName().equals(expectedCategoryName))
				return;
		}
			
		Assert.fail("Couldn't find category "+ expectedCategoryName+ " in collection "+categories);
	}
	private static void assertContainsProductWithName(Set<Product> products,
			String expectedProductName) {
		
		for(Product prod: products){
			if (prod.getName().equals(expectedProductName))
				return;
		}
			
		Assert.fail("Couldn't find product "+ expectedProductName+ " in collection "+products);
	}

	
}
