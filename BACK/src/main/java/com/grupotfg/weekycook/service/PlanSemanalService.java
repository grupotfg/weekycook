package com.grupotfg.weekycook.service;

import com.grupotfg.weekycook.dto.request.PlanItemRequestDTO;
import com.grupotfg.weekycook.dto.request.PlanSemanalRequestDTO;
import com.grupotfg.weekycook.dto.response.PlanSemanalResponseDTO;

import java.util.List;

public interface PlanSemanalService {

    PlanSemanalResponseDTO crearPlan(Integer usuarioId, PlanSemanalRequestDTO dto);

    PlanSemanalResponseDTO obtenerPlanPorId(Integer usuarioId, Integer planId);

    List<PlanSemanalResponseDTO> obtenerPlanesDeUsuario(Integer usuarioId);

    PlanSemanalResponseDTO actualizarPlan(Integer usuarioId, Integer planId, PlanSemanalRequestDTO dto);

    void eliminarPlan(Integer usuarioId, Integer planId);

    // Items

    PlanSemanalResponseDTO asignarItem(
            Integer usuarioId, Integer planId, PlanItemRequestDTO itemDTO);

    PlanSemanalResponseDTO eliminarItem(
            Integer usuarioId, Integer planId, String dia, String turno);
}
