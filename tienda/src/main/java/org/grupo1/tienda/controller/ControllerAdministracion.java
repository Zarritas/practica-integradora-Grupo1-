package org.grupo1.tienda.controller;

import org.grupo1.tienda.component.FiltradoParametrizado;
import org.grupo1.tienda.model.catalog.MotivoBloqueo;
import org.grupo1.tienda.model.entity.Cliente;
import org.grupo1.tienda.model.entity.UsuarioEmpleadoCliente;
import org.grupo1.tienda.repository.ClienteRepository;
import org.grupo1.tienda.repository.MotivoBloqueoRepository;
import org.grupo1.tienda.repository.TipoClienteRepository;
import org.grupo1.tienda.repository.UsuarioEmpleadoClienteRepository;
import org.grupo1.tienda.service.ServicioSesion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequestMapping("admin")
public class ControllerAdministracion {
    private final String PREFIJO1 = "app/";

    private final ServicioSesion servicioSesion;
    private final UsuarioEmpleadoClienteRepository usuarioEmpleadoClienteRepository;
    private final ClienteRepository clienteRepository;
    private final MotivoBloqueoRepository motivoBloqueoRepository;
    private final TipoClienteRepository tipoClienteRepository;
    private final FiltradoParametrizado filtradoParametrizado;

    public ControllerAdministracion(ServicioSesion servicioSesion, UsuarioEmpleadoClienteRepository usuarioEmpleadoClienteRepository, ClienteRepository clienteRepository, MotivoBloqueoRepository motivoBloqueoRepository, TipoClienteRepository tipoClienteRepository, FiltradoParametrizado filtradoParametrizado) {
        this.servicioSesion = servicioSesion;
        this.usuarioEmpleadoClienteRepository = usuarioEmpleadoClienteRepository;
        this.clienteRepository = clienteRepository;
        this.motivoBloqueoRepository = motivoBloqueoRepository;
        this.tipoClienteRepository = tipoClienteRepository;
        this.filtradoParametrizado = filtradoParametrizado;
    }

    // Área personal de un usuario administrador.
    @GetMapping("administracion")
    public ModelAndView administracionGet(ModelAndView modelAndView) {
        // Si no se ha iniciado sesión correctamente y se intenta acceder directamente al área de administración
        // se redirige a la vista de login de los administradores de la aplicación.
        if (servicioSesion.getAdministradorLoggeado() == null) {
            modelAndView.setViewName("redirect:/usuario/authadmin");
        } else {
            modelAndView.setViewName(PREFIJO1 + "administracion");
        }
        return modelAndView;
    }

    @PostMapping("administracion")
    public ModelAndView administracionPost(ModelAndView modelAndView) {
        // Desconexión ordenada.
        servicioSesion.setAdministradorLoggeado(null);
        modelAndView.setViewName("redirect:/usuario/authadmin");
        return modelAndView;
    }

    @GetMapping("listado-usuarios")
    public ModelAndView listarDepartamentosGet(@ModelAttribute("flashAttribute") Object flashAttribute,
                                               ModelAndView modelAndView) {
        // Si no se ha iniciado sesión correctamente y se intenta acceder directamente al área de administración
        // se redirige a la vista de login de los administradores de la aplicación.
        if (servicioSesion.getAdministradorLoggeado() == null) {
            modelAndView.setViewName("redirect:/usuario/authadmin");
            return modelAndView;
        }
        servicioSesion.setListaUsuariosEmpleadoCliente(usuarioEmpleadoClienteRepository.findAll());
        // Se pasa el listado de usuarios a la vista
        modelAndView.addObject("lista_usuariosClienteEmpleado", servicioSesion.getListaUsuariosEmpleadoCliente());
        // Se evalúa si este método ha recibido un atributo flash
        if (!flashAttribute.getClass().getSimpleName().equals("String")) {
            flashAttribute = null;
        }
        // Se pasa el atributo flash a la vista
        modelAndView.addObject("exito", flashAttribute);
        modelAndView.setViewName(PREFIJO1 + "listado_usuarios");
        return modelAndView;
    }

    @GetMapping("listado-clientes")
    public ModelAndView listarClientesGet(@ModelAttribute("flashAttribute") Object flashAttribute,
                                          ModelAndView modelAndView) {
        // Si no se ha iniciado sesión correctamente y se intenta acceder directamente al área de administración
        // se redirige a la vista de login de los administradores de la aplicación.
        if (servicioSesion.getAdministradorLoggeado() == null) {
            modelAndView.setViewName("redirect:/usuario/authadmin");
            return modelAndView;
        }
        // Si no existe una lista de los tipos de cliente de la base de datos, se crea una.
        if (servicioSesion.getListaTiposCliente() == null) {
            servicioSesion.setListaTiposCliente(tipoClienteRepository.findAll());
        }
        // Se añade los tipos de cliente a la vista para que el administrador pueda filtar por uno de ellos.
        modelAndView.addObject("lista_tipo_cliente", servicioSesion.getListaTiposCliente());
        // Se actualiza la lista de clientes.
        servicioSesion.setListaClientes(clienteRepository.findAll());
        // Se pasa el listado de clientes a la vista.
        modelAndView.addObject("lista_clientes", servicioSesion.getListaClientes());
        // Se evalúa si este método ha recibido un atributo flash
        if (!flashAttribute.getClass().getSimpleName().equals("String")) {
            flashAttribute = null;
        }
        // Se pasa el atributo flash a la vista
        modelAndView.addObject("exito", flashAttribute);
        modelAndView.setViewName(PREFIJO1 + "listado_clientes");
        return modelAndView;
    }

    @PostMapping("listado-clientes")
    public ModelAndView listarClientesPost(ModelAndView modelAndView,
                                           @RequestParam(value = "finicio", required = false) String finicio,
                                           @RequestParam(value = "ffin", required = false) String ffin,
                                           @RequestParam(value = "tcliente", required = false) String tipoCliente,
                                           @RequestParam(value = "dmin", required = false) String dmin,
                                           @RequestParam(value = "dmax", required = false) String dmax,
                                           @RequestParam(value = "apellido", required = false) String apellido,
                                           @RequestParam(value = "boton", required = false) String boton) {
        // Se añade los tipos de cliente a la vista para que el administrador pueda filtar por uno de ellos.
        modelAndView.addObject("lista_tipo_cliente", servicioSesion.getListaTiposCliente());
        // Se actualiza la lista de clientes según el filtrado elegido
        servicioSesion.setListaClientes(filtradoParametrizado.filtrarListaClientes(boton, finicio, ffin, tipoCliente, dmin, dmax, apellido));
        // Se pasa el listado de clientes a la vista.
        modelAndView.addObject("lista_clientes", servicioSesion.getListaClientes());
        modelAndView.setViewName(PREFIJO1 + "listado_clientes");
        return modelAndView;
    }

    @GetMapping("detalle/{id}")
    public ModelAndView detallarClienteGet(ModelAndView modelAndView,
                                           @PathVariable UUID id) {
        // Si no se ha iniciado sesión correctamente y se intenta acceder directamente al área de administración
        // se redirige a la vista de login de los administradores de la aplicación.
        if (servicioSesion.getAdministradorLoggeado() == null) {
            modelAndView.setViewName("redirect:/usuario/authadmin");
            return modelAndView;
        }
        Optional<Cliente> c1 = clienteRepository.findById(id);
        if (c1.isPresent()) {
            Cliente cliente = c1.get();
            modelAndView.addObject("cliente", cliente);
            modelAndView.addObject("readonly", true);
            modelAndView.addObject("action", "detalle");
            modelAndView.setViewName(PREFIJO1 + "detalle_cliente");
        } else {
            modelAndView.setViewName("redirect:/admin/listado-usuarios");
        }
        return modelAndView;
    }

    @PostMapping("detalle")
    public ModelAndView detallarClientePost(ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/admin/listado");
        return modelAndView;
    }

    @GetMapping("modificacion/{id}")
    public ModelAndView modificarClienteGet(ModelAndView modelAndView,
                                            @PathVariable UUID id) {
        // Si no se ha iniciado sesión correctamente y se intenta acceder directamente al área de administración
        // se redirige a la vista de login de los administradores de la aplicación.
        if (servicioSesion.getAdministradorLoggeado() == null) {
            modelAndView.setViewName("redirect:/usuario/authadmin");
            return modelAndView;
        }
        Optional<Cliente> c1 = clienteRepository.findById(id);
        if (c1.isPresent()) {
            Cliente cliente = c1.get();
            modelAndView.addObject("cliente", cliente);
            modelAndView.addObject("readonly", false);
            modelAndView.addObject("action", "modificacion");
            modelAndView.setViewName(PREFIJO1 + "detalle_cliente");
        } else {
            modelAndView.setViewName("redirect:/admin/listado-clientes");
        }
        return modelAndView;
    }

    @GetMapping("bloqueo/{id}")
    public ModelAndView bloquearUsuarioGet(ModelAndView modelAndView,
                                           @PathVariable UUID id) {
        // Si no se ha iniciado sesión correctamente y se intenta acceder directamente al área de administración
        // se redirige a la vista de login de los administradores de la aplicación.
        if (servicioSesion.getAdministradorLoggeado() == null) {
            modelAndView.setViewName("redirect:/usuario/authadmin");
            return modelAndView;
        }
        // Si la sesión no tiene un listado de los motivos de bloqueo, se crea uno y se incluye en la sesión
        if (servicioSesion.getListaMotivosBloqueo() == null) {
            servicioSesion.setListaMotivosBloqueo(motivoBloqueoRepository.findAll());
        }
        Optional<UsuarioEmpleadoCliente> u1 = usuarioEmpleadoClienteRepository.findById(id);
        if (u1.isPresent()) {
            UsuarioEmpleadoCliente uec = u1.get();
            modelAndView.addObject("usuario", uec);
            modelAndView.addObject("lista_motivos", servicioSesion.getListaMotivosBloqueo());
            modelAndView.setViewName(PREFIJO1 + "modificacion_bloqueo");
        } else {
            modelAndView.setViewName("redirect:/admin/listado-usuarios");
        }
        return modelAndView;
    }

    @PostMapping("bloqueo/{id}")
    public ModelAndView bloquearUsuarioPost(ModelAndView modelAndView,
                                            @ModelAttribute("motivo") MotivoBloqueo motivoBloqueo,
                                            @PathVariable UUID id) {
        Optional<UsuarioEmpleadoCliente> u1 = usuarioEmpleadoClienteRepository.findById(id);
        if (u1.isPresent()) {
            UsuarioEmpleadoCliente uec = u1.get();
            uec.setMotivoBloqueo(motivoBloqueo);
            usuarioEmpleadoClienteRepository.save(uec);
        }
        modelAndView.setViewName("redirect:/admin/listado-usuarios");
        return modelAndView;
    }

    @GetMapping("nomina/{id}")
    public ModelAndView detallarNominasGet(ModelAndView modelAndView,
                                           @PathVariable UUID id) {
        // Si no se ha iniciado sesión correctamente y se intenta acceder directamente al área de administración
        // se redirige a la vista de login de los administradores de la aplicación.
        if (servicioSesion.getAdministradorLoggeado() == null) {
            modelAndView.setViewName("redirect:/usuario/authadmin");
            return modelAndView;
        }
        Optional<Cliente> c1 = clienteRepository.findById(id);
        if (c1.isPresent()) {
            Cliente cliente = c1.get();
            modelAndView.addObject("cliente", cliente);
            modelAndView.addObject("readonly", true);
            modelAndView.addObject("action", "detalle");
            modelAndView.setViewName(PREFIJO1 + "detalle_cliente");
        } else {
            modelAndView.setViewName("redirect:/admin/listado-usuarios");
        }
        return modelAndView;
    }
}
