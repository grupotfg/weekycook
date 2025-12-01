package com.grupotfg.weekycook.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupotfg.weekycook.dto.request.PlanItemRequestDTO;
import com.grupotfg.weekycook.dto.request.PlanSemanalRequestDTO;
import com.grupotfg.weekycook.dto.response.PlanSemanalResponseDTO;
import com.grupotfg.weekycook.dto.response.ListaCompraItemDto;

import com.grupotfg.weekycook.entity.*;
import com.grupotfg.weekycook.mapper.PlanItemMapper;
import com.grupotfg.weekycook.mapper.PlanSemanalMapper;
import com.grupotfg.weekycook.repository.*;
import com.grupotfg.weekycook.util.EnumParser;

@Service
public class PlanSemanalServiceImpl {

    @Autowired private PlanSemanalRepository planRepository;
    @Autowired private PlanItemRepository planItemRepository;
    @Autowired private RecetaRepository recetaRepository;
    @Autowired private RecetasFavoritasRepository favoritosRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PlanSemanalMapper planSemanalMapper;
    @Autowired private PlanItemMapper planItemMapper;

    private final Random random = new Random();

    // ----- crud planes
   
    // ... (rest of the code remains the same)

    @Transactional(readOnly = true)
    public List<PlanSemanalResponseDTO> obtenerPlanesDeUsuario(Integer usuarioId) {
        return planRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(planSemanalMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ----- lista de la compra a partir del plan semanal -----

    @Transactional(readOnly = true)
    public List<ListaCompraItemDto> generarListaCompra(Integer usuarioId, Integer planId) {

        PlanSemanal plan = planRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("Plan no encontrado"));

        checkAccess(usuarioId, plan);

        Map<String, ListaCompraItemDto> acumulado = new HashMap<>();

        for (PlanItem item : plan.getItems()) {
            Receta receta = item.getReceta();
            if (receta == null || receta.getRecetaIngredientes() == null) {
                continue;
            }

            for (RecetaIngrediente ri : receta.getRecetaIngredientes()) {
                Ingrediente ing = ri.getIngrediente();
                if (ing == null || ri.getCantidad() == null) {
                    continue;
                }

                String unidad = ri.getUnidad();
                Double cantidad = ri.getCantidad();

                String key = ing.getId() + "|" + unidad;
                ListaCompraItemDto dto = acumulado.get(key);
                if (dto == null) {
                    dto = ListaCompraItemDto.builder()
                            .ingredienteId(ing.getId())
                            .nombre(ing.getNombre())
                            .unidad(unidad)
                            .cantidadTotal(0.0)
                            .build();
                    acumulado.put(key, dto);
                }

                dto.setCantidadTotal(dto.getCantidadTotal() + cantidad);
            }
        }

        return new ArrayList<>(acumulado.values());
    }
}