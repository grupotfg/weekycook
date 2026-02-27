package com.grupotfg.weekycook.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupotfg.weekycook.dto.request.RecetaIngredienteRequestDTO;
import com.grupotfg.weekycook.dto.request.RecetaRequestDTO;
import com.grupotfg.weekycook.dto.request.RecetaValorNutricionalRequestDTO;
import com.grupotfg.weekycook.dto.response.RecetaDetailResponseDTO;
import com.grupotfg.weekycook.dto.response.RecetaResponseDTO;
import com.grupotfg.weekycook.entity.*;
import com.grupotfg.weekycook.exception.ForbiddenException;
import com.grupotfg.weekycook.exception.NotFoundException;
import com.grupotfg.weekycook.mapper.RecetaMapper;
import com.grupotfg.weekycook.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecetaServiceImpl implements RecetaService {

    private final RecetaRepository recetaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final IngredienteRepository ingredienteRepository;
    // Este repositorio lo necesitamos para el método 'deleteAll', no salia¡¡
    private final RecetaIngredienteRepository recetaIngredienteRepository;

    private final RecetaMapper recetaMapper;

    // ... (los métodos listarRecetas, obtenerRecetaPorId, obtenerRecetaAleatoria no cambian) ...

    @Override
    @Transactional(readOnly = true)
    public RecetaDetailResponseDTO obtenerRecetaPorId(Integer id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Receta no encontrada con ID: " + id));
        return recetaMapper.toRecetaDetailDto(receta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecetaResponseDTO> listarRecetas() {
        return recetaRepository.findAll()
                .stream()
                .map(recetaMapper::toRecetaResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RecetaResponseDTO obtenerRecetaAleatoria() {
        Receta receta = recetaRepository.findRandom()
                .orElseThrow(() -> new NotFoundException("No hay recetas disponibles para modo aleatorio"));
        return recetaMapper.toRecetaResponseDto(receta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecetaResponseDTO> buscarPorTextoYCategoria(String texto, Integer categoriaId) {
        List<Receta> recetas = recetaRepository.buscarPorTextoYCategoria(texto, categoriaId);
        return recetas.stream()
                .map(recetaMapper::toRecetaResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecetaResponseDTO> buscarPorIngrediente(Integer ingredienteId) {
        List<RecetaIngrediente> relaciones = recetaIngredienteRepository.findByIngredienteId(ingredienteId);
        return relaciones.stream()
                .map(RecetaIngrediente::getReceta)
                .distinct()
                .map(recetaMapper::toRecetaResponseDto)
                .collect(Collectors.toList());
    }

    // --------crear receta admin
    
    @Override
    @Transactional
    public RecetaDetailResponseDTO crearReceta(Integer usuarioId, RecetaRequestDTO dto) {
        
        Usuario creador = checkAdmin(usuarioId);
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

        Receta receta = recetaMapper.toEntity(dto);
        receta.setCategoria(categoria);
        receta.setCreadoPor(creador);
        receta.setFechaCreacion(LocalDateTime.now());
        applyValorNutricional(receta, dto.getValorNutricional());
        
        //Inicializamos la lista si no lo está (es importante)
        receta.setRecetaIngredientes(new ArrayList<>());
        
        // Guardamos la receta (sin ingredientes aún) para que la BD genere su ID
        Receta recetaGuardada = recetaRepository.save(receta);
        
        // Lógica de ingredientes (no manual)
        if (dto.getIngredientes() != null) {
            for (RecetaIngredienteRequestDTO itemDTO : dto.getIngredientes()) {
                
                Ingrediente ing = ingredienteRepository.findById(itemDTO.getIngredienteId())
                        .orElseThrow(() -> new NotFoundException("Ingrediente no encontrado con ID: " + itemDTO.getIngredienteId()));

                RecetaIngredienteId idCompuesto = new RecetaIngredienteId(recetaGuardada.getId(), ing.getId());
                
                RecetaIngrediente ri = new RecetaIngrediente(
                        idCompuesto, 
                        recetaGuardada, // <- La receta ya gestionada
                        ing, 
                        itemDTO.getCantidad(), 
                        itemDTO.getUnidad()
                );
                
                // Simplemente añadimos a la lista de la receta.
                // CascadeType.ALL hace el guardado.
                recetaGuardada.getRecetaIngredientes().add(ri);
            }
        }
        
        // Al final guardará la 'recetaGuardada'
        // y (por cascada) todos los 'RecetaIngrediente' en su lista.
        return recetaMapper.toRecetaDetailDto(recetaGuardada);
    }

 // ------actualizar admin
 
 @Override
 @Transactional
 public RecetaDetailResponseDTO actualizarReceta(Integer recetaId, Integer usuarioId, RecetaRequestDTO dto) {

     checkAdmin(usuarioId);

     Receta receta = recetaRepository.findById(recetaId)
             .orElseThrow(() -> new NotFoundException("Receta no encontrada con ID: " + recetaId));

     Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
             .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

     // 1. Actualizar campos 
     receta.setTitulo(dto.getTitulo());
     receta.setDescripcionCorta(dto.getDescripcionCorta());
     receta.setInstrucciones(dto.getInstrucciones());
     receta.setPorciones(dto.getPorciones());
     receta.setTiempoPreparacionMin(dto.getTiempoPreparacionMin());
     receta.setFotoUrl(dto.getFotoUrl());
     receta.setCategoria(categoria);

    // 2. Gestión de ingredientes: Borrar antiguos y añadir nuevos (vamos por orden)
     
     // Borrado (gracias a orphanRemoval=true de la bd que pa esto lo pusimos)
     if (receta.getRecetaIngredientes() != null) {
         receta.getRecetaIngredientes().clear(); // Limpiando la lista, borrare los registros antiguos.
     } else {
         receta.setRecetaIngredientes(new ArrayList<>());
     }

     // Añadir nuevos (gracias a CascadeType.ALL tb bd) 
     if (dto.getIngredientes() != null) {
         for (RecetaIngredienteRequestDTO itemDTO : dto.getIngredientes()) {
             
             Ingrediente ing = ingredienteRepository.findById(itemDTO.getIngredienteId())
                     .orElseThrow(() -> new NotFoundException("Ingrediente no encontrado con ID: " + itemDTO.getIngredienteId()));

             RecetaIngredienteId idCompuesto = new RecetaIngredienteId(receta.getId(), ing.getId());
             RecetaIngrediente ri = new RecetaIngrediente(
                     idCompuesto, receta, ing, itemDTO.getCantidad(), itemDTO.getUnidad()
             );
             
             // y se añade a la lista de la receta.
             receta.getRecetaIngredientes().add(ri);
         }
     }
     
    // 3. Actualizar valor nutricional
    applyValorNutricional(receta, dto.getValorNutricional());

    // 4. Guardar la receta
     Receta recetaActualizada = recetaRepository.save(receta);
     
     return recetaMapper.toRecetaDetailDto(recetaActualizada);
 }

    
    // ------eliminar receta admin
    
    @Override
    @Transactional
    public void eliminarReceta(Integer recetaId, Integer usuarioId) {

        checkAdmin(usuarioId); 

        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new NotFoundException("Receta no encontrada con ID: " + recetaId));
        
        recetaRepository.delete(receta);
    }


    //seguridad que para eso están las excepciones chulas
    private Usuario checkAdmin(Integer usuarioId) {
        Usuario user = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + usuarioId));

        if (user.getEsAdmin() == null || !user.getEsAdmin()) {
            throw new ForbiddenException("Acción no permitida. Se requieren permisos de administrador.");
        }
        return user;
    }

    private void applyValorNutricional(Receta receta, RecetaValorNutricionalRequestDTO valorDTO) {
        if (valorDTO == null) {
            receta.setValorNutricional(null);
            return;
        }

        RecetaValorNutricional valor = receta.getValorNutricional();
        if (valor == null) {
            valor = new RecetaValorNutricional();
        }

        valor.setReceta(receta);
        valor.setCaloriasTotales(valorDTO.getCalorias());
        valor.setProteinasTotales(valorDTO.getProteinas());
        valor.setGrasasTotales(valorDTO.getGrasas());
        valor.setHidratosTotales(valorDTO.getHidratos());
        valor.setFechaCalculo(LocalDateTime.now());

        receta.setValorNutricional(valor);
    }
}
