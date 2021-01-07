package model.dao.impl.test;


import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBCTest {
	@InjectMocks
	DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
	@Mock
	Department dep;
	
	@Before
	public void cenario() {
		initMocks(this);
	}
	
	@Test
	public void inserindoDepartment() {
		//cenario
		Mockito.when(dep.getName()).thenReturn("Aprendizagem");
		
		//acao
		departmentDao.insert(dep);
		
		//verificacao
		assertThat(dep.getName(),is("Aprendizagem"));
	}

	@Test
	public void updateDepartment() {
		//cenario
		Department obj = new Department(null, "abc");
		departmentDao.insert(obj);
		obj.setName("Learning");
		
		//acao
		departmentDao.update(obj);
		
		//verificacao
		assertEquals("Learning",obj.getName());	
	}
	
	@Test
	public void encontraPorId() {
		//acao
		Department obj = departmentDao.findById(1);
		
		//verificacao
		assertEquals("Computers",obj.getName());
	}
	
	@Test(expected = db.DbException.class)
	public void naoEncontraIdInvalido() {
		//cenario
		when(departmentDao.findById(-1)).thenThrow(new Exception("Esse id é invalido"));
		departmentDao.findById(-1);
	}

	@Test
	public void findAll() {
		//acao
		List<Department> lista = departmentDao.findAll();
		
		//verificacao
		assertNotNull(lista);
	}
	
	@Test(expected = db.DbException.class)
	public void deletarPorId() {
		//cenario
		dep.setId(100);
		departmentDao.update(dep);
		
		//acao
		departmentDao.deleteById(100);
		departmentDao.findById(100);		
	}
}
