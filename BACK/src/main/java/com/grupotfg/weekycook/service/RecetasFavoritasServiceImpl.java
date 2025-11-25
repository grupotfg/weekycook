package com.grupotfg.weekycook.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupotfg.weekycook.dto.request.FavoritoRequestDTO;
import com.grupotfg.weekycook.dto.response.FavoritoResponseDTO;
import com.grupotfg.weekycook.entity.RecetasFavoritas;
import com.grupotfg.weekycook.entity.Receta;
import com.grupotfg.weekycook.entity.Usuario;
import com.grupotfg.weekycook.exception.NotFoundException;
import com.grupotfg.weekycook.exception.BadRequestException;
import com.grupotfg.weekycook.exception.ForbiddenException;
import com.grupotfg.weekycook.repository.RecetasFavoritasRepository;
import com.grupotfg.weekycook.repository.RecetaRepository;
import com.grupotfg.weekycook.repository.UsuarioRepository;
import com.grupotfg.weekycook.mapper.RecetasFavoritasMapper;
import com.grupotfg.weekycook.service.RecetasFavoritasService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class RecetasFavoritasServiceImpl implements RecetasFavoritasService {
    @Autowired
    private RecetasFavoritasRepository favoritasRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RecetaRepository recetaRepository;
    @Autowired
    private RecetasFavoritasMapper mapper;

    @Override
    @Transactional
    public FavoritoResponseDTO agregarFavorito(Integer usuarioId, FavoritoRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        Receta receta = recetaRepository.findById(request.getRecetaId())
                .orElseThrow(() -> new NotFoundException("Receta no encontrada"));
        if (favoritasRepository.existsByUsuarioIdAndRecetaId(usuarioId, request.getRecetaId())) {
            throw new BadRequestException("La receta ya está marcada como favorita");
        }
        RecetasFavoritas favorito = mapper.toEntity(request);
        favorito.setUsuario(usuario);
        favorito.setReceta(receta);
        favorito.setFechaGuardado(LocalDateTime.now());
        RecetasFavoritas saved = favoritasRepository.save(favorito);
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoritoResponseDTO> listarFavoritosUsuario(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        if (!usuario.getEsAdmin() && !Objects.equals(usuario.getId(), usuarioId)) {
            throw new ForbiddenException("Acceso denegado");
        }
        return favoritasRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarFavorito(Integer usuarioId, Integer recetaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        if (!usuario.getEsAdmin() && !Objects.equals(usuario.getId(), usuarioId)) {
            throw new ForbiddenException("Acceso denegado");
        }
        if (!favoritasRepository.existsByUsuarioIdAndRecetaId(usuarioId, recetaId)) {
            throw new NotFoundException("La receta no estaba marcada como favorita");
        }
        favoritasRepository.deleteByUsuarioIdAndRecetaId(usuarioId, recetaId);
    }
}