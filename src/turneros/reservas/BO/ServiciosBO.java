package turneros.reservas.BO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import turneros.BerkeleyDb.repository.ReservaRepository;
import turneros.BerkeleyDb.repository.ServiciosRepository;
import turneros.Common.entidades.Reserva;
import turneros.Common.entidades.Servicio;
import turneros.Common.entidades.Taquilla;
import turneros.Common.entidades.Turno;
import turneros.configuration.Configuration;

public class ServiciosBO {
	private ServiciosRepository servicioRepository;
	private String ruta = "";
	
	public ServiciosBO() {
		this.ruta = Configuration.getPreference("databaseUrl");
		servicioRepository = new ServiciosRepository(this.ruta);
	}
	
	public List<Servicio> listarServiciosDisponibles() throws SQLException {
		return servicioRepository.listarServiciosDisponibles();
	}
	
}
