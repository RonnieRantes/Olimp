package pe.edu.upc.spring.controller;

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

import pe.edu.upc.spring.model.TipoVehiculo;
import pe.edu.upc.spring.service.ITipoVehiculoService;

@Controller
@RequestMapping("/tipovehiculo")
public class TipoVehiculoController {
	@Autowired
	private ITipoVehiculoService tService;
	
	@RequestMapping("/")
	public String irPaginaEntidad(Model model) {
		model.addAttribute("tipoVehiculo", new TipoVehiculo());
		return "/Entidad/tipoVehiculo";
	}
	//CRUD
	@RequestMapping("/registrar")
	public String registrar(@ModelAttribute TipoVehiculo objTipo, BindingResult binRes, Model model) throws ParseException{
		if(binRes.hasErrors()) model.addAttribute("mensaje", "Ocurrio un error");
		else {
			boolean flag = tService.registrar(objTipo);
			model.addAttribute("titulo", "REGISTRAR TIPO DE VEHICULO");
			if (flag) return "redirect:/admin/tipovehiculo/";
			else model.addAttribute("mensaje", "Ocurrio un error");
		}
		return "/Entidad/tipoVehiculo";
	}
	@RequestMapping("/modificar/{id}")
	public String modificar(@PathVariable int id, Model model, RedirectAttributes objRedir) {
		Optional<TipoVehiculo> objTipo = tService.buscarId(id);
		if (objTipo == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrio un error");
			return "redirect:/admin/tipovehiculo/";
		}
		else {
			model.addAttribute("tipoVehiculo", objTipo);
			model.addAttribute("titulo", "MODIFICAR TIPO DE VEHICULO");
			if (objTipo.isPresent()) objTipo.ifPresent(o -> model.addAttribute("usuario", o));
			return "/Entidad/tipoVehiculo";
		}
	}
	@RequestMapping("/eliminar")
	public String eliminar(RedirectAttributes objRedir, @RequestParam(value="id") Integer id) {
		try {
			if (id!=null && id>0) tService.eliminar(id);
		}
		catch(Exception ex) {
			objRedir.addFlashAttribute("listaTipoVehiculo", tService.listar());
		}
		return "redirect:/admin/tipovehiculo/"; 
	}
}
