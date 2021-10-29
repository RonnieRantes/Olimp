package pe.edu.upc.spring.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sun.el.parser.ParseException;


import pe.edu.upc.spring.model.Tarifa;
import pe.edu.upc.spring.model.TipoVehiculo;
import pe.edu.upc.spring.model.Servicio;
import pe.edu.upc.spring.service.ITarifaService;
import pe.edu.upc.spring.service.ITipoVehiculoService;
import pe.edu.upc.spring.service.IServicioService;

@Controller
@RequestMapping("/tarifa")
public class TarifaController {
	
	@Autowired
	private IServicioService sService;
	
	@Autowired
	private ITipoVehiculoService tvService;
	
	@Autowired
	private ITarifaService tService;	
	
	//Páginas
	@RequestMapping("/")
	public String irPaginaListado(Map<String, Object> model) {
		model.put("listaTarifas", tService.listar());
		return "listTarifa"; //panel sucursal
	}
	
	@RequestMapping("/irRegistrar")
	public String irPaginaRegistrar(Model model) {
		
		model.addAttribute("tipoVehiculo", new TipoVehiculo());
		model.addAttribute("servicio", new Servicio());
		model.addAttribute("tarifa", new Tarifa());
		
		model.addAttribute("listaTipoVehiculo", tvService.listar());
		model.addAttribute("listaServicios", sService.listar());		
		
		return "tarifa";
	}
	
	//Funciones
	@RequestMapping("/registrar")
	public String registrar(@ModelAttribute Tarifa objTarifa, BindingResult binRes, Model model)
			throws ParseException
	{
		if (binRes.hasErrors()) 
			{
				model.addAttribute("listaTipoVehiculo", tvService.listar());
				model.addAttribute("listaServicios", sService.listar());			
				return "tarifa";
			}
		else {
			boolean flag = tService.registrar(objTarifa);
			if (flag)
				return "redirect:/tarifa/"; //panel sucursal
			else {
				model.addAttribute("mensaje", "Ocurrio un error");
				return "redirect:/tarifa/irRegistrar";
			}
		}
	}
	
	@RequestMapping("/modificar/{id}")
	public String modificar(@PathVariable int id, Model model, RedirectAttributes objRedir)
		throws ParseException 
	{
		Optional<Tarifa> objTarifa = tService.buscarId(id);
		if (objTarifa == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrio un error");
			return "redirect:/tarifa/"; //panel sucursal
		}
		else {
			model.addAttribute("listaTipoVehiculo", tvService.listar());
			model.addAttribute("listaServicios", sService.listar());			
					
			if (objTarifa.isPresent())
				objTarifa.ifPresent(o -> model.addAttribute("tarifa", o));
			
			return "tarifa";
		}
	}
	
	@RequestMapping("/eliminar")
	public String eliminar(Map<String, Object> model, @RequestParam(value="id") Integer id) {
		try {
			if (id!=null && id>0) {
				tService.eliminar(id);
				model.put("listaTarifa", tService.listar());
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			model.put("mensaje","Ocurrio un error");
			model.put("listaTarifa", tService.listar());
			
		}
		return "redirect:/tarifa/";
	}
	/////////////////
	@RequestMapping("/listar")
	public String listar(Map<String, Object> model) {
		model.put("listaTarifa", tService.listar());
		return "listTarifa";
	}		
	
	@RequestMapping("/buscarId")
	public String buscarId(Map<String, Object> model, @ModelAttribute Tarifa Tarifa) 
	throws ParseException
	{
		tService.buscarId(Tarifa.getIdTarifa());
		return "listTarifa";
	}	
	
	@RequestMapping("/irBuscar")
	public String irBuscar(Model model) 
	{
		model.addAttribute("Tarifa", new Tarifa());
		return "buscar";
	}
	
	@RequestMapping("/buscar")
	public String buscar(Map<String, Object> model, @ModelAttribute Tarifa Tarifa)
			throws ParseException
	{
		List<Tarifa> listaTarifa;
		listaTarifa = tService.buscarSucursal(Tarifa.getServicio().getIdServicio());
		if (listaTarifa.isEmpty()) {
			listaTarifa = tService.buscarServicio(Tarifa.getServicio().getIdServicio());
		}
		if (listaTarifa.isEmpty()) {
			model.put("mensaje", "No existen coincidencias");		
		}
		model.put("listaTarifas", listaTarifa);
		return "buscar";
	}
}