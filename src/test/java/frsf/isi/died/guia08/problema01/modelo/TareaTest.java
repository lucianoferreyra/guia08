package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class TareaTest {

	@Test
	public void asignarEmpleadoTestAgrega() throws TareaException {
		Empleado e1 = new Empleado(123, "Juan", Tipo.CONTRATADO, 100.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		
		t1.asignarEmpleado(e1);
		
		assertTrue(t1.getEmpleadoAsignado() == e1);
	}

	@Test
	public void asignarEmpleadoTestNoAgrega() {
		Empleado e1 = new Empleado(123, "Juan", Tipo.CONTRATADO, 100.00);
		Empleado e2 = new Empleado(789, "Pedro", Tipo.EFECTIVO, 130.00);
		Tarea t1 = new Tarea(1, "Tarea1", 10);
		
		
		try {
			t1.asignarEmpleado(e1);
		} catch (TareaException e) {
			System.out.println("No se pudo asignar el empelado 1");
		}
		try {
			t1.asignarEmpleado(e2);
		} catch (TareaException e) {
			System.out.println("No se pudo asignar el empelado 2");

		}
		
		assertTrue(t1.getEmpleadoAsignado() == e1);
	}
}
