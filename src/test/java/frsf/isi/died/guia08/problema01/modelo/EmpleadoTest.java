package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//import org.junit.Ignore;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;


public class EmpleadoTest {

	// IMPORTANTE
	// ESTA CLASE ESTA ANOTADA COMO @IGNORE por lo que no ejecutará ningun test
	// hasta que no borre esa anotación.
	
	@Test
	public void testSalario() {
		Empleado e1 = new Empleado(123, "Juan", Tipo.CONTRATADO, 100.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		Tarea t2 = new Tarea(2, "Tarea2", 8);
		t1.setFechaInicio(LocalDateTime.now().minusDays(10));
		t2.setFechaInicio(LocalDateTime.now().minusDays(8));
		t1.setFechaFin(LocalDateTime.now());
		t2.setFechaFin(LocalDateTime.now());
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t1);
		tareas.add(t2);
		
		e1.setTareasAsignadas(tareas);
		
		assertTrue(e1.salario().equals(1800.00));
		
	}

	@Test
	public void testCostoTarea() {
		Empleado e1 = new Empleado(123, "Juan", Tipo.EFECTIVO, 100.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		t1.setFechaInicio(LocalDateTime.now().minusDays(10));
		t1.setFechaFin(LocalDateTime.now());
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t1);
		
		e1.setTareasAsignadas(tareas);
		
		assertTrue(e1.costoTarea(t1).equals(1000.00));
	}

	@Test
	public void testAsignarTarea() throws TareaException {
		Empleado e1 = new Empleado(123, "Juan", Tipo.CONTRATADO, 100.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		Tarea t2 = new Tarea(2, "Tarea2", 8);
		Tarea t3 = new Tarea(3, "Tarea3", 10);
		t1.setFechaInicio(LocalDateTime.now().minusDays(10));
		t2.setFechaInicio(LocalDateTime.now().minusDays(8));
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t1);
		tareas.add(t2);
		e1.setTareasAsignadas(tareas);
		tareas.add(t3);

		e1.asignarTarea(t3);
		
		assertEquals(tareas, e1.getTareasAsignadas());
	}

	@Test
	public void testNoAsignarTarea() {
		Empleado e1 = new Empleado(123, "Juan", Tipo.CONTRATADO, 100.00);
		Empleado e2 = new Empleado(789, "Pedro", Tipo.EFECTIVO, 130.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		Tarea t2 = new Tarea(2, "Tarea2", 8);
		Tarea t3 = new Tarea(3, "Tarea3", 10);
		t1.setFechaInicio(LocalDateTime.now().minusDays(10));
		t2.setFechaInicio(LocalDateTime.now().minusDays(8));
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t1);
		tareas.add(t2);
		e1.setTareasAsignadas(tareas);
		t3.setEmpleadoAsignado(e2);

		try {
			e1.asignarTarea(t3);
		} catch (TareaException e) {
			System.out.println("No se pudo asignar la tarea 3");
		}
		
		assertEquals(tareas, e1.getTareasAsignadas());
	}
	
	@Test
	public void testComenzarInteger() throws TareaException {
		Empleado e1 = new Empleado(123, "Juan", Tipo.EFECTIVO, 100.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t1);
		e1.setTareasAsignadas(tareas);
		
		e1.comenzar(1);
		
		assertTrue(t1.getFechaInicio() != null);
		
	}

	@Test
	public void testFinalizarInteger() throws TareaException {
		Empleado e1 = new Empleado(123, "Juan", Tipo.CONTRATADO, 100.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		t1.setFechaInicio(LocalDateTime.now().minusDays(10));
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t1);
		e1.setTareasAsignadas(tareas);
		
		e1.finalizar(1);
		
		assertTrue(t1.getFechaFin() != null);
	}

	@Test
	public void testComenzarIntegerString() throws TareaException {
		Empleado e1 = new Empleado(123, "Juan", Tipo.CONTRATADO, 100.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t1);
		e1.setTareasAsignadas(tareas);
		
		e1.comenzar(1, "28-05-2020 08:30");
		
		assertTrue(t1.getFechaInicio().equals(LocalDateTime.of(2020, 05, 28, 8, 30)));
	}

	@Test
	public void testFinalizarIntegerString() throws TareaException {
		Empleado e1 = new Empleado(123, "Juan", Tipo.CONTRATADO, 100.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		t1.setFechaInicio(LocalDateTime.now().minusDays(10));
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t1);
		e1.setTareasAsignadas(tareas);
		
		e1.finalizar(1, "28-05-2020 12:54");
		
		assertTrue(t1.getFechaFin().equals(LocalDateTime.of(2020, 05, 28, 12, 54)));
	}

}
