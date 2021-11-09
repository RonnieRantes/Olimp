package pe.edu.upc.spring.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sun.el.parser.ParseException;

import pe.edu.upc.spring.model.Servicio;
import pe.edu.upc.spring.model.Sucursal;
import pe.edu.upc.spring.service.ICalificacionService;
import pe.edu.upc.spring.service.IImagenService;
import pe.edu.upc.spring.service.IServicioService;
import pe.edu.upc.spring.service.ISucursalService;
import pe.edu.upc.spring.service.ITarifaService;

@Controller
@RequestMapping("/visualizar")
public class VisualizarController {
	@Autowired
	public IServicioService sService;
	@Autowired
	public ISucursalService suService;
	@Autowired
	public ICalificacionService cService;
	@Autowired
	public ITarifaService tService;
	@Autowired
	public IImagenService iService;
	
	@RequestMapping("/sucursal/{id}")
	public String irVisualizarSucursal(@PathVariable int id, Model model, RedirectAttributes objRedir) throws ParseException {
		Optional<Sucursal> objSucursal = suService.buscarId(id);
		if (objSucursal == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrio un error");
			return "redirect:/busqueda/sucursal/";
		}
		else {		
			if (objSucursal.isPresent()) {
				objSucursal.ifPresent(o -> model.addAttribute("sucursal", o));
				model.addAttribute("listaServicios", sService.buscarSucursal(id));
				//Calificaciones
				model.addAttribute("total", cService.contarCalificaciones("sucursal", id, -1));			
				model.addAttribute("media", cService.contarCalificaciones("sucursal", id, 0));
				for(int i = 1; i <= 5; i++) model.addAttribute("cal"+i,cService.contarCalificaciones("sucursal", id, i));	
			}
			return "verSucursal";
		}
	}
	@RequestMapping("/servicio/{id}")
	public String irVisualizarServicio(@PathVariable int id, Model model, RedirectAttributes objRedir) throws ParseException {
		Optional<Servicio> objServicio = sService.buscarId(id);
		if (objServicio == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrio un error");
			return "redirect:/busqueda/servicio/";
		}
		else {		
			if (objServicio.isPresent()) {
				objServicio.ifPresent(o -> model.addAttribute("servicio", o));
				model.addAttribute("listaServicios", sService.buscarSucursal(id));
				model.addAttribute("listaTarifas", tService.buscarServicio(id));
				model.addAttribute("listaImagenes", iService.buscarServicio(id));
				//Calificaciones
				model.addAttribute("listaCalificaciones", cService.buscarServicio(id));
				model.addAttribute("total", cService.contarCalificaciones("sucursal", id, -1));			
				model.addAttribute("media", cService.contarCalificaciones("servicio", id, 0));			
				for(int i = 1; i <= 5; i++) model.addAttribute("cal"+i,cService.contarCalificaciones("servicio", id, i));	
			}
			return "verServicio";
		}
	}
	
	@RequestMapping("/imagen/")
	public String irVisualizarImagen() throws ParseException{
		return "verImagen";
	}
}