import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { PlanService } from '../../core/services/plan.service';
import {
  PlanDetail,
  PlanItem,
  PlanPayload,
  PlanSummary,
} from '../../core/models/plan.model';
import { DayOfWeek } from '../../core/enums/day.enum';
import { MealTurn } from '../../core/enums/meal-turn.enum';

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
  creatingPlan = false;

  loading = true;
  errorMessage = '';
  subtitle = 'Organiza los platos de lunes a domingo';

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

  readonly meals: MealTurn[] = [MealTurn.Comida, MealTurn.Cena];

  constructor(
    private planService: PlanService,
    private router: Router,
  ) {}

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

        if (!this.selectedPlanId) {
          this.onCreatePlan();
          return;
        }

        this.loadPlanDetail(this.selectedPlanId);
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
        this.selectedPlan = { ...plan, items: plan.items ?? [] };
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'No se pudo cargar el plan seleccionado.';
        this.loading = false;
      },
    });
  }

  // Busca y devuelve el item del plan correspondiente a un dia y turno especifico o undefine si no exisste
  getSlot(day: DayOfWeek, meal: MealTurn): PlanItem | undefined {
    return this.selectedPlan?.items?.find(
      (item) => item.dia === day && item.turno === meal,
    );
  }

  // Permite reducir lso nombres de las recetas, cuando son muy largas y poderse visulizar bien.
  displayTitle(recipeTitle?: string | null): string {
    if (!recipeTitle) return '';
    const words = recipeTitle.trim().split(/\s+/);
    if (words.length <= 3) return recipeTitle;
    return `${words.slice(0, 3).join(' ')}…`;
  }

  // Navega a la vista de seleccion de recetas pasando por el plan, dia y turno como parametros si hay plan seleccionado
  handleSlotClick(day: DayOfWeek, meal: MealTurn): void {
    if (!this.selectedPlanId) return;

    this.router.navigate(['/planner/select'], {
      queryParams: { planId: this.selectedPlanId, dia: day, turno: meal },
    });
  }
  // maneja la interacion po rtecladoy, al presionar Enter o Espacio, ejecuta la misma accion que un clic en el slot
  onSlotKey(event: KeyboardEvent, day: DayOfWeek, meal: MealTurn): void {
    if (event.key !== 'Enter' && event.key !== ' ') return;
    event.preventDefault();
    this.handleSlotClick(day, meal);
  }
  //Elimina un item del paln para un dia y turno dados, actualizando el estado local o mostrando un error si falla
  removeSlot(day: DayOfWeek, meal: MealTurn, event: MouseEvent): void {
    event.stopPropagation();
    if (!this.selectedPlanId) return;

    this.planService.deleteItem(this.selectedPlanId, day, meal).subscribe({
      next: () => {
        if (!this.selectedPlan) return;
        this.selectedPlan = {
          ...this.selectedPlan,
          items: this.selectedPlan.items.filter(
            (item) => !(item.dia === day && item.turno === meal),
          ),
        };
      },
      error: () => {
        this.errorMessage = 'No se pudo eliminar la receta del plan.';
      },
    });
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
      next: (plan) =>
        (this.selectedPlan = { ...plan, items: plan.items ?? [] }),
      error: () =>
        (this.errorMessage = 'No se pudo generar el plan aleatorio.'),
    });
  }

  onCreatePlan(): void {
    if (this.creatingPlan) return;

    this.creatingPlan = true;
    this.loading = true;
    this.errorMessage = '';

    const payload = this.buildDefaultPlanPayload();

    this.planService.create(payload).subscribe({
      next: (plan) => {
        this.plans = [
          plan,
          ...this.plans.filter((existing) => existing.id !== plan.id),
        ];
        this.selectedPlanId = plan.id;
        this.selectedPlan = { ...plan, items: plan.items ?? [] };
        this.creatingPlan = false;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'No se pudo crear un plan nuevo.';
        this.creatingPlan = false;
        this.loading = false;
      },
    });
  }

  private buildDefaultPlanPayload(): PlanPayload {
    const now = new Date();
    return {
      nombre: this.generateDefaultName(now),
      semanaInicio: this.getWeekStartISO(now),
      numComensales: 2,
      observaciones: '',
      items: [],
    };
  }

  private getWeekStartISO(date: Date): string {
    const start = new Date(date);
    const jsDay = start.getDay();
    const diff = jsDay === 0 ? -6 : 1 - jsDay;
    start.setDate(start.getDate() + diff);
    start.setHours(0, 0, 0, 0);
    return start.toISOString().split('T')[0];
  }

  private generateDefaultName(date: Date): string {
    const monthNames = [
      'enero',
      'febrero',
      'marzo',
      'abril',
      'mayo',
      'junio',
      'julio',
      'agosto',
      'septiembre',
      'octubre',
      'noviembre',
      'diciembre',
    ];
    const month = monthNames[date.getMonth()] ?? '';
    const weekNumber = Math.max(1, Math.ceil(date.getDate() / 7));
    return `${weekNumber} semana ${month}`.trim();
  }
}
