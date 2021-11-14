package pe.edu.upc.spring.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pe.edu.upc.spring.model.Sucursal;
import pe.edu.upc.spring.model.Usuario;
import pe.edu.upc.spring.service.ICalificacionService;
import pe.edu.upc.spring.service.IDistritoService;
import pe.edu.upc.spring.service.IFavoritoService;
import pe.edu.upc.spring.service.IImagenService;
import pe.edu.upc.spring.service.IServicioService;
import pe.edu.upc.spring.service.ISolicitudService;
import pe.edu.upc.spring.service.ISucursalService;
import pe.edu.upc.spring.service.ITarifaService;
import pe.edu.upc.spring.service.IUsuarioService;

@Controller
@RequestMapping("/panel")
public class PanelUsuarioController {
	@Autowired
	private IUsuarioService uService;
	@Autowired
	private IFavoritoService fService;
	@Autowired
	private IServicioService sService;
	@Autowired
	private ICalificacionService cService;
	@Autowired
	private ISucursalService suService;
	@Autowired
	private IImagenService iService;
	@Autowired
	private ITarifaService tService;
	@Autowired
	private ISolicitudService soService;
	@Autowired
	private IDistritoService dService;
	
	@RequestMapping("/")
	public String irPanel() {
		return "redirect:/panel/cliente/";
	}
	
	//CLIENTE
	@RequestMapping("/cliente/")
	public String irPanelPerfil(Model model, RedirectAttributes objRedir) {
		Optional<Usuario> objUsuario = uService.buscarId("00000000");
		if (objUsuario == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrio un error");
			return "redirect:/inicio/";
		}
		else {
			model.addAttribute("usuario", objUsuario);
			if (objUsuario.isPresent())
				objUsuario.ifPresent(o -> model.addAttribute("usuario", o));
			
		}
		return "/Cliente/PC_Cuenta";
	}
	@RequestMapping("/cliente/favoritos/")
	public String irPanelFavoritos(Model model) {
		model.addAttribute("listaFavoritos", fService.listarServicios("00000000"));
		return "/Cliente/PC_Favorito";
	}
	@RequestMapping("/cliente/solicitudes/")
	public String irPanelSolicitudesRealizadas(Model model) {
		model.addAttribute("listaSolicitudes", soService.buscarUsuario("00000000"));
		return "/Cliente/PC_Solicitud";
	}
	@RequestMapping("/cliente/calificaciones/")
	public String irPanelCalificaciones(Model model) {
		model.addAttribute("listaCalificaciones", cService.buscarUsuario("00000000"));
		return "/Cliente/PC_Calificacion";
	}
	//SUCURSAL
	@RequestMapping("/sucursal/")
	public String irPanelSucursal(Model model, RedirectAttributes objRedir) {
		Optional<Sucursal> objSucursal = suService.buscarId(1);
		if (objSucursal == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrio un error");
			return "redirect:/panel/sucursal/cuenta/";
		}
		else {
			model.addAttribute("sucursal", objSucursal);
			model.addAttribute("listaDistritos", dService.listar());
			if (objSucursal.isPresent()) objSucursal.ifPresent(o -> model.addAttribute("sucursal", o));
		}
		return "/Sucursal/PS_Sucursal";
	}
	@RequestMapping("/sucursal/cuenta/")
	public String irPanelCuenta(Model model, RedirectAttributes objRedir) {
		Optional<Usuario> objUsuario = uService.buscarId("00000000");
		if (objUsuario == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrio un error");
			return "redirect:/inicio/";
		}
		else {
			model.addAttribute("usuario", objUsuario);
			if (objUsuario.isPresent()) objUsuario.ifPresent(o -> model.addAttribute("usuario", o));
		}
		return "/Sucursal/PS_Cuenta";
	}
	@RequestMapping("/sucursal/servicios/")
	public String irPanelServicios(Model model) {
		model.addAttribute("listaServicios", sService.buscarSucursal(1));
		return "/Sucursal/PS_Servicio";
	}
	@RequestMapping("/sucursal/tarifario/")
	public String irPanelTarifario(Model model) {
		model.addAttribute("listaTarifas", tService.buscarSucursal(1));
		return "/Sucursal/PS_Tarifa";
	}
	@RequestMapping("/sucursal/galeria/")
	public String irPanelGaleria(Model model) {
		model.addAttribute("listaImagenes", iService.buscarSucursal(1));
		return "/Sucursal/PS_Galeria";
	}
	@RequestMapping("/sucursal/solicitudes/")
	public String irPanelSolicitudes(Model model) {
		model.addAttribute("listaSolicitudes", soService.buscarSucursal(1));
		return "/Sucursal/PS_Solicitud";
	}
}