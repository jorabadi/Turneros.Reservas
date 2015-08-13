package turneros.reservas.BO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import turneros.BerkeleyDb.repository.ReservaRepository;
import turneros.Common.entidades.Reserva;
import turneros.Common.entidades.Servicio;
import turneros.Common.entidades.Taquilla;
import turneros.Common.entidades.Turno;
import turneros.configuration.Configuration;

public class ReservaBO {
	private ReservaRepository reservaRepository;
	private int minTurno = 0;
	private int maxTurno = 0;
	private String ruta = "";
	
	public ReservaBO() {
		this.ruta = Configuration.getPreference("databaseUrl");
		reservaRepository = new ReservaRepository(this.ruta);
		this.minTurno = Integer.parseInt(Configuration.getPreference("minTurno"));
		this.maxTurno = Integer.parseInt(Configuration.getPreference("maxTurno"));
	}
	
	public Reserva crearReserva(Servicio servicio) throws SQLException {
		//reservaRepository.openTransaccion();
		//obtengo el turno 
		Servicio stmp = reservaRepository.obtenerServicio(servicio);
		
		int numTurno = minTurno; // por defecto el valor es el minimo turno que es  en su mayor de casos 0
	    
	    if(stmp != null && stmp.getTurno()!=null) {
	    	numTurno = Integer.parseInt(stmp.getTurno());
	    	numTurno++;
	    	if(numTurno > maxTurno) { // en caso de que llegue al maximo se devuelve al menor turo
	    		numTurno = minTurno;
	    	}
	    	
	    	stmp.setTurno(String.valueOf(numTurno));
	    } else {
	    	stmp = servicio;
			servicio.setTurno(String.valueOf(numTurno));
		}
	    
	    System.out.println("Obtengo el turno del servicio:"+stmp.getNombre()+" , el turno:"+numTurno);
	    reservaRepository.guardarServicio(stmp); // actualizo el turno que se creo
		
		//guardo la reserva para e servicio con el turno
		Reserva reserva = new Reserva();
		reserva.setTurno(stmp.getLabel()+String.valueOf(numTurno));
		reserva.setEstado("disponible");
		reserva.setCodigoServicio(stmp.getCodigoServicio());
		reserva.setServicio(stmp.getNombre());
		reserva = reservaRepository.guardarReserva(reserva);
		
//		reserva = reservaRepository.obtenerInfoReserva(reserva);
		//reservaRepository.commitTransaccion();
		return reserva;
	}
	
	public Reserva asignarReservaTaquilla(Taquilla taquilla) throws SQLException {
		//busco reserva disponibles para los servicios de la taquilla
		Reserva reserva = reservaRepository.buscarReservaDisponibleTaquilla(taquilla);
		
		if(reserva ==null) {
			return null;
		}
		//asocio la reserva a la taquilla y la pongo como no disponible
		reserva.setCodigoTaquilla(taquilla.getCodigoTaquilla());
		reserva.setEstado("asignado");
		reserva = reservaRepository.guardarReserva(reserva);
		return reserva;
	}
	
	public static void main (String []args) {
		ReservaBO boReserva = new ReservaBO();
		 for(int i=1; i<5; i++){
			Servicio s = new Servicio();
			s.setCodigoServicio(i);
			s.setNombre("Agenda-"+i);
			try {
				Reserva r = boReserva.crearReserva(s);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Taquilla taquilla = new Taquilla();
		taquilla.setCodigoTaquilla("1");
		taquilla.setNombre("Taquilla 1");
		try {
			boReserva.asignarReservaTaquilla(taquilla);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
