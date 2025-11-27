package com.grupotfg.weekycook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.grupotfg.weekycook.dto.request.PlanSemanalRequestDTO;
import com.grupotfg.weekycook.dto.request.PlanItemRequestDTO;
import com.grupotfg.weekycook.dto.response.PlanSemanalResponseDTO;
import com.grupotfg.weekycook.service.PlanSemanalServiceImpl;

import java.net.URI;
import java.util.List;

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
@RequestMapping("/api/planes")
public class PlanSemanalController {

    @Autowired
    private PlanSemanalServiceImpl service;

    @PostMapping("/usuario")
    public ResponseEntity<PlanSemanalResponseDTO> crearPlan(
            @RequestHeader("usuarioId") Integer usuarioId,
            @RequestBody PlanSemanalRequestDTO dto) {
        PlanSemanalResponseDTO created = service.crearPlan(usuarioId, dto);
        // location header opcional
        return ResponseEntity.created(URI.create("/api/planes/" + created.getId())).body(created);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<PlanSemanalResponseDTO> obtenerPlan(
            @RequestHeader("usuarioId") Integer usuarioId,
            @PathVariable Integer planId) {
        PlanSemanalResponseDTO dto = service.obtenerPlan(usuarioId, planId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{planId}")
    public ResponseEntity<PlanSemanalResponseDTO> actualizarPlan(
            @RequestHeader("usuarioId") Integer usuarioId,
            @PathVariable Integer planId,
            @RequestBody PlanSemanalRequestDTO dto) {
        PlanSemanalResponseDTO updated = service.actualizarPlan(usuarioId, planId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> eliminarPlan(
            @RequestHeader("usuarioId") Integer usuarioId,
            @PathVariable Integer planId) {
        service.eliminarPlan(usuarioId, planId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{planId}/items")
    public ResponseEntity<PlanSemanalResponseDTO> addOrUpdateItem(
            @RequestHeader("usuarioId") Integer usuarioId,
            @PathVariable Integer planId,
            @RequestBody PlanItemRequestDTO itemDto) {
        PlanSemanalResponseDTO dto = service.addOrUpdateItem(usuarioId, planId, itemDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{planId}/items")
    public ResponseEntity<Void> eliminarItem(
            @RequestHeader("usuarioId") Integer usuarioId,
            @PathVariable Integer planId,
            @RequestParam String dia,
            @RequestParam String turno) {
        service.eliminarItem(usuarioId, planId, dia, turno);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{planId}/aleatorio")
    public ResponseEntity<PlanSemanalResponseDTO> rellenarAleatorio(
            @RequestHeader("usuarioId") Integer usuarioId,
            @PathVariable Integer planId) {
        PlanSemanalResponseDTO dto = service.rellenarPlanAleatorio(usuarioId, planId);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PlanSemanalResponseDTO>> obtenerPlanesDeUsuario(
            @PathVariable Integer usuarioId) {
        List<PlanSemanalResponseDTO> dtos = service.obtenerPlanesDeUsuario(usuarioId);
        return ResponseEntity.ok(dtos);
    }
    

}