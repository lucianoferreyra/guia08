package frsf.isi.died.guia08.problema01.modelo;

//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Empleado {

	public enum Tipo {CONTRATADO,EFECTIVO}; 
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	
	private Function<Tarea, Double> calculoPagoPorTarea;		
	private Predicate<Tarea> puedeAsignarTarea;
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	
	public Empleado() {
		super();
	}
	
	public Empleado(Integer cuil, String nombre, Tipo tipo, Double costoHora) {
		super();
		this.cuil = cuil;
		this.nombre = nombre;
		this.tipo = tipo;
		this.costoHora = costoHora;
		this.tareasAsignadas = new ArrayList<Tarea>();
		this.puedeAsignarTarea = (t) -> t.getEmpleadoAsignado() == null || t.getFechaFin() != null;
		
		switch(tipo) {
		case EFECTIVO:
	
			calculoPagoPorTarea = (t) -> {	if(this.finalizoAntes(t)) {
												return this.costoHora*t.getDuracionEstimada()*1.20;
											} else {
												return this.costoHora*t.getDuracionEstimada();
											}
										}; 
		
		case CONTRATADO:
			
			calculoPagoPorTarea = (t) -> {	if(this.finalizoAntes(t)) {
												return this.costoHora*t.getDuracionEstimada()*1.30;
											} else if(this.diferenciaDias(t)>2){
												return this.costoHora*t.getDuracionEstimada()*0.75;
											} else {
												return this.costoHora*t.getDuracionEstimada();
											}
										};
		}
	}

	
	public Double salario(){
		// cargar todas las tareas no facturadas
		// calcular el costo
		// marcarlas como facturadas.
		
		Double aux = 0.0;
		for(Tarea t : this.tareasAsignadas) {
			if(t.getFacturada() == false) {
				aux += this.costoTarea(t);
			}
		}		
		
		return aux;
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t){
		if(t.getFechaFin() != null) {
			return this.calculoPagoPorTarea.apply(t);
		} else {
			return this.costoHora*t.getDuracionEstimada();
		}
	}
		
	public Boolean asignarTarea(Tarea t) throws TareaException {
		if(puedeAsignarTarea.test(t)) {
		
			if(tipo == Tipo.CONTRATADO) {
				if(this.getTareasPendientes()<5) {
					this.tareasAsignadas.add(t);
					return true;
				} else {
					return false;
				}
			}else {
				if(this.getHorasTareas() < 15 && (this.getHorasTareas()+t.getDuracionEstimada()) <= 15) {
					this.tareasAsignadas.add(t);
					return true;
				} else {
					return false;
				}
			}
		} else {
			throw new TareaException("La tarea que se quiere asignar no es correcata. Seleccione otra.");
		}
	}
	
	public Integer getTareasPendientes() {
		Integer pendientes = 0;
		
		for(Tarea tar : tareasAsignadas) {
			if(tar.getFechaFin() != null) {
				pendientes++;
			}
		}
		
		return pendientes;
	}
	
	public Integer getHorasTareas() {
		Integer horas = 0;
		for(Tarea tar : tareasAsignadas) {
			horas = tar.getDuracionEstimada();
		}
		
		return horas;
	}
	
	public void comenzar(Integer idTarea) throws TareaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de inicio la fecha y hora actual
		Boolean aux = false;
		
			for(Tarea t : this.tareasAsignadas) {
				if(t.getId().equals(idTarea)) {
					t.setFechaInicio(LocalDateTime.now());
					aux = true;
					t.asignarEmpleado(this);
				}
			}
		
		if(aux==false) {
			throw new TareaException("La tarea no existe en la lista.");
		}

	}
	
	public void finalizar(Integer idTarea) throws TareaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		Boolean aux = false;
		
		for(Tarea t : this.tareasAsignadas) {
			if(t.getId().equals(idTarea)) {
				t.setFechaFin(LocalDateTime.now());
				aux = true;
			}
		}
	
		if(aux==false) {
			throw new TareaException("La tarea no existe en la lista.");
		}
	}

	public void comenzar(Integer idTarea,String fecha) throws TareaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		Boolean aux = false;
		
		for(Tarea t : this.tareasAsignadas) {
			if(t.getId().equals(idTarea)) {
				t.setFechaInicio(LocalDateTime.parse(fecha, this.formatter));
				aux = true;
			}
		}
	
		if(aux==false) {
			throw new TareaException("La tarea no existe en la lista.");
		}
		
	}
	
	public void finalizar(Integer idTarea,String fecha) throws TareaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		Boolean aux = false;
		
		for(Tarea t : this.tareasAsignadas) {
			if(t.getId().equals(idTarea)) {
				t.setFechaFin(LocalDateTime.parse(fecha, this.formatter));
				aux = true;
			}
		}
	
		if(aux==false) {
			throw new TareaException("La tarea no existe en la lista.");
		}
	}

	public Boolean finalizoAntes(Tarea t) {
		
		LocalDateTime fechaEst = t.getFechaInicio().plusDays(t.getDuracionEstimada());
		
		
		if(t.getFechaFin().isBefore(fechaEst)) {
			return true;
		}
		
		return false;
	}
	
	public long diferenciaDias(Tarea t) {
		long diasFin = t.getFechaFin().toLocalDate().toEpochDay();
		long diasCom = t.getFechaInicio().toLocalDate().toEpochDay();
		long diasUtilizados = diasFin - diasCom;
		int diasEsperados = t.getDuracionEstimada();
		
		return diasUtilizados - diasEsperados;
	}
	
	public Integer getCuil() {
		return cuil;
	}

	public void setCuil(Integer cuil) {
		this.cuil = cuil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Double getCostoHora() {
		return costoHora;
	}

	public void setCostoHora(Double costoHora) {
		this.costoHora = costoHora;
	}

	public List<Tarea> getTareasAsignadas() {
		return tareasAsignadas;
	}

	public void setTareasAsignadas(List<Tarea> tareasAsignadas) {
		this.tareasAsignadas = tareasAsignadas;
	}
	

}
