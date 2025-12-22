import { DayOfWeek } from '../enums/day.enum';
import { MealTurn } from '../enums/meal-turn.enum';
import { Recipe } from './recipe.model';

// Resumen de un plan semanal (PlanSemanalResponseDTO)
export interface PlanSummary {
  id: number;
  usuarioId: number;
  nombre: string;
  semanaInicio: string;
  numComensales: number;
  fechaCreacion: string;
  totalCaloriasSemana: number;
}

// Plan detallado con sus items
export interface PlanDetail extends PlanSummary {
  observaciones?: string;
  items: PlanItem[];
}

// Item individual dentro del plan (PlanItemResponseDTO)
export interface PlanItem {
  id: number;
  receta: Recipe;
  dia: DayOfWeek;
  turno: MealTurn;
  notas?: string;
}

// Payload para crear/editar un plan (PlanSemanalRequestDTO)
export interface PlanPayload {
  nombre: string;
  semanaInicio: string; // ISO string desde date picker
  numComensales: number;
  observaciones?: string;
  items: PlanItemPayload[];
}

// Payload para cada item (PlanItemRequestDTO)
export interface PlanItemPayload {
  recetaId: number;
  dia: DayOfWeek;
  turno: MealTurn;
  notas?: string;
}
