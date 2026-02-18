package com.grupotfg.weekycook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.grupotfg.weekycook.dto.request.PlanSemanalRequestDTO;
import com.grupotfg.weekycook.dto.request.PlanItemRequestDTO;
import com.grupotfg.weekycook.dto.response.PlanSemanalResponseDTO;
import com.grupotfg.weekycook.dto.response.ListaCompraItemDto;

import com.grupotfg.weekycook.service.PlanSemanalServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * PlanSemanalController
 *
 * Conrequestheader ("usuarioId") para identificar al usuario que llama (simula autenticación hasta mejora)
 * Endpoints:
 *    POST /api/planes/usuario         -> crear plan
 *    GET  /api/planes/{planId}        -> obtener plan
 *    PUT  /api/planes/{planId}        -> actualizar plan
 *    DELETE /api/planes/{planId}      -> eliminar plan
 *    POST /api/planes/{planId}/items  -> añadir/actualizar item (dia/turno)
 *    DELETE /api/planes/{planId}/items?dia=...&turno=... -> eliminar item
 *    POST /api/planes/{planId}/aleatorio -> rellenar plan 100% aleatorio
 */
@RestController
@RequestMapping("/planes")
@CrossOrigin(origins = "*")
@Tag(name = "Planes semanales", description = "Gestión de planes semanales, items, generación aleatoria y lista de la compra")
public class PlanSemanalController {

    @Autowired
    private PlanSemanalServiceImpl service;

    @PostMapping("/usuario")
    @Operation(summary = "Crear un plan semanal", description = "Crea un nuevo plan semanal para el usuario indicado")
    public ResponseEntity<PlanSemanalResponseDTO> crearPlan(
            @Parameter(description = "ID del usuario propietario del plan")
            @RequestHeader("usuarioId") Integer usuarioId,
            @RequestBody PlanSemanalRequestDTO dto) {
        PlanSemanalResponseDTO created = service.crearPlan(usuarioId, dto);
        // location header opcional
        URI location = Objects.requireNonNull(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri()
        );
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{planId}")
    @Operation(summary = "Obtener un plan semanal", description = "Recupera un plan semanal concreto por su ID, validando acceso del usuario")
    public ResponseEntity<PlanSemanalResponseDTO> obtenerPlan(
            @Parameter(description = "ID del usuario que solicita el plan")
            @RequestHeader("usuarioId") Integer usuarioId,
            @Parameter(description = "ID del plan semanal")
            @PathVariable Integer planId) {
        PlanSemanalResponseDTO dto = service.obtenerPlan(usuarioId, planId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{planId}")
    @Operation(summary = "Actualizar un plan semanal", description = "Actualiza los datos básicos de un plan semanal existente")
    public ResponseEntity<PlanSemanalResponseDTO> actualizarPlan(
            @Parameter(description = "ID del usuario que realiza la acción")
            @RequestHeader("usuarioId") Integer usuarioId,
            @Parameter(description = "ID del plan semanal a actualizar")
            @PathVariable Integer planId,
            @RequestBody PlanSemanalRequestDTO dto) {
        PlanSemanalResponseDTO updated = service.actualizarPlan(usuarioId, planId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{planId}")
    @Operation(summary = "Eliminar un plan semanal", description = "Elimina un plan semanal por su ID")
    public ResponseEntity<Void> eliminarPlan(
            @Parameter(description = "ID del usuario que realiza la acción")
            @RequestHeader("usuarioId") Integer usuarioId,
            @Parameter(description = "ID del plan semanal a eliminar")
            @PathVariable Integer planId) {
        service.eliminarPlan(usuarioId, planId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{planId}/items")
    @Operation(summary = "Añadir o actualizar un item del plan", description = "Asigna o actualiza una receta para un día y turno concretos del plan semanal")
    public ResponseEntity<PlanSemanalResponseDTO> addOrUpdateItem(
            @Parameter(description = "ID del usuario que realiza la acción")
            @RequestHeader("usuarioId") Integer usuarioId,
            @Parameter(description = "ID del plan semanal")
            @PathVariable Integer planId,
            @RequestBody PlanItemRequestDTO itemDto) {
        PlanSemanalResponseDTO dto = service.addOrUpdateItem(usuarioId, planId, itemDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{planId}/items")
    @Operation(summary = "Eliminar un item del plan", description = "Elimina la receta asignada a un día y turno específicos del plan semanal")
    public ResponseEntity<Void> eliminarItem(
            @Parameter(description = "ID del usuario que realiza la acción")
            @RequestHeader("usuarioId") Integer usuarioId,
            @Parameter(description = "ID del plan semanal")
            @PathVariable Integer planId,
            @Parameter(description = "Día de la semana (ej: Lunes)")
            @RequestParam String dia,
            @Parameter(description = "Turno (Comida/Cena)")
            @RequestParam String turno) {
        service.eliminarItem(usuarioId, planId, dia, turno);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{planId}/aleatorio")
    @Operation(summary = "Rellenar plan semanal aleatoriamente", description = "Genera un plan semanal completo asignando recetas aleatorias a todos los días y turnos")
    public ResponseEntity<PlanSemanalResponseDTO> rellenarAleatorio(
            @Parameter(description = "ID del usuario que realiza la acción")
            @RequestHeader("usuarioId") Integer usuarioId,
            @Parameter(description = "ID del plan semanal")
            @PathVariable Integer planId) {
        PlanSemanalResponseDTO dto = service.rellenarPlanAleatorio(usuarioId, planId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar planes de un usuario", description = "Obtiene el historial de planes semanales de un usuario")
    public ResponseEntity<List<PlanSemanalResponseDTO>> obtenerPlanesDeUsuario(
            @Parameter(description = "ID del usuario del que se quieren ver los planes")
            @PathVariable Integer usuarioId) {
        List<PlanSemanalResponseDTO> dtos = service.obtenerPlanesDeUsuario(usuarioId);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario/{usuarioId}/fecha")
    @Operation(summary = "Listar planes por rango de fechas", description = "Obtiene los planes semanales de un usuario filtrando por un rango de fechas de semana de inicio")
    public ResponseEntity<List<PlanSemanalResponseDTO>> obtenerPlanesDeUsuarioPorFecha(
            @Parameter(description = "ID del usuario del que se quieren ver los planes")
            @PathVariable Integer usuarioId,
            @Parameter(description = "Fecha de inicio del rango (semana_inicio >= desde, formato yyyy-MM-dd)")
            @RequestParam("desde") java.time.LocalDate desde,
            @Parameter(description = "Fecha fin del rango (semana_inicio <= hasta, formato yyyy-MM-dd)")
            @RequestParam("hasta") java.time.LocalDate hasta) {

        List<PlanSemanalResponseDTO> dtos = service.obtenerPlanesDeUsuarioPorRangoFecha(usuarioId, desde, hasta);
        return ResponseEntity.ok(dtos);
    }

    // Lista de la compra a partir de un plan semanal
    // GET /api/planes/{planId}/lista-compra
    @GetMapping("/{planId}/lista-compra")
    @Operation(summary = "Generar lista de la compra", description = "Genera la lista de la compra agregada a partir de las recetas de un plan semanal")
    public ResponseEntity<List<ListaCompraItemDto>> obtenerListaCompra(
            @Parameter(description = "ID del usuario que realiza la acción")
            @RequestHeader("usuarioId") Integer usuarioId,
            @Parameter(description = "ID del plan semanal del que generar la lista de la compra")
            @PathVariable Integer planId) {
        List<ListaCompraItemDto> lista = service.generarListaCompra(usuarioId, planId);
        return ResponseEntity.ok(lista);
    }
}