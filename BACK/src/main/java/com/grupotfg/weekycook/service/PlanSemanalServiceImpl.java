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
   

    @Transactional
    public PlanSemanalResponseDTO crearPlan(Integer usuarioId, PlanSemanalRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        Optional<PlanSemanal> existing =
                planRepository.findByUsuarioIdAndSemanaInicio(usuarioId, dto.getSemanaInicio());

        if (existing.isPresent()) {
            throw new IllegalStateException("Ya existe un plan para esa semana");
        }

        PlanSemanal plan = new PlanSemanal();
        plan.setUsuario(usuario);
        plan.setNombre(dto.getNombre());
        plan.setSemanaInicio(dto.getSemanaInicio());
        plan.setNumComensales(dto.getNumComensales() == null ? 1 : dto.getNumComensales());
        plan.setObservaciones(dto.getObservaciones());
        plan.setFechaCreacion(LocalDateTime.now());

        if (dto.getItems() != null) {
            for (PlanItemRequestDTO i : dto.getItems()) {
                plan.addItem(buildItemFromRequest(i, plan));
            }
        }

        plan = planRepository.save(plan);
        return mapPlanToResponse(plan);
    }

    @Transactional(readOnly = true)
    public PlanSemanalResponseDTO obtenerPlan(Integer usuarioId, Integer planId) {

        PlanSemanal plan = planRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("Plan no encontrado"));

        checkAccess(usuarioId, plan);
        return mapPlanToResponse(plan);
    }

    @Transactional
    public PlanSemanalResponseDTO actualizarPlan(Integer usuarioId, Integer planId, PlanSemanalRequestDTO dto) {

        PlanSemanal plan = planRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("Plan no encontrado"));

        checkAccess(usuarioId, plan);

        plan.setNombre(dto.getNombre());
        plan.setSemanaInicio(dto.getSemanaInicio());
        plan.setNumComensales(dto.getNumComensales());
        plan.setObservaciones(dto.getObservaciones());

        plan = planRepository.save(plan);
        return mapPlanToResponse(plan);
    }

    @Transactional
    public void eliminarPlan(Integer usuarioId, Integer planId) {

        PlanSemanal plan = planRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("Plan no encontrado"));

        checkAccess(usuarioId, plan);
        planRepository.delete(plan);
    }

    
    // ----- items
    

    @Transactional
    public PlanSemanalResponseDTO addOrUpdateItem(Integer usuarioId, Integer planId, PlanItemRequestDTO itemDto) {

        PlanSemanal plan = planRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("Plan no encontrado"));

        checkAccess(usuarioId, plan);

        DiaSemanaEnum dia = EnumParser.parse(DiaSemanaEnum.class, itemDto.getDia());
        TurnoEnum turno = EnumParser.parse(TurnoEnum.class, itemDto.getTurno());

        Receta receta = recetaRepository.findById(itemDto.getRecetaId())
                .orElseThrow(() -> new NoSuchElementException("Receta no encontrada"));

        Optional<PlanItem> existing =
                planItemRepository.findByPlanSemanalIdAndDiaAndTurno(planId, dia, turno);

        if (existing.isPresent()) {
            PlanItem item = existing.get();
            item.setReceta(receta);
            item.setNotas(itemDto.getNotas());
            planItemRepository.save(item);
        } else {
            PlanItem item = new PlanItem();
            item.setPlanSemanal(plan);
            item.setDia(dia);
            item.setTurno(turno);
            item.setReceta(receta);
            item.setNotas(itemDto.getNotas());
            plan.addItem(item);
        }

        planRepository.save(plan);
        return mapPlanToResponse(plan);
    }

    @Transactional
    public void eliminarItem(Integer usuarioId, Integer planId, String diaStr, String turnoStr) {

        PlanSemanal plan = planRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("Plan no encontrado"));

        checkAccess(usuarioId, plan);

        DiaSemanaEnum dia = EnumParser.parse(DiaSemanaEnum.class, diaStr);
        TurnoEnum turno = EnumParser.parse(TurnoEnum.class, turnoStr);

        Optional<PlanItem> existing =
                planItemRepository.findByPlanSemanalIdAndDiaAndTurno(planId, dia, turno);

        if (existing.isEmpty()) {
            throw new NoSuchElementException("Item no encontrado");
        }

        plan.removeItem(existing.get());
        planRepository.save(plan);
    }

    
    // --------generar plan completo ramdon
    

    @Transactional
    public PlanSemanalResponseDTO rellenarPlanAleatorio(Integer usuarioId, Integer planId) {

        PlanSemanal plan = planRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("Plan no encontrado"));

        checkAccess(usuarioId, plan);

        plan.getItems().clear();

        DiaSemanaEnum[] dias = DiaSemanaEnum.values();
        TurnoEnum[] turnos = TurnoEnum.values();

        List<int[]> cells = new ArrayList<>();
        for (int d = 0; d < dias.length; d++) {
            for (int t = 0; t < turnos.length; t++) {
                cells.add(new int[]{d, t});
            }
        }

        Collections.shuffle(cells);

        Set<Integer> used = new HashSet<>();

        for (int[] cell : cells) {
            DiaSemanaEnum dia = dias[cell[0]];
            TurnoEnum turno = turnos[cell[1]];
            Receta receta = null;

            int attempts = 0;
            while (receta == null && attempts < 20) {
                Optional<Receta> maybe = recetaRepository.findRandom();
                if (maybe.isPresent()) {
                    Receta candidate = maybe.get();
                    if (!used.contains(candidate.getId()) || attempts > 10) {
                        receta = candidate;
                        used.add(candidate.getId());
                    }
                }
                attempts++;
            }

            if (receta == null)
                throw new IllegalStateException("No hay recetas disponibles.");

            PlanItem item = new PlanItem();
            item.setPlanSemanal(plan);
            item.setDia(dia);
            item.setTurno(turno);
            item.setReceta(receta);
            item.setNotas("Generado aleatoriamente");
            plan.addItem(item);
        }

        plan = planRepository.save(plan);
        return mapPlanToResponse(plan);
    }

    
    // ---------helpers privados para tareas especifica, menos repeticion mejor mantenimiento 
    // si queremos cambiar algo... he investigado esto para centralizar
    
    //para convertie PlanItemRequestDTO a PlanItem,
    //búsqueda de la Receta en el repositorio y el parse de los String a Enum que me dabe errores.
    
    private PlanItem buildItemFromRequest(PlanItemRequestDTO dto, PlanSemanal plan) {
        DiaSemanaEnum dia = EnumParser.parse(DiaSemanaEnum.class, dto.getDia());
        TurnoEnum turno = EnumParser.parse(TurnoEnum.class, dto.getTurno());
        Receta receta = recetaRepository.findById(dto.getRecetaId())
                .orElseThrow(() -> new NoSuchElementException("Receta no encontrada"));
        PlanItem item = new PlanItem();
        item.setPlanSemanal(plan);
        item.setDia(dia);
        item.setTurno(turno);
        item.setReceta(receta);
        item.setNotas(dto.getNotas());
        return item;
    }
    //convierte PlanSemanal al DTO de respuesta, con el formateo de fechas y
    //el cálculo de las totalCaloriasSemana que es import
    
    private PlanSemanalResponseDTO mapPlanToResponse(PlanSemanal plan) {
        PlanSemanalResponseDTO dto = planSemanalMapper.toResponseDto(plan);
        BigDecimal total = plan.getItems().stream()
                .map(i -> {
                    Double kcal = 0.0;
                    if (i.getReceta() != null &&
                        i.getReceta().getValorNutricional() != null &&
                        i.getReceta().getValorNutricional().getCaloriasTotales() != null) {
                        kcal = i.getReceta().getValorNutricional().getCaloriasTotales();
                    }
                    int comensales = (plan.getNumComensales() == null ? 1 : plan.getNumComensales());
                    return BigDecimal.valueOf(kcal).multiply(BigDecimal.valueOf(comensales));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalCaloriasSemana(total);
        if (plan.getSemanaInicio() != null)
            dto.setSemanaInicio(plan.getSemanaInicio().toString());
        if (plan.getFechaCreacion() != null)
            dto.setFechaCreacion(plan.getFechaCreacion().toString());
        return dto;
    }
    
    //unico para validar que el usuarioId tiene permiso para tocar el PlanSemanal 
    //mete la lógica especial para el usuario admin, ID 1

    private void checkAccess(Integer usuarioId, PlanSemanal plan) {
        if (usuarioId == null)
            throw new SecurityException("Usuario no autenticado");
        if (Objects.equals(usuarioId, 1))
            return; // admin
        if (!Objects.equals(plan.getUsuario().getId(), usuarioId))
            throw new SecurityException("Acceso denegado");
    }
    
    //Planes por usuario, se me habia olvidado¡¡
    
    
    @Transactional(readOnly = true)
    public List<PlanSemanalResponseDTO> obtenerPlanesDeUsuario(Integer usuarioId) {
        return planRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(planSemanalMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}