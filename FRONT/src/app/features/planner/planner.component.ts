import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PlanService } from '../../core/services/plan.service';
import { PlanDetail, PlanItem, PlanPayload, PlanSummary } from '../../core/models/plan.model';
import { DayOfWeek } from '../../core/enums/day.enum';
import { MealTurn } from '../../core/enums/meal-turn.enum';
import { AlertService } from '../../core/services/alert.service';

@Component({
  selector: 'app-planner',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, DatePipe],
  templateUrl: './planner.component.html',
  styleUrl: './planner.component.css',
})
export class PlannerComponent implements OnInit {
  // uso de inject 
  private planService = inject(PlanService);
  private route = inject(ActivatedRoute);
  private alertService = inject(AlertService);
  
  // uso public para que el html no de error de acceso privado que daba fallos el html
  public router = inject(Router); 

  // Estado de la pantalla
  plans: PlanSummary[] = [];
  selectedPlanId: number | null = null;
  selectedPlan?: PlanDetail;
  viewMode: 'list' | 'detail' | 'create' = 'list';
  loading = true;
  errorMessage = '';

  //formulario de nuevo plan
  newPlan: PlanPayload = { 
    nombre: '', 
    semanaInicio: '', 
    numComensales: 2, 
    observaciones: '', 
    items: [] 
  };

  // Mapa para encontrar rápido qué receta va en qué hueco
  private planMap = new Map<string, PlanItem>();
  
  // pintamos días y turnos
  readonly days = [DayOfWeek.Lunes, DayOfWeek.Martes, DayOfWeek.Miércoles, DayOfWeek.Jueves, DayOfWeek.Viernes, DayOfWeek.Sábado, DayOfWeek.Domingo];
  readonly meals = [MealTurn.Comida, MealTurn.Cena];

  ngOnInit(): void {
    // para saber si hay que mostrar la lista o un plan concreto
    this.route.queryParams.subscribe(params => {
      const planId = params['planId'];
      if (planId) {
        this.selectedPlanId = +planId;
        this.loadPlanDetail(this.selectedPlanId);
      } else {
        this.loadPlans();
      }
    });
  }

  loadPlans(): void {
    this.loading = true;
    this.planService.getUserPlans().subscribe({
      next: (data) => { 
        this.plans = data; 
        this.loading = false; 
        this.viewMode = 'list'; 
      },
      error: () => this.loading = false
    });
  }

  loadPlanDetail(planId: number): void {
    this.loading = true;
    this.planService.getById(planId).subscribe({
      next: (plan) => {
        this.selectedPlan = plan;
        this.buildPlanMap(); // Organiza las recetas en el mapa que para eso se ha hecho
        this.viewMode = 'detail';
        this.loading = false;
      },
      error: () => this.irALista()
    });
  }

  // Elimina un plan completo de la base de datos
  borrarPlan(id: number, nombre: string, event: MouseEvent): void {
    event.stopPropagation();
    
    this.alertService.confirmDelete(`el plan semanal "${nombre}"`).then((confirmado) => {
      if (confirmado) {
        this.planService.delete(id).subscribe({
          next: () => {
            this.alertService.success('Eliminado', 'El plan ha desaparecido de tu lista.');
            this.plans = this.plans.filter(p => p.id !== id);
            if (this.selectedPlanId === id) this.irALista();
          },
          error: () => this.alertService.error('Error', 'No hemos podido eliminar el plan.')
        });
      }
    });
  }

  //de relleno aleatorio
  rellenarAleatorio(): void {
    if (!this.selectedPlanId) return;
    
    this.planService.randomize(this.selectedPlanId).subscribe({
      next: () => {
        this.alertService.success('¡Listo!', 'Hemos generado sugerencias para los huecos libres.');
        this.loadPlanDetail(this.selectedPlanId!);
      },
      error: () => this.alertService.error('Ups', 'No hay suficientes recetas para rellenar el plan.')
    });
  }

  confirmCreate() {
    this.errorMessage = '';
    
    // 1. Validaciones actuales (mantenemos los mensajes en pequeño en el HTML)
    if (!this.newPlan.semanaInicio) {
      this.errorMessage = 'Debes seleccionar una fecha de inicio.';
      return;
    }

    const date = new Date(this.newPlan.semanaInicio);
    if (date.getUTCDay() !== 1) { 
      this.errorMessage = 'Lo sentimos, los planes deben empezar obligatoriamente un Lunes.';
      return;
    }

    // 2. Llamada al servicio
    this.planService.create(this.newPlan).subscribe({
      next: (res) => {
        // Guardamos el nombre para el mensaje antes de limpiar el objeto
        const nombrePlan = this.newPlan.nombre || 'el plan semanal';

        // LANZAMOS LA ALERTA DE ÉXITO
        this.alertService.success(
          '¡Plan creado!', 
          `Se ha creado "${nombrePlan}" correctamente. Vamos a añadir las recetas.`
        );

        // Limpiamos el formulario
        this.newPlan = { 
          nombre: '', 
          semanaInicio: '', 
          numComensales: 2, 
          observaciones: '', 
          items: [] 
        };
        this.errorMessage = '';

        // Saltamos automáticamente al detalle (los items del plan)
        this.router.navigate([], { queryParams: { planId: res.id } });
      },
      error: (err) => {
        // Mantenemos el mensaje de error en pequeño si ya existe la semana
        this.errorMessage = 'Ya tienes una planificación para esa semana';
      }
    });
  }

  irALista(): void {
    this.selectedPlanId = null;
    this.newPlan = { 
      nombre: '', 
      semanaInicio: '', 
      numComensales: 2, 
      observaciones: '', 
      items: [] 
    };
    this.errorMessage = '';
    this.router.navigate(['/planner']); // Limpiamos
  }

  handleSlotClick(day: DayOfWeek, meal: MealTurn): void {
    this.router.navigate(['/planner/select'], {
      queryParams: { planId: this.selectedPlanId, dia: day, turno: meal }
    });
  }

  removeSlot(day: DayOfWeek, meal: MealTurn, event: MouseEvent): void {
    event.stopPropagation();
    if (!this.selectedPlanId) return;
    this.planService.deleteItem(this.selectedPlanId, day, meal).subscribe({
      next: () => {
        this.selectedPlan!.items = this.selectedPlan!.items.filter(i => !(i.dia === day && i.turno === meal));
        this.buildPlanMap();
      }
    });
  }

  private buildPlanMap(): void {
    this.planMap.clear();
    this.selectedPlan?.items.forEach(item => this.planMap.set(`${item.dia}-${item.turno}`, item));
  }

  getSlot(day: DayOfWeek, meal: MealTurn) { return this.planMap.get(`${day}-${meal}`); }
}