package model.dao.impl.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBCTest {
	@InjectMocks
	SellerDao sellerdao = DaoFactory.createSellerDao();
	DepartmentDao depdao = DaoFactory.createDepartmentDao();
	@Mock
	Seller seller;		
	
	@Before
	public void cenario() {
		initMocks(this);
	}
	
	@Test(expected = db.DbException.class)
	public void deletandoById() {
		//cenario
		seller.setId(100);
		
		//acao
		sellerdao.deleteById(100);
		
		//verificacao
		sellerdao.findById(100);
	}
	
	@Test
	public void encontrandoById() {
		//acao
		Seller sel = sellerdao.findById(1);
		
		//verificacao
		assertThat(sel.getName(), containsString("Marta Wayne"));
	}
	
	@Test
	public void encontrandoByDep() {
		//cenario
			Department dep = depdao.findById(3);
		//acao
		List<Seller> sel = sellerdao.findByDepartment(dep);
		
		//verificacao
		for (Seller seller : sel) {
			assertThat(seller.getId(), is(5));
		}
	}
	
	@Test
	public void encontraTodos() {
		//acao
		List<Seller> sel = sellerdao.findAll();
		
		//verificacao
		Assert.assertNotNull(sel);
	}
	
	@Test
	public void updateSeller() {
		//cenario
		Seller sel = sellerdao.findById(2);
		sel.setName("Mellony Gloria");
		sel.setEmail("mellony@gmail.com");
		
		//acao
		sellerdao.update(sel);
		
		//verifcacao
		Assert.assertThat(sel.getEmail(), CoreMatchers.containsString("mellony@gmail.com"));
		Assert.assertThat(sel.getName(), CoreMatchers.containsString("Mellony Gloria"));
	}
	
	@Test
	public void insertSeller() {
		//cenario
		Department dep = depdao.findById(1);
		Mockito.when(seller.getName()).thenReturn("Joelma");
		Mockito.when(seller.getEmail()).thenReturn("jojo@gmail.com");
		Mockito.when(seller.getBirthDate()).thenReturn(new Date());
		Mockito.when(seller.getBaseSalary()).thenReturn(3200.0);
		Mockito.when(seller.getDepartment()).thenReturn(dep);
		
		//acao
		sellerdao.insert(seller);
		
		//verificacao
		Assert.assertThat(seller.getName(), CoreMatchers.is("Joelma"));
		
	}
}
