package frsf.isi.died.guia08.problema01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.TareaException;

public class AppRRHH {

	private List<Empleado> empleados;
	
	public AppRRHH() {
		empleados = new ArrayList<Empleado>();
	}
	
	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {
		// crear un empleado
		// agregarlo a la lista
		Empleado e = new Empleado(cuil, nombre, Tipo.CONTRATADO, costoHora);
		empleados.add(e);
	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		// crear un empleado
		// agregarlo a la lista	
		Empleado e = new Empleado(cuil, nombre, Tipo.EFECTIVO, costoHora);
		empleados.add(e);
	}
	
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) throws TareaException {
		// crear un empleado
		// con el método buscarEmpleado() de esta clase
		// agregarlo a la lista	
		Tarea t = new Tarea(idTarea, descripcion, duracionEstimada);
		buscarEmpleado(e -> e.getCuil().equals(cuil))
							 .get()
							 .asignarTarea(t);
		
	}
	
	public void empezarTarea(Integer cuil,Integer idTarea) throws EmpleadoException, TareaException{
		// busca el empleado por cuil en la lista de empleados
		// con el método buscarEmpleado() actual de esta clase
		// e invoca al método comenzar tarea
		buscarEmpleado(e -> e.getCuil()
							 .equals(cuil))
							 .orElseThrow(() -> new EmpleadoException("No existe el empleado."))
							 .comenzar(idTarea);
	}
	
	public void terminarTarea(Integer cuil,Integer idTarea) throws TareaException, EmpleadoException {
		// crear un empleado
		// agregarlo a la lista		
		buscarEmpleado(e -> e.getCuil()
							 .equals(cuil))
							 .orElseThrow(() -> new EmpleadoException("No existe el empleado."))
							 .finalizar(idTarea);
	}

	public void cargarEmpleadosContratadosCSV(String nombreArchivo) throws IOException, FileNotFoundException {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
		FileInputStream archivo;
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader entrada = new BufferedReader(fileReader)){
				String linea = null;
				
				while((linea = entrada.readLine()) != null) {
					String fila[] = linea.split(";");
					Empleado e1 = new Empleado();
					e1.setTipo(Tipo.CONTRATADO);
					e1.setCuil(Integer.valueOf(fila[0]));
					e1.setNombre(fila[1]);
					e1.setCostoHora(Double.valueOf(fila[2]));
					this.empleados.add(e1);
				}
			}
		}
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
		FileInputStream archivo;
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader entrada = new BufferedReader(fileReader)){
				String linea = null;
				
				while((linea = entrada.readLine()) != null) {
					String fila[] = linea.split(";");
					Empleado e1 = new Empleado();
					e1.setTipo(Tipo.EFECTIVO);
					e1.setCuil(Integer.valueOf(fila[0]));
					e1.setNombre(fila[1]);
					e1.setCostoHora(Double.valueOf(fila[2]));
					this.empleados.add(e1);
				}
			}
		}
	}

	public void cargarTareasCSV(String nombreArchivo) throws FileNotFoundException, IOException, TareaException {
		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
		FileInputStream archivo;
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader entrada = new BufferedReader(fileReader)){
				String linea = null;
				
				while((linea = entrada.readLine()) != null) {
					String fila[] = linea.split(";");
					this.asignarTarea(Integer.valueOf(fila[3]), Integer.valueOf(fila[0]), fila[1], Integer.valueOf(fila[2]));
				}
			}
		}
	}
	
	private void guardarTareasTerminadasCSV() throws IOException{
		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV
		List<Tarea> tareasTerminadas = new ArrayList<Tarea>();
		for(Empleado e : this.empleados) {
			for(Tarea t : e.getTareasAsignadas()) {
				if(t.getFechaFin() != null && t.getFacturada() == false) {
					tareasTerminadas.add(t);
				}
			}
		}
		
		try(FileWriter fileWriter = new FileWriter("Tareas terminadas sin facturar.csv", true)) {
			try(BufferedWriter salida = new BufferedWriter(fileWriter)){
				for(Tarea tarea : tareasTerminadas) {
					salida.write(tarea.asCsv()+ System.getProperty("line.separator"));
				}
			}
		}
	}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() throws IOException {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}

	public List<Empleado> getEmpleados() {
		return empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}
	
	
}
