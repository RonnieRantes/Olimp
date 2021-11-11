package pe.edu.upc.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.edu.upc.spring.model.Sucursal;
import pe.edu.upc.spring.model.Usuario;

@Controller
@RequestMapping("/registro")
public class RegistroController {
	@RequestMapping("/")
	public String irPaginaRegistro(@ModelAttribute("sucursal") Sucursal sucursal, Model model) {
		Usuario usuario = new Usuario();
		if(sucursal.getIdSucursal()==0) model.addAttribute("sucursal", null);
		else model.addAttribute("sucursal", sucursal);
		model.addAttribute("usuario", usuario);
		return "registro";
	}
}
