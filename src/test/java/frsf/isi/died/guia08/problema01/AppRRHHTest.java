package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.TareaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class AppRRHHTest {

	@Test
	public void asignarTareaTest() {
		AppRRHH app = new AppRRHH();
		Empleado e1 = new Empleado(123, "Juan", Tipo.CONTRATADO, 100.00);
		app.getEmpleados().add(e1);
		
		List<Tarea> lista = new ArrayList<Tarea>();
		Tarea t1 = new Tarea(1,"Tarea1",10);
		lista.add(t1);
		try {
			app.asignarTarea(123,1,"Tarea1",10);
		} catch (TareaException e) {
			System.out.println("No se pudo asignar al empleado 123");
		}
		
		assertFalse(e1.getTareasAsignadas().isEmpty());
	}

	@Test
	public void empezarTareaTest() {
		AppRRHH app = new AppRRHH();
		Empleado e1 = new Empleado(123, "Juan", Tipo.CONTRATADO, 100.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		app.getEmpleados().add(e1);
		e1.getTareasAsignadas().add(t1);
		
		try {
			app.empezarTarea(123, 1);
		} catch (EmpleadoException e) {
			System.out.println("No existe el empleado");
		} catch (TareaException e) {
			System.out.println("La tarea ya tiene un empleado asignado o ya finalizo");
		}
		
		assertTrue(t1.getFechaInicio() != null);
	}
	
	@Test
	public void terminarTareaTest() {
		AppRRHH app = new AppRRHH();
		Empleado e1 = new Empleado(123, "Juan", Tipo.CONTRATADO, 100.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		app.getEmpleados().add(e1);
		e1.getTareasAsignadas().add(t1);
		
		try {
			app.terminarTarea(123, 1);
		} catch (EmpleadoException e) {
			System.out.println("No existe el empleado");
		} catch (TareaException e) {
			System.out.println("La tarea ya tiene un empleado asignado o ya finalizo");
		}
		
		assertTrue(t1.getFechaFin() != null);
	}
}
