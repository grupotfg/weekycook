import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { PlanService } from '../../core/services/plan.service';
import { PlanDetail, PlanSummary } from '../../core/models/plan.model';
import { DayOfWeek } from '../../core/enums/day.enum';
import { MealTurn } from '../../core/enums/meal-turn.enum';


// Planner semanal - Alejandra



@Component({
  selector: 'app-planner',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './planner.component.html',
  styleUrl: './planner.component.css',
})
export class PlannerComponent implements OnInit {
  plans: PlanSummary[] = [];
  selectedPlanId: number | null = null;
  selectedPlan?: PlanDetail;

  loading = true;
  errorMessage = '';
  subtitle = 'Organiza los platos de lunes a domingo';
  viewLabel = '';


  // Usa enums si son string 
 readonly days: DayOfWeek[] = [
  DayOfWeek.Lunes,
  DayOfWeek.Martes,
  DayOfWeek.Miércoles,
  DayOfWeek.Jueves,
  DayOfWeek.Viernes,
  DayOfWeek.Sábado,
  DayOfWeek.Domingo,
];

readonly meals: MealTurn[] = [
  MealTurn.Comida,
  MealTurn.Cena,
];


  constructor(private planService: PlanService) {}

  ngOnInit(): void {
    this.loadPlans();
  }

  loadPlans(): void {
    this.loading = true;
    this.errorMessage = '';

    this.planService.getUserPlans().subscribe({
      next: (data) => {
        this.plans = data ?? [];
        this.selectedPlanId = this.plans[0]?.id ?? null;

        if (this.selectedPlanId) {
          this.loadPlanDetail(this.selectedPlanId);
        } else {
          this.selectedPlan = undefined;
          this.loading = false;
        }
      },
      error: () => {
        this.errorMessage = 'No se pudieron cargar los planes.';
        this.loading = false;
      },
    });
  }

  onPlanChange(): void {
    if (this.selectedPlanId) this.loadPlanDetail(this.selectedPlanId);
  }

  loadPlanDetail(planId: number): void {
    this.loading = true;
    this.errorMessage = '';

    this.planService.getById(planId).subscribe({
      next: (plan) => {
        this.selectedPlan = plan;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'No se pudo cargar el plan seleccionado.';
        this.loading = false;
      },
    });
  }

  getSlotTitle(day: DayOfWeek, meal: MealTurn): string | null {
    const item = this.selectedPlan?.items?.find(i => i.dia === day && i.turno === meal);
    return item?.receta?.titulo ?? null;
  }

  onDeletePlan(): void {
    if (!this.selectedPlanId) return;

    this.planService.delete(this.selectedPlanId).subscribe({
      next: () => this.loadPlans(),
      error: () => (this.errorMessage = 'No se pudo eliminar el plan.'),
    });
  }

  onRandomize(): void {
    if (!this.selectedPlanId) return;

    this.planService.randomize(this.selectedPlanId).subscribe({
      next: (plan) => (this.selectedPlan = plan),
      error: () => (this.errorMessage = 'No se pudo generar el plan aleatorio.'),
    });
  }
}


