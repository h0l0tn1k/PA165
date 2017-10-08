package cz.fi.muni.pa165;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import cz.fi.muni.pa165.entity.Color;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;

public class MainJavaSe {
	private static EntityManagerFactory emf;

	public static void main(String[] args) throws SQLException, ParseException {
		// The following line is here just to start up a in-memory database
		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(InMemoryDatabaseSpring.class);

		emf = Persistence.createEntityManagerFactory("default");
		try {
			// BEGIN YOUR CODE
			task08();
			// END YOUR CODE
		}
		finally {
			emf.close();
			appContext.close();
		}
	}

	private static void task04() {
		EntityManager entityManager = emf.createEntityManager();
		Category oCategoryElectronics = new Category();
		Category oCategoryMusical = new Category();

		oCategoryElectronics.setName("Electronics");
		oCategoryMusical.setName("Musical");

		entityManager.getTransaction().begin();

		entityManager.persist(oCategoryElectronics);
		entityManager.persist(oCategoryMusical);

		entityManager.getTransaction().commit();
		entityManager.close();

		// The code below is just testing code. Do not modify it
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		List<Category> categories = em.createQuery(
				"select c from Category c order by c.name", Category.class)
				.getResultList();

                if (categories.size() != 2) 
                    throw new RuntimeException("Expected two categories!");

		assertEq(categories.get(0).getName(), "Electronics");
		assertEq(categories.get(1).getName(), "Musical");

		em.getTransaction().commit();
		em.close();

		System.out.println("Succesfully found Electronics and Musical!");
	}

	private static void task05() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Category category = new Category();
		category.setName("Electronics");
		em.persist(category);
		em.getTransaction().commit();
		em.close();

		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();

		Category oCategory = entityManager.find(Category.class, category.getId());
		oCategory.setName("Electro");
		entityManager.merge(oCategory);
		entityManager.getTransaction().commit();
		entityManager.close();

		// The code below is just testing code. Do not modify it
		EntityManager checkingEm = emf.createEntityManager();
		checkingEm.getTransaction().begin();
		Category cat = checkingEm.find(Category.class, category.getId());
		assertEq(cat.getName(), "Electro");
		System.out.println("Name changed successfully to Electro");
		checkingEm.getTransaction().commit();
		checkingEm.close();
	}

	private static void task06() throws ParseException {
		Product oGuitar = new Product();
		oGuitar.setName("Guitar");
		oGuitar.setColor(Color.BLACK);

		Calendar oCalendar = Calendar.getInstance();
		SimpleDateFormat oSdf = new SimpleDateFormat("dd-MM-yyy");
		oCalendar.setTime(oSdf.parse("20-01-2011"));
		oGuitar.setAddedDate(oCalendar.getTime());

		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.persist(oGuitar);
		entityManager.getTransaction().commit();
		entityManager.close();

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Product p = em.createQuery("select p from Product p", Product.class)
				.getSingleResult();
		em.getTransaction().commit();
		em.close();
	 
		assertEq(p.getName(), "Guitar");
		Calendar cal = Calendar.getInstance();
		cal.setTime(p.getAddedDate());
		assertEq(cal.get(Calendar.YEAR), 2011);
		assertEq(cal.get(Calendar.MONTH), 0);
		assertEq(cal.get(Calendar.DAY_OF_MONTH), 20);
		assertEq(cal.get(Calendar.MINUTE), 0);
		assertEq(p.getColor(), Color.BLACK);
		System.out
				.println("Found Guitar with correct values. Starting uniqueness test.");

		em = emf.createEntityManager();
		em.getTransaction().begin();
		Product p2 = new Product();
		p2.setName("Guitar");
		Product p3 = new Product();
		p3.setName("Violin");
		em.persist(p3);
		System.out.println("Successfully persited Violin");
		try {
			em.persist(p2);
			
			throw new RuntimeException(
					"Successfully saved new Product with the same name (Guitar) it should be unique!");
		} catch (PersistenceException ex) {
			System.out
					.println("Unsuccessfully saved second object with name Guitar -> OK");
		}
		em.close();
	

		System.out.println("Task6 ok!");
	}
	
	private static void task08() {
		System.out.println("Running TASK 08");
		
		class MockProduct extends Product {
			private boolean getNameCalled = false;
			@Override
			public String getName() {
				getNameCalled = true;
				return super.getName();
			}
		}
		
		Product p = new Product();
		p.setName("X");
		p.setId(2l);
		Product p2 = new Product();
		p2.setName("X");
		p2.setId(4l);
		MockProduct mp = new MockProduct();
		mp.setName("X");
		p.setId(3l);
		
		System.out.println("Your equals and hashcode should work on unique 'name' attribute");
		if (p.equals(p2) && p.hashCode()==p2.hashCode()){
			System.out.println("CORRECT");
		} else System.out.println("INCORRECT!");
		
		
		System.out.println("Your equals should use instanceof and not getClass()==");
		if (p.equals(mp)){
			System.out.println("CORRECT");
		} else
			System.out.println("INCORRECT!");

		System.out.println("Your equals should call getter to get 'name' value on the other object, because other object may be a proxy class instance");
		if (mp.getNameCalled){
			System.out.println("CORRECT");
		} else System.out.println("INCORRECT!");
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
